package me.quiz_together.root.repository.broadcast;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import me.quiz_together.root.support.RedisKeyFormatter;

@Repository
public class BroadcastRedisRepository {
    private static final String STEP_KEY = "step:";
    @Autowired
    private RedisTemplate<String, Long> longRedisTemplate;
    @Autowired
    private RedisTemplate<String, Integer> integerRedisTemplate;

    /////////////////////////
    // playUser
    /////////////////////////
    public void insertPlayUser(long broadcastId, int step, long userId) {
        longRedisTemplate.opsForSet().add(RedisKeyFormatter.getPlayUser(broadcastId, step), userId);
    }

    public boolean isPlayUser(long broadcastId, int step, long userId) {
        return longRedisTemplate.opsForSet().isMember(RedisKeyFormatter.getPlayUser(broadcastId, step), userId);
    }

    public Set<Long> selectPlayUserIds(long broadcastId, int step) {
        return longRedisTemplate.opsForSet().members(RedisKeyFormatter.getPlayUser(broadcastId, step));
    }

    public void deletePlayUser(long broadcastId, int step) {
        longRedisTemplate.delete(RedisKeyFormatter.getPlayUser(broadcastId, step));
    }

    /////////////////////////
    // playUserAnswer
    /////////////////////////
    public void insertPlayUserAnswer(long broadcastId, long userId, int step, int answerNo) {
        integerRedisTemplate.opsForHash().put(RedisKeyFormatter.getPlayUserAnswer(broadcastId, userId), STEP_KEY
                                                                                                        + step,
                                              answerNo);
    }

    public Integer selectPlayUserAnswer(long broadcastId, long userId, int step) {
        return (Integer) integerRedisTemplate.opsForHash().get(RedisKeyFormatter.getPlayUserAnswer(broadcastId, userId),
                                              STEP_KEY + step);
    }

    public void deletePlayUserAnswer(long broadcastId, long userId) {
        longRedisTemplate.delete(RedisKeyFormatter.getPlayUserAnswer(broadcastId, userId));
    }


    /////////////////////////
    // questionAnswerStat
    /////////////////////////

    public void incrementQuestionAnswerStat(long broadcastId, int step, int answerNo) {
        integerRedisTemplate.opsForHash().increment(RedisKeyFormatter.getQuestionAnswerStat(broadcastId, step), String.valueOf(answerNo), 1);
    }

    public Map selectQuestionAnswerStat(long broadcastId, int step) {
        return integerRedisTemplate.opsForHash().entries(RedisKeyFormatter.getQuestionAnswerStat(broadcastId, step));
    }

    public void deleteQuestionAnswerStat(long broadcastId, int step) {
        integerRedisTemplate.delete(RedisKeyFormatter.getQuestionAnswerStat(broadcastId, step));
    }

    /////////////////////////
    // broadcastStep
    /////////////////////////

    public void insertBroadcastStep(long broadcastId, int step) {
        integerRedisTemplate.opsForSet().add(RedisKeyFormatter.getCurrentBroadcastStep(broadcastId), step);
    }

    public boolean isCurrentBroadcastStep(long broadcastId, int step) {
        return integerRedisTemplate.opsForSet().isMember(RedisKeyFormatter.getCurrentBroadcastStep(broadcastId), step);
    }

    public void deleteBroadcastStep(long broadcastId) {
        integerRedisTemplate.delete(RedisKeyFormatter.getCurrentBroadcastStep(broadcastId));
    }

}
