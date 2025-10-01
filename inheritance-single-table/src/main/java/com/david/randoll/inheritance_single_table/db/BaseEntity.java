package com.david.randoll.inheritance_single_table.db;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@MappedSuperclass
@SuperBuilder
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public abstract class BaseEntity {
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    @Size(max = 255)
    private String createdBy = "anonymous";

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate = Instant.now();

    @LastModifiedBy
    @Size(max = 255)
    @Column(name = "last_modified_by", nullable = false)
    private String lastModifiedBy = "anonymous";

    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false)
    private Instant lastModifiedDate = Instant.now();
}
