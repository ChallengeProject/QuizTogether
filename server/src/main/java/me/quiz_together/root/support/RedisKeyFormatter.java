package me.quiz_together.root.support;

public class RedisKeyFormatter {
    private static final String BROADCAST_STEP = "broadcastStep:";
    private static final String PLAY_USER = "playUser:";
    private static final String PLAY_USER_ANSWER = "playUserAnswer:";
    private static final String VIEWERS = "viewers:";
    private static final String QUESTION_ANSWER_STATISTICS = "question_answer_statistics:";

    public static String getPlayUser(long broadcastId, int step) {
        return PLAY_USER + broadcastId + ':' + step;
    }

    public static String getPlayUserAnswer(long broadcastId, Long userId) {
        return PLAY_USER_ANSWER + broadcastId + ':' + userId;
    }

    public static String getQuestionAnswerStat(long broadcastId, int step) {
        return QUESTION_ANSWER_STATISTICS + broadcastId + ':' + step;
    }

    public static String getCurrentViewers(long broadcastId, long userId) {
        return VIEWERS + broadcastId + ':' + userId;
    }

    public static String getCurrentBroadcastStep(long broadcastId) {
        return BROADCAST_STEP + broadcastId;
    }
}
