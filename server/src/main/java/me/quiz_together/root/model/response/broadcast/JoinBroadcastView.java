package me.quiz_together.root.model.response.broadcast;

import lombok.Data;
import me.quiz_together.root.model.question.QuestionProp;
import me.quiz_together.root.model.user.PlayUserStatus;

@Data
public class JoinBroadcastView {
    private BroadcastView broadcastView;
    private QuestionProp questionProp;
    private int step;
    private int answerNo;
    private PlayUserStatus playUserStatus;
    private Long viewerCount;
}
