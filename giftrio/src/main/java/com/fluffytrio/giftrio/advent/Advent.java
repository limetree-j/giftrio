package com.fluffytrio.giftrio.advent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fluffytrio.giftrio.calendar.Calendar;
import com.fluffytrio.giftrio.users.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Advent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false, updatable = false)
    private Users userId;

    @ManyToOne
    @JoinColumn(name="calendar_id", nullable = false, updatable = false)

    @JsonIgnore
    private Calendar calendarId;

    private int seqNum;
    private LocalDate adventDate;

    @Column(length = 128, nullable = false)
    private String text;
    private String img;

    private boolean isOpen;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
