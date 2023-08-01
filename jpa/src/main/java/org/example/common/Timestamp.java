package org.example.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Timestamp {

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public void updateCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    public void updateModifiedAt() {
        this.modifiedAt = LocalDateTime.now();
    }
}
