package com.ptitB22DCCN539.todoList.Modal.Entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;
    @LastModifiedBy
    @Column(name = "modified_by", insertable = false)
    private String modifiedBy;
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(name = "modified_date", insertable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime modifiedDate;
}
