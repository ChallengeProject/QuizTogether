package me.quiz_together.root.model.question;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.quiz_together.root.support.converter.QuestionPropConverter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, updatable = false)
    private Long userId;
    @Column(nullable = false, updatable = false)
    private Long broadcastId;
    @Column(nullable = false)
    private Integer step;
    @Column(nullable = false)
    @Convert(converter = QuestionPropConverter.class)
    private QuestionProp questionProp;
    @Column(nullable = false)
    private Integer answerNo;
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private Long createdTime = System.currentTimeMillis();
    @Column(nullable = false)
    private Long updatedTime = System.currentTimeMillis();
    private CategoryType category = CategoryType.NORMAL;

    @Builder
    public Question(Long userId, Long broadcastId, Integer step,
                    QuestionProp questionProp, Integer answerNo) {
        this.userId = userId;
        this.broadcastId = broadcastId;
        this.step = step;
        this.questionProp = questionProp;
        this.answerNo = answerNo;
    }
}
