package me.quiz_together.root.repository.broadcast;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.quiz_together.root.IntegrationTest;
import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;
import me.quiz_together.root.repository.arbitrary.BroadcastArbitrary;

class BroadcastRepositoryTest extends IntegrationTest {

    @Autowired
    private BroadcastRepository broadcastRepository;
    private Broadcast broadcast;

    @BeforeEach
    void setUp() {
        broadcast = BroadcastArbitrary.defaultOne();
        broadcastRepository.insertBroadcast(broadcast);
    }

    @Test
    void selectBroadcastById() {
        Broadcast resultBroadcast = broadcastRepository.selectBroadcastById(this.broadcast.getId());
        assertThat(resultBroadcast).isNotNull();
    }

    @Test
    void selectPagingBroadcastList() {
        List<Broadcast> broadcastList = broadcastRepository.selectPagingBroadcastList(0, 50, null);
        assertThat(broadcastList).isNotEmpty();
    }

    @Test
    void selectMyBroadcastList() {
        List<Broadcast> broadcastList = broadcastRepository.selectMyBroadcastList(broadcast.getUserId());
        assertThat(broadcastList).isNotEmpty();
    }

    @Test
    void selectPreparedBroadcastByUserId() {
        int count = broadcastRepository.selectPreparedBroadcastByUserId(broadcast.getUserId());
        assertThat(count).isGreaterThan(0);
    }

    @Test
    void updateBroadcast() {
        int expected = 12;
        broadcast.setQuestionCount(expected);
        broadcastRepository.updateBroadcast(broadcast);
        Broadcast count12Broadcast = broadcastRepository.selectBroadcastById(this.broadcast.getId());
        assertThat(count12Broadcast.getQuestionCount()).isEqualTo(expected);
    }

    @Test
    void updateBroadcastStatus() {
        broadcastRepository.updateBroadcastStatus(BroadcastStatus.COMPLETED, broadcast.getId());
        Broadcast completedBroadcast = broadcastRepository.selectBroadcastById(this.broadcast.getId());
        assertThat(completedBroadcast.getBroadcastStatus()).isEqualByComparingTo(BroadcastStatus.COMPLETED);
    }

    @Test
    void deleteBroadcastById() {
        broadcastRepository.deleteBroadcastById(broadcast.getId());
        Broadcast deletedBroadcast = broadcastRepository.selectBroadcastById(this.broadcast.getId());
        assertThat(deletedBroadcast).isNull();

    }
}