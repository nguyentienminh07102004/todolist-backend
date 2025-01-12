package com.ptitB22DCCN539.todoList.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import com.ptitB22DCCN539.todoList.Bean.ContantVariable;
import com.ptitB22DCCN539.todoList.CustomerException.DataInvalidException;
import com.ptitB22DCCN539.todoList.CustomerException.ExceptionVariable;
import com.ptitB22DCCN539.todoList.Modal.Response.APIResponse;
import com.ptitB22DCCN539.todoList.Repository.IJwtTokenRepository;
import com.ptitB22DCCN539.todoList.Service.Token.JwtToken;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
public class WebSecurityConfig {
    @Value(value = "${apiPrefix}")
    private String apiPrefix;
    @Value(value = "${signerKey}")
    private String SINGER_KEY;
    @Value(value = "${urlFrontEndOrigin}")
    private String urlFrontEndOrigin;

    private final IJwtTokenRepository jwtTokenRepository;
    private final JwtToken jwtToken;

    @Autowired
    public WebSecurityConfig(IJwtTokenRepository jwtTokenRepository, JwtToken jwtToken) {
        this.jwtTokenRepository = jwtTokenRepository;
        this.jwtToken = jwtToken;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(request -> request
                .requestMatchers(HttpMethod.POST, "/%s/users/login".formatted(apiPrefix)).permitAll()
                .requestMatchers(HttpMethod.POST, "/%s/users/register".formatted(apiPrefix)).permitAll()
                .requestMatchers(HttpMethod.POST, "/%s/users/login/google".formatted(apiPrefix)).permitAll()
                .requestMatchers(HttpMethod.PUT, "/%s/users/forgot-password/{email}".formatted(apiPrefix)).permitAll()
                .requestMatchers(HttpMethod.PUT, "/%s/users/verify-code-set-password".formatted(apiPrefix)).permitAll()
                .requestMatchers("/%s/auth/users/**".formatted(apiPrefix))
                    .access(new WebExpressionAuthorizationManager("not isAnonymous()"))
                .requestMatchers(HttpMethod.POST, "/upload/").permitAll()
                .requestMatchers(HttpMethod.POST, "/%s/users/logout".formatted(apiPrefix))
                    .access(new WebExpressionAuthorizationManager("not isAnonymous()"))

                // tasks
                .requestMatchers(HttpMethod.POST, "/%s/auth/tasks/".formatted(apiPrefix))
                    .access(new WebExpressionAuthorizationManager("not isAnonymous()"))
                .requestMatchers(HttpMethod.PUT, "/%s/auth/tasks/".formatted(apiPrefix))
                    .access(new WebExpressionAuthorizationManager("not isAnonymous()"))
                .requestMatchers(HttpMethod.POST, "/%s/auth/email/send-mail".formatted(apiPrefix)).permitAll()
                .requestMatchers(HttpMethod.GET, "/%s/auth/tasks/".formatted(apiPrefix))
                    .access(new WebExpressionAuthorizationManager("not isAnonymous()"))
                .requestMatchers(HttpMethod.GET, "/%s/auth/tasks/{id}".formatted(apiPrefix)).permitAll()
                .requestMatchers(HttpMethod.GET, "/%s/auth/tasks/my-task/".formatted(apiPrefix)).permitAll()
                .requestMatchers(HttpMethod.DELETE, "/%s/auth/tasks/my-tasks/{ids}".formatted(apiPrefix))
                    .access(new WebExpressionAuthorizationManager("not isAnonymous()"))
                .requestMatchers(HttpMethod.GET, "/%s/auth/tasks/deleted/all".formatted(apiPrefix))
                    .access(new WebExpressionAuthorizationManager("not isAnonymous()"))
                .requestMatchers(HttpMethod.PUT, "/%s/auth/tasks/restore/{ids}".formatted(apiPrefix))
                    .access(new WebExpressionAuthorizationManager("not isAnonymous()"))
                .requestMatchers(HttpMethod.DELETE, "/%s/auth/tasks/deleted/completed/{ids}".formatted(apiPrefix))
                    .access(new WebExpressionAuthorizationManager("not isAnonymous()"))
                //fakes
                .requestMatchers(HttpMethod.POST, "/fakes/**").permitAll()

                // category
                .requestMatchers("/%s/auth/categories/**".formatted(apiPrefix))
                    .access(new WebExpressionAuthorizationManager("not isAnonymous()"))
        );
        http.cors(cors -> corsFilter());
        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .bearerTokenResolver(bearerTokenResolver())
                .authenticationEntryPoint(((httpServletRequest, httpServletResponse, authException) -> {
                    // thường là lỗi 401
                    APIResponse response = APIResponse.builder()
                            .message(ExceptionVariable.UNAUTHENTICATED.getMessage())
                            .response(authException.getMessage())
                            .build();
                    ObjectMapper objectMapper = new ObjectMapper();
                    httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    httpServletResponse.setStatus(ExceptionVariable.UNAUTHENTICATED.getStatus().value());
                    httpServletResponse.getWriter().write(objectMapper.writeValueAsString(response));
                    httpServletResponse.getWriter().flush();
                })));
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> {
            // check token is logout ?
            try {
                SignedJWT jwt = jwtToken.verifyToken(token);
                if(!jwtTokenRepository.existsById(jwt.getJWTClaimsSet().getJWTID())) {
                    throw new DataInvalidException(ExceptionVariable.TOKEN_INVALID);
                }
                SecretKey secretKey = new SecretKeySpec(SINGER_KEY.getBytes(), MacAlgorithm.HS512.getName());
                return NimbusJwtDecoder.withSecretKey(secretKey)
                        .macAlgorithm(MacAlgorithm.HS512)
                        .build()
                        .decode(token);
            } catch (Exception exception) {
                throw new DataInvalidException(ExceptionVariable.UNAUTHENTICATED);
            }
        };
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(ContantVariable.ROLE_PREFIX);
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return converter;
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedOrigin(urlFrontEndOrigin);
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Bean
    public BearerTokenResolver bearerTokenResolver() {
        return request -> {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if(cookie.getName().equals(ContantVariable.TOKEN_NAME)) {
                        return cookie.getValue();
                    }
                }
                return null;
            }
            return null;
        };
    }
}
