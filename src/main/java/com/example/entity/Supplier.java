package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "supplier")
@AllArgsConstructor
@NoArgsConstructor
@EnableJpaAuditing
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class Supplier {

    @Id
    @Column
    private UUID id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String address;

    @Column
    private String website;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
