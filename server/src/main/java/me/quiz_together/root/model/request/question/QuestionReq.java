package me.quiz_together.root.model.request.question;

import javax.validation.constraints.NotNull;

import lombok.Data;
import me.quiz_together.root.model.question.CategoryType;
import me.quiz_together.root.model.question.QuestionProp;

@Data
public class QuestionReq {
    @NotNull
    private Integer answerNo;
    @NotNull
    private Integer step;
    @NotNull
    private QuestionProp questionProp;
    @NotNull
    private CategoryType category;
}
