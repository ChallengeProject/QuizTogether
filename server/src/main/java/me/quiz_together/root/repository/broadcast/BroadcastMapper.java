package me.quiz_together.root.repository.broadcast;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;

@Mapper
public interface BroadcastMapper {

    Broadcast selectBroadcastById(@Param("broadcastId") long braodcastId);

    List<Broadcast> selectPagingBroadcastList(@Param("next") long next, @Param("limit") int limit);

    void insertBroadcast(Broadcast broadcast);

    int updateBroadcast(Broadcast broadcast, @Param("broadcastId") long broadcastId);

    int updateBroadcastStatus(@Param("broadcastStatus") BroadcastStatus broadcastStatus, @Param("broadcastId") long broadcastId);

    int deleteBroadcastById(@Param("broadcastId") long broadcastId, @Param("broadcastStatus") BroadcastStatus broadcastStatus);

}
