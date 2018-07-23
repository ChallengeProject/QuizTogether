package me.quiz_together.root.service.broadcast;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;
import me.quiz_together.root.repository.broadcast.BroadcastRedisRepository;
import me.quiz_together.root.repository.broadcast.BroadcastRepository;

@Service
public class BroadcastService {
    @Autowired
    private BroadcastRepository broadcastRepository;
    @Autowired
    private BroadcastRedisRepository broadcastRedisRepository;

    public Broadcast getBroadcastById(long broadcastId) {
        return broadcastRepository.selectBroadcastById(broadcastId);
    }

    public List<Broadcast> getPagingBroadcastList(long next, int limit) {
        return broadcastRepository.selectPagingBroadcastList(next, limit);
    }

    public void insertBroadcast(Broadcast broadcast) {
        broadcastRepository.insertBroadcast(broadcast);
    }

    public int updateBroadcast(Broadcast broadcast) {
        return broadcastRepository.updateBroadcast(broadcast);
    }

    public int updateBroadcastStatus(BroadcastStatus broadcastStatus, long broadcastId) {
        return broadcastRepository.updateBroadcastStatus(broadcastStatus, broadcastId);
    }

    public int deleteBroadcastById(long broadcastId) {
        return broadcastRepository.deleteBroadcastById(broadcastId);
    }

    public Set<Long> getPlayUserIds(long broadcastId, int lastStep) {
        return broadcastRedisRepository.selectPlayUserIds(broadcastId, lastStep);
    }

    public Map<Integer, Long> getQuestionAnswerStat(long broadcastId, int step) {
        Map<String, Long> questionAnswerStat = broadcastRedisRepository.selectQuestionAnswerStat(broadcastId, step);
        return questionAnswerStat.entrySet().stream().collect(Collectors.toMap(e -> Integer.valueOf(e.getKey()), e-> e.getValue()));
    }

    public void insertPlayUserAnswer(long broadcastId, long userId, int step, int answerNo) {
        broadcastRedisRepository.insertPlayUserAnswer(broadcastId, userId, step, answerNo);
   }

   public void insertPlayUser(long broadcastId, long userId, int step) {
        broadcastRedisRepository.insertPlayUser(broadcastId, step, userId);
   }

   public void incrementQuestionAnswerStat(long broadcastId, int step, int answerNo) {
        broadcastRedisRepository.incrementQuestionAnswerStat(broadcastId, step, answerNo);
   }
   public boolean isPlayUser(long broadcastId, long userId, int step) {
        return broadcastRedisRepository.isPlayUser(broadcastId, step, userId);
   }

   public void insertBroadcastStep(long broadcastId, int step) {
        broadcastRedisRepository.insertBroadcastStep(broadcastId, step);
   }

   public boolean isCurrentBroadcastStep(long broadcastId, int step) {
        return broadcastRedisRepository.isCurrentBroadcastStep(broadcastId, step);
   }
}
