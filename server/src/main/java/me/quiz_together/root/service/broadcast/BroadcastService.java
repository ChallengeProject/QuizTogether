package me.quiz_together.root.service.broadcast;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.exceptions.InaccessiblePermissionException;
import me.quiz_together.root.exceptions.NotMatchException;
import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;
import me.quiz_together.root.model.user.PlayUserStatus;
import me.quiz_together.root.repository.broadcast.BroadcastRedisRepository;
import me.quiz_together.root.repository.broadcast.BroadcastRepository;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastService {
    private static final Integer MAX_BROADCAST = 1;
    private final BroadcastRepository broadcastRepository;
    private final BroadcastRedisRepository broadcastRedisRepository;

    public Broadcast getBroadcastById(long broadcastId) {
        return broadcastRepository.selectBroadcastById(broadcastId);
    }

    public List<Broadcast> getPagingBroadcastList(long next, int limit, Long userId) {
        return broadcastRepository.selectPagingBroadcastList(next, limit, userId);
    }

    public List<Broadcast> getMyBroadcastList(Long userId) {
        return broadcastRepository.selectMyBroadcastList(userId);
    }

    public boolean getPreparedBroadcastByUserId(Long userId) {
        return broadcastRepository.selectPreparedBroadcastByUserId(userId) >= MAX_BROADCAST;
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

    public void checkPermissionBroadcast(long broadcastId, long userId) {
        Broadcast broadcast = getBroadcastById(broadcastId);
        if (broadcast.getUserId() != userId) {
            log.error("해당 유저는 권한이 없습니다. broadcastId : " + broadcastId + " userId : " + userId + "!!");
            throw new InaccessiblePermissionException();
        }
    }

    public void validCurrentBroadcastStep(long broadcastId, int sendStep) {
        Long currentStep = getCurrentBroadcastStep(broadcastId);
        if (currentStep.intValue() != sendStep) {
            log.error("step 불일치!! currentStep :" + currentStep + " sendStep : " + sendStep);
            throw new NotMatchException("현재 step 불일치!!");
        }
    }
}
