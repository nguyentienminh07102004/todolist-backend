package com.ptitB22DCCN539.todoList.Service.Commons.User;

import com.ptitB22DCCN539.todoList.CustomerException.DataInvalidException;
import com.ptitB22DCCN539.todoList.Mapper.User.UserConvertor;
import com.ptitB22DCCN539.todoList.Modal.Entity.JwtTokenEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.RoleEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.UserEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.User.LoginRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserChangePasswordRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserRegisterRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.UserResponse;
import com.ptitB22DCCN539.todoList.Repository.IRoleRepository;
import com.ptitB22DCCN539.todoList.Repository.IUserRepository;
import com.ptitB22DCCN539.todoList.Service.Token.JwtGenerateToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceCommonsImpl implements IUserServiceCommons {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserConvertor userConvertor;
    private final JwtGenerateToken jwtGenerateToken;
    private final IRoleRepository roleRepository;

    @Value(value = "${google.clientId}")
    private String googleClientId;
    @Value(value = "${google.clientSecret}")
    private String googleClientSecret;


    @Override
    public String login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findById(loginRequest.getEmail())
                .orElseThrow(() -> new DataInvalidException("Email or password is invalid!"));
        if(loginRequest.getIsSocial() == null || !loginRequest.getIsSocial()) {
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new DataInvalidException("Email or password is invalid!");
            }
        }
        if (user.getStatus().equalsIgnoreCase("INACTIVE")) {
            throw new DataInvalidException("Account is invalid!");
        }
        // kiểm tra xem user đăng nhập bao nhiêu máy rồi
        // tối đa 3 máy(3 token)
        List<JwtTokenEntity> jwtTokenList = user.getJwtTokens();
        if (jwtTokenList.size() >= 3) {
            // không cho đăng nhập nữa
            throw new DataInvalidException("Exceed the number of machines that can log in");
        }
        JwtTokenEntity jwtTokenEntity = jwtGenerateToken.generateToken(user);
        jwtTokenList.add(jwtTokenEntity);
        userRepository.save(user);
        return jwtTokenEntity.getToken();
    }

    @Override
    @Transactional
    public UserResponse register(UserRegisterRequest userRegisterRequest) {
        if (userRepository.existsById(userRegisterRequest.getEmail())) {
            throw new DataInvalidException("Email is already in use!");
        }
        if (!userRegisterRequest.getPassword().equals(userRegisterRequest.getRePassword())) {
            throw new DataInvalidException("Password and repeat password do not match!");
        }
        UserEntity user = userConvertor.registerRequestToEntity(userRegisterRequest);
        userRepository.save(user);
        return userConvertor.entityToResponse(user);
    }

    @Override
    public UserEntity getUserEntityById(String email) {
        return userRepository.findById(email)
                .orElseThrow(() -> new DataInvalidException("Email is not found!"));
    }

    @Override
    public UserResponse changePassword(UserChangePasswordRequest userChangePasswordRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = this.getUserEntityById(email);
        if(!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new DataInvalidException("Old password is invalid!");
        }
        if(passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new DataInvalidException("New password is invalid!");
        }
        if(!userChangePasswordRequest.getNewPassword().equals(userChangePasswordRequest.getReNewPassword())) {
            throw new DataInvalidException("New password and repeat password do not match!");
        }
        for (JwtTokenEntity jwtTokenEntity : user.getJwtTokens()) {
            jwtTokenEntity.setUser(null);
        }
        user.getJwtTokens().clear();
        user.setPassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
        userRepository.save(user);
        return userConvertor.entityToResponse(user);
    }

    @Override
    @Transactional
    public String loginWithGoogle(String code) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            // set các tham số cần thiết cho quá trình lấy accessToken
            MultiValueMap<String, String> values = new LinkedMultiValueMap<>();
            values.add("code", code);
            values.add("client_id", googleClientId);
            values.add("client_secret", googleClientSecret);
            values.add("redirect_uri", "http://localhost:5173/login?social=google");
            String getAccessTokenUrl = "https://www.googleapis.com/oauth2/v4/token";
            String getUserInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
            values.add("grant_type", AuthorizationGrantType.AUTHORIZATION_CODE.getValue()); // xác định đúng authorization flow
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(values, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> responseAccessToken = restTemplate.exchange(getAccessTokenUrl, HttpMethod.POST, entity, Map.class);
            if(responseAccessToken.getStatusCode().equals(HttpStatus.OK)) {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setBearerAuth(responseAccessToken.getBody().get("access_token").toString());
                HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpHeaders);
                ResponseEntity<Map> responseUserInfo = restTemplate.exchange(getUserInfoUrl, HttpMethod.POST, httpEntity, Map.class);
                String email = responseUserInfo.getBody().get("email").toString();
                if(userRepository.existsById(email)) {
                    return this.login(new LoginRequest(email, null, true));
                }
                RoleEntity roleUser = roleRepository.findById("USER")
                        .orElseThrow(() -> new DataInvalidException("Role is not found!"));
                UserEntity user = UserEntity.builder()
                        .email(email)
                        .fullName(responseUserInfo.getBody().get("name").toString())
                        .roles(List.of(roleUser))
                        .status("ACTIVE")
                        .build();
                JwtTokenEntity jwtTokenEntity = jwtGenerateToken.generateToken(user);
                user.setJwtTokens(List.of(jwtTokenEntity));
                userRepository.save(user);
                return jwtTokenEntity.getToken();
            }
        } catch (Exception exception) {
            throw new DataInvalidException("Login failed!");
        }
        throw new DataInvalidException("Login failed!");
    }
}
