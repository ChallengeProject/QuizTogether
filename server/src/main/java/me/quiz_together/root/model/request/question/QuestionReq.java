package me.quiz_together.root.model.request.question;

import lombok.Data;
import me.quiz_together.root.model.question.Category;
import me.quiz_together.root.model.question.QuestionProp;

@Data
public class QuestionReq {
    private int answerNo;
    private int step;
    private QuestionProp questionProp;
    private Category category;
}
