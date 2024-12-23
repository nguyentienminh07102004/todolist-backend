package com.ptitB22DCCN539.todoList.Modal.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class RoleEntity {
    @Id
    @Column(name = "code")
    private String code;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @ManyToMany(mappedBy = "roles")
    private List<UserEntity> users;
}
