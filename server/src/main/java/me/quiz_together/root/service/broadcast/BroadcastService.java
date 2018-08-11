package me.quiz_together.root.service.broadcast;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;
import me.quiz_together.root.model.user.PlayUserStatus;
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

    public List<Broadcast> getPagingBroadcastList(long next, int limit, Long userId) {
        return broadcastRepository.selectPagingBroadcastList(next, limit, userId);
    }

    public List<Broadcast> getMyBroadcastList(Long userId) {
        return broadcastRepository.selectMyBroadcastList(userId);
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

    public Map<Integer, Integer> getQuestionAnswerStat(long broadcastId, int step) {
        Map<String, Integer> questionAnswerStat = broadcastRedisRepository.selectQuestionAnswerStat(broadcastId,
                                                                                                    step);
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

    public PlayUserStatus getPlayUserStatus(long broadcastId, long userId, int step) {
        boolean isFirstStepJoinUser = broadcastRedisRepository.isPlayUser(broadcastId, 1, userId);
        boolean isPlayUser = broadcastRedisRepository.isPlayUser(broadcastId, step - 1, userId);

        if (step == 1) {
            // 1단계에선 모든 유저가 PLAY 상태
            // TODO : 만약 늦게 들어오는 경우 어떻게 처리 할지
            return PlayUserStatus.PLAY;
        } else if (isFirstStepJoinUser && isPlayUser) {
            return PlayUserStatus.PLAY;
        } else if (isFirstStepJoinUser) {
            return PlayUserStatus.LOSER;
        }

        return PlayUserStatus.VIEWER;
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

    public Long getCurrentBroadcastStep(long broadcastId) {
        return broadcastRedisRepository.getCurrentBroadcastStep(broadcastId);
    }

    public void insertViewer(long broadcastId, long userId) {
        broadcastRedisRepository.insertViewer(broadcastId, userId);
    }

    public void deleteViewer(long broadcastId, long userId) {
        broadcastRedisRepository.deleteViewer(broadcastId, userId);
    }

    public Long getCurrentViewers(long broadcastId) {
        return broadcastRedisRepository.getCurrentViewers(broadcastId);
    }
}
