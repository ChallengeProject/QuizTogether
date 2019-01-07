package me.quiz_together.root.repository.broadcast;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;

public interface BroadcastJpaRepository extends JpaRepository<Broadcast, Long> {

    List<Broadcast> findBroadcastsByUserIdNotAndBroadcastStatusNotOrderByScheduledTime(Long userId,
                                                                                       BroadcastStatus broadcastStatus,
                                                                                       Pageable pageable);

    List<Broadcast> findByUserIdAndBroadcastStatusNotOrderByScheduledTime(Long userId,
                                                                          BroadcastStatus broadcastStatus);

    int countByUserIdAndBroadcastStatusNot(Long userId, BroadcastStatus broadcastStatus);
}
