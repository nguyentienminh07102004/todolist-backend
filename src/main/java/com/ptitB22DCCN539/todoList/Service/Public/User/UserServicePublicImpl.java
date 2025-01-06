package com.ptitB22DCCN539.todoList.Service.Public.User;

import com.nimbusds.jwt.SignedJWT;
import com.ptitB22DCCN539.todoList.Bean.ContantVariable;
import com.ptitB22DCCN539.todoList.Bean.USER_STATUS;
import com.ptitB22DCCN539.todoList.CustomerException.DataInvalidException;
import com.ptitB22DCCN539.todoList.CustomerException.ExceptionVariable;
import com.ptitB22DCCN539.todoList.Mapper.User.UserConvertor;
import com.ptitB22DCCN539.todoList.Modal.Entity.JwtTokenEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.RoleEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.UserEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.User.LoginRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserForgotPasswordRequest;
import com.ptitB22DCCN539.todoList.Modal.Request.User.UserRegisterRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.UserResponse;
import com.ptitB22DCCN539.todoList.Redis.CodeVerify.CodeVerifyRedis;
import com.ptitB22DCCN539.todoList.Redis.CodeVerify.ICodeVerifyService;
import com.ptitB22DCCN539.todoList.Repository.IJwtTokenRepository;
import com.ptitB22DCCN539.todoList.Repository.IRoleRepository;
import com.ptitB22DCCN539.todoList.Repository.IUserRepository;
import com.ptitB22DCCN539.todoList.Service.Email.IEmailService;
import com.ptitB22DCCN539.todoList.Service.Token.JwtToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServicePublicImpl implements IUserServicePublic {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserConvertor userConvertor;
    private final JwtToken jwtToken;
    private final IRoleRepository roleRepository;
    private final IEmailService emailService;
    private final IJwtTokenRepository jwtTokenRepository;
    private final ICodeVerifyService codeVerifyService;

    @Value(value = "${google.clientId}")
    private String googleClientId;
    @Value(value = "${google.clientSecret}")
    private String googleClientSecret;
    @Value(value = "${google_redirect_uri}")
    private String googleRedirectUri;
    @Value(value = "${timeToLiveForgetPasswordCode}")
    private Long timeToLiveForgetPasswordCode;
    @Value(value = "${maxDeviceLoginToAccount}")
    private Integer maxDeviceLoginToAccount;
    @Value(value = "${googleGetAccessTokenUrl}")
    private String getAccessTokenUrl;
    @Value(value = "${googleGetUserInfoUrl}")
    private String getUserInfoUrl;


    @Override
    @Transactional
    public JwtTokenEntity login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findById(loginRequest.getEmail())
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.EMAIL_OR_PASSWORD_NOT_MATCH));
        if (loginRequest.getIsSocial() == null || !loginRequest.getIsSocial()) {
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new DataInvalidException(ExceptionVariable.EMAIL_OR_PASSWORD_NOT_MATCH);
            }
        }
        if (user.getStatus().equals(USER_STATUS.INACTIVE)) {
            throw new DataInvalidException(ExceptionVariable.ACCOUNT_INACTIVE);
        }
        // check number of device account login
        List<JwtTokenEntity> jwtTokenList = user.getJwtTokens();
        if (jwtTokenList.size() >= maxDeviceLoginToAccount) {
            throw new DataInvalidException(ExceptionVariable.ACCOUNT_LOGIN_MAX_DEVICE);
        }
        JwtTokenEntity jwtTokenEntity = jwtToken.generateToken(user);
        jwtTokenList.add(jwtTokenEntity);
        userRepository.save(user);
        return jwtTokenEntity;
    }

    @Override
    @Transactional
    public UserResponse register(UserRegisterRequest userRegisterRequest) {
        if (userRepository.existsById(userRegisterRequest.getEmail())) {
            throw new DataInvalidException(ExceptionVariable.EMAIL_ALREADY_EXISTS);
        }
        if (!userRegisterRequest.getPassword().equals(userRegisterRequest.getRePassword())) {
            throw new DataInvalidException(ExceptionVariable.PASSWORD_AND_REPEAT_PASSWORD_NOT_MATCH);
        }
        UserEntity user = userConvertor.registerRequestToEntity(userRegisterRequest);
        userRepository.save(user);
        return userConvertor.entityToResponse(user);
    }

    @Override
    @Transactional
    @SuppressWarnings(value = "rawtypes")
    public JwtTokenEntity loginWithGoogle(String code) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> values = new LinkedMultiValueMap<>();
            values.add(OAuth2ParameterNames.CODE, code);
            values.add(OAuth2ParameterNames.CLIENT_ID, googleClientId);
            values.add(OAuth2ParameterNames.CLIENT_SECRET, googleClientSecret);
            values.add(OAuth2ParameterNames.REDIRECT_URI, googleRedirectUri);
            values.add(OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.AUTHORIZATION_CODE.getValue()); // authorization flow
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(values, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> responseAccessToken = restTemplate.exchange(getAccessTokenUrl, HttpMethod.POST, entity, Map.class);
            if (responseAccessToken.getStatusCode().equals(HttpStatus.OK)) {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setBearerAuth(Objects.requireNonNull(responseAccessToken.getBody()).get(OAuth2ParameterNames.ACCESS_TOKEN).toString());
                HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpHeaders);
                ResponseEntity<Map> responseUserInfo = restTemplate.exchange(getUserInfoUrl, HttpMethod.POST, httpEntity, Map.class);
                String email = Objects.requireNonNull(responseUserInfo.getBody()).get("email").toString();
                if (userRepository.existsById(email)) {
                    return this.login(new LoginRequest(email, null, true));
                }
                RoleEntity roleUser = roleRepository.findById(ContantVariable.ROLE_USER)
                        .orElseThrow(() -> new DataInvalidException(ExceptionVariable.ROLE_NOT_FOUND));
                UserEntity user = UserEntity.builder()
                        .email(email)
                        .fullName(responseUserInfo.getBody().get("name").toString())
                        .roles(List.of(roleUser))
                        .status(USER_STATUS.ACTIVE)
                        .build();
                JwtTokenEntity jwtTokenEntity = jwtToken.generateToken(user);
                user.setJwtTokens(List.of(jwtTokenEntity));
                userRepository.save(user);
                return jwtTokenEntity;
            }
        } catch (Exception exception) {
            throw new DataInvalidException(ExceptionVariable.LOGIN_FAILED);
        }
        throw new DataInvalidException(ExceptionVariable.LOGIN_FAILED);
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        try {
            UserEntity user = userRepository.findById(email)
                    .orElseThrow(() -> new DataInvalidException(ExceptionVariable.EMAIL_NOT_FOUND));
            String code = UUID.randomUUID().toString();
            Map<String, Object> properties = new HashMap<>();
            properties.put(ContantVariable.CODE_VERIFIED, code);
            CodeVerifyRedis codeVerifyRedis = CodeVerifyRedis.builder()
                    .code(code)
                    .email(email)
                    .timeToLive(timeToLiveForgetPasswordCode)
                    .build();
            codeVerifyService.save(codeVerifyRedis);
            properties.put("name", user.getFullName() == null ? user.getEmail() : user.getFullName());
            emailService.sendMailWithTemplate(email, "Forgot Password", "forgotPassword", properties);
        } catch (Exception exception) {
            log.info(exception.getMessage());
        }
    }

    @Override
    @Transactional
    public UserResponse verifyCodeAndSetPassword(UserForgotPasswordRequest userForgotPasswordRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CodeVerifyRedis codeVerifyRedis = codeVerifyService.findByCode(userForgotPasswordRequest.getCode())
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.CODE_INVALID));
        // Đảm bảo email trùng với email của code verify lưu ở redis
        if (!codeVerifyRedis.getEmail().equals(email)) {
            throw new DataInvalidException(ExceptionVariable.EMAIL_NOT_FOUND);
        }
        UserEntity user = userRepository.findById(email)
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.EMAIL_NOT_FOUND));
        if (!userForgotPasswordRequest.getPassword().equals(userForgotPasswordRequest.getRePassword())) {
            throw new DataInvalidException(ExceptionVariable.PASSWORD_AND_REPEAT_PASSWORD_NOT_MATCH);
        }
        if (passwordEncoder.matches(userForgotPasswordRequest.getPassword(), user.getPassword())) {
            throw new DataInvalidException(ExceptionVariable.OLD_PASSWORD_NEW_PASSWORD_MATCH);
        }
        user.setPassword(passwordEncoder.encode(userForgotPasswordRequest.getPassword()));
        // logout after change password
        user.getJwtTokens().forEach(token -> token.setUser(null));
        user.getJwtTokens().clear();
        userRepository.save(user);
        return userConvertor.entityToResponse(user);
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest httpServletRequest) {
        try {
            Cookie[] cookies = httpServletRequest.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(ContantVariable.TOKEN_NAME)) {
                        SignedJWT signedJWT = jwtToken.verifyToken(cookie.getValue());
                        jwtTokenRepository.deleteById(signedJWT.getJWTClaimsSet().getJWTID());
                    }
                }
            }
        } catch (Exception exception) {
            throw new DataInvalidException(ExceptionVariable.TOKEN_INVALID);
        }
    }
}
