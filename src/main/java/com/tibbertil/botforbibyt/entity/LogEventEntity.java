package com.tibbertil.botforbibyt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "log_event")
public class LogEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "level")
    private Level level;

    @Column(name = "message")
    private String message;

    @Column(name = "timestamp")
    @CreationTimestamp
    private LocalDateTime timestamp;

    @Column(name = "context")
    private String context;

}
