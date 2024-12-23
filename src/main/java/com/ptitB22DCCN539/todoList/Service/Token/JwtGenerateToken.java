package com.ptitB22DCCN539.todoList.Service.Token;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.ptitB22DCCN539.todoList.Modal.Entity.JwtTokenEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.RoleEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtGenerateToken {
    @Value(value = "${signerKey}")
    private String signerKey;

    public JwtTokenEntity generateToken(UserEntity user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        LocalDateTime exp = LocalDateTime.now().plusDays(1L);
        String id = UUID.randomUUID().toString();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .jwtID(id)
                .expirationTime(Date.from(exp.atZone(ZoneId.systemDefault()).toInstant()))
                .claim("scope", buildScope(user.getRoles()))
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
}
