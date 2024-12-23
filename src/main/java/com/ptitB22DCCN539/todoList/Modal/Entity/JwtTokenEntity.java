package com.ptitB22DCCN539.todoList.Modal.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "jwt_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtTokenEntity {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "token", columnDefinition = "LONGTEXT", nullable = false, unique = true)
    private String token;
    @Column(name = "expired")
    private LocalDateTime expired;

    @ManyToOne
    @JoinColumn(name = "email")
    private UserEntity user;
}
