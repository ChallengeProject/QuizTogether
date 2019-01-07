package me.quiz_together.root.model.broadcast;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Broadcast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, updatable = false)
    private Long userId;
    @Column(nullable = false)
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BroadcastStatus broadcastStatus = BroadcastStatus.CREATED;
    private Long prize;
    private String giftDescription;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GiftType giftType = GiftType.GIFT;
    private String winnerMessage;
    private String code;
    @Enumerated(EnumType.STRING)
    private BroadcastType isPublic = BroadcastType.PUBLIC;
    private Integer questionCount;
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private Long createdTime = System.currentTimeMillis();
    @Column(nullable = false)
    private Long updatedTime;
    private Long scheduledTime;

    @Builder
    public Broadcast(Long id, Long userId, String title, String description,
                     BroadcastStatus broadcastStatus, Long prize, String giftDescription,
                     GiftType giftType, String winnerMessage, String code,
                     BroadcastType isPublic, Integer questionCount, Long updatedTime, Long scheduledTime) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.broadcastStatus = broadcastStatus;
        this.prize = prize;
        this.giftDescription = giftDescription;
        this.giftType = giftType;
        this.winnerMessage = winnerMessage;
        this.code = code;
        this.isPublic = isPublic;
        this.questionCount = questionCount;
        this.updatedTime = updatedTime;
        this.scheduledTime = scheduledTime;
    }
}
