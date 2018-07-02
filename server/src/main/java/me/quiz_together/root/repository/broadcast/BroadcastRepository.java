package me.quiz_together.root.repository.broadcast;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;

@Repository
public class BroadcastRepository {
    @Autowired
    private BroadcastMapper broadcastMapper;

    public Broadcast selectBroadcastById(long broadcastId) {
        return broadcastMapper.selectBroadcastById(broadcastId);
    }

    public List<Broadcast> selectPagingBroadcastList(long next, int limit) {
        return broadcastMapper.selectPagingBroadcastList(next, limit);
    }

    public void insertBroadcast(Broadcast broadcast) {
        broadcastMapper.insertBroadcast(broadcast);
    }

    public int updateBroadcast(Broadcast broadcast, long broadcastId) {
        return broadcastMapper.updateBroadcast(broadcast, broadcastId);
    }

    public int updateBroadcastStatus(BroadcastStatus broadcastStatus, long broadcastId) {
        return broadcastMapper.updateBroadcastStatus(broadcastStatus, broadcastId);
    }

    public int deleteBroadcastById(long broadcastId) {
        return broadcastMapper.deleteBroadcastById(broadcastId, BroadcastStatus.COMPLETED);
    }
}
