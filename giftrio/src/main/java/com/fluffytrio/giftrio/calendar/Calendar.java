package com.fluffytrio.giftrio.calendar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fluffytrio.giftrio.advent.Advent;
import com.fluffytrio.giftrio.settings.Settings;
import com.fluffytrio.giftrio.user.User;
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
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false, updatable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="setting_id", nullable=false)
    private Settings settingId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="calendar_id")
    @JsonIgnore
    private List<Advent> adventList;

    @Column(updatable = false)
    private LocalDate startDate;

    @Column(updatable = false)
    private LocalDate endDate;

    private String title;
    private String detail;
    private String backgroundImg;
    private String password;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isDelete;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void delete() {
        this.isDelete = true;
        for (Advent advent : adventList) {
            advent.delete();
        }
    }

    public void changePassword(String password) {
        this.password = password;
    }
}
