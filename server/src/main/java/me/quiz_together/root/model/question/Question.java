package me.quiz_together.root.model.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Question {
    private Long id;
    private Long userId;
    private Long broadcastId;
    private Integer step;
    private QuestionProp questionProp;
    private Integer answerNo;
    private Long createdTime;
    private Long updatedTime;
    private CategoryType category;

}
