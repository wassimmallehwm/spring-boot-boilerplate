package com.boilerplate.modules.base;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode
public class BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    protected Long id;
    @Column(name = "created_at")
    @CreationTimestamp
    protected Date createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    protected Date updatedAt;
    @Column(name = "is_enabled", columnDefinition = "boolean default true")
    protected Boolean isEnabled = true;

    @PrePersist
    private void onCreate() {
        if(createdAt == null){
            createdAt = new Date();
        }
        if(isEnabled == null){
            isEnabled = true;
        }
    }
}