package com.ptitB22DCCN539.todoList.Service.Token;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.ptitB22DCCN539.todoList.CustomerException.DataInvalidException;
import com.ptitB22DCCN539.todoList.CustomerException.ExceptionVariable;
import com.ptitB22DCCN539.todoList.Modal.Entity.JwtTokenEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.RoleEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtToken {
    @Value(value = "${signerKey}")
    private String signerKey;
    @Value(value = "${tokenMaxAgeDays}")
    private Long tokenMaxAge;

    public JwtTokenEntity generateToken(UserEntity user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        LocalDateTime exp = LocalDateTime.now().plusDays(tokenMaxAge);
        String id = UUID.randomUUID().toString();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .jwtID(id)
                .expirationTime(Date.from(exp.atZone(ZoneId.systemDefault()).toInstant()))
                .claim(OAuth2ParameterNames.SCOPE, buildScope(user.getRoles()))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            JWSSigner signer = new MACSigner(signerKey.getBytes());
            jwsObject.sign(signer);
            String token = jwsObject.serialize();
            return JwtTokenEntity.builder()
                    .token(token)
                    .id(id)
                    .expired(exp)
                    .user(user)
                    .build();
        } catch (JOSEException exception) {
            return null;
        }
    }

    public String buildScope(List<RoleEntity> roles) {
        return roles.stream().map(RoleEntity::getCode).collect(Collectors.joining(" "));
    }

    public SignedJWT verifyToken(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date exp = signedJWT.getJWTClaimsSet().getExpirationTime();
            boolean isVerify = signedJWT.verify(verifier) && (exp.after(new Date(System.currentTimeMillis())));
            if (!isVerify) {
                throw new DataInvalidException(ExceptionVariable.UNAUTHENTICATED);
            }
            return signedJWT;
        } catch (JOSEException | ParseException exception) {
            throw new DataInvalidException(ExceptionVariable.UNAUTHENTICATED);
        }
    }
}
