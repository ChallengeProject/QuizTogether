package me.quiz_together.root.service.broadcast;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;
import me.quiz_together.root.repository.broadcast.BroadcastRepository;

@Service
public class BroadcastService {
    @Autowired
    private BroadcastRepository broadcastRepository;

    public Broadcast getBroadcastById(long broadcastId) {
        return broadcastRepository.selectBroadcastById(broadcastId);
    }

    public List<Broadcast> getPagingBroadcastList(long next, int limit) {
        return broadcastRepository.selectPagingBroadcastList(next, limit);
    }

    public void insertBroadcast(Broadcast broadcast) {
        broadcastRepository.insertBroadcast(broadcast);
    }

    public int updateBroadcast(Broadcast broadcast, long broadcastId) {
        return broadcastRepository.updateBroadcast(broadcast, broadcastId);
    }

    public int updateBroadcastStatus(BroadcastStatus broadcastStatus, long broadcastId) {
        return broadcastRepository.updateBroadcastStatus(broadcastStatus, broadcastId);
    }

    public int deleteBroadcastById(long broadcastId) {
        return broadcastRepository.deleteBroadcastById(broadcastId);
    }
}
