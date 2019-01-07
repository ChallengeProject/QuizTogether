package me.quiz_together.root.repository.broadcast;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import me.quiz_together.root.JpaRepositoryTest;
import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;
import me.quiz_together.root.repository.arbitrary.BroadcastArbitrary;
import me.quiz_together.root.util.ArbitraryUtils;

class BroadcastRepositoryTest extends JpaRepositoryTest {

    @Autowired
    private BroadcastJpaRepository broadcastJpaRepository;
    private Broadcast broadcast;

    @BeforeEach
    void setUp() {
        broadcast = BroadcastArbitrary.defaultOne();
        broadcastJpaRepository.save(broadcast);
        flushAndClear();
    }

    @Test
    void selectBroadcastById() {
        Broadcast resultBroadcast = broadcastJpaRepository.findById(this.broadcast.getId()).orElse(null);
        assertThat(resultBroadcast).isNotNull();
    }

    @Test
    void selectPagingBroadcastList() {
        PageRequest pageRequest = PageRequest.of(0, 50, Sort.by(Direction.DESC, "id"));

        broadcastJpaRepository.saveAll(ArbitraryUtils.list(new BroadcastArbitrary().arbitrary(), 100));

        List<Broadcast> broadcastList =
                broadcastJpaRepository.findBroadcastsByUserIdNotAndBroadcastStatusNotOrderByScheduledTime(
                        broadcast.getUserId(), BroadcastStatus.COMPLETED, pageRequest);
        assertThat(broadcastList).isNotEmpty();
    }

    @Test
    void selectMyBroadcastList() {
        Broadcast completedBroadcast = BroadcastArbitrary.defaultOne();
        completedBroadcast.setBroadcastStatus(BroadcastStatus.COMPLETED);
        completedBroadcast.setUserId(broadcast.getUserId());
        broadcastJpaRepository.save(completedBroadcast);
        flushAndClear();

        List<Broadcast> broadcastList =
                broadcastJpaRepository.findByUserIdAndBroadcastStatusNotOrderByScheduledTime(
                        broadcast.getUserId(), BroadcastStatus.COMPLETED);
        assertThat(broadcastList).isNotEmpty();
        assertThat(broadcastList).hasSize(1);
    }

    @Test
    void selectPreparedBroadcastByUserId() {
        Broadcast completedBroadcast = BroadcastArbitrary.defaultOne();
        completedBroadcast.setBroadcastStatus(BroadcastStatus.COMPLETED);
        completedBroadcast.setUserId(broadcast.getUserId());
        broadcastJpaRepository.save(completedBroadcast);
        flushAndClear();

        int count = broadcastJpaRepository.countByUserIdAndBroadcastStatusNot(broadcast.getUserId(),
                                                                              BroadcastStatus.COMPLETED);

        assertThat(count).isEqualTo(1);
    }

    @Test
    void updateBroadcast() {
        int expected = 12;
        broadcast.setQuestionCount(expected);
        broadcastJpaRepository.save(broadcast);
        flushAndClear();
        Broadcast count12Broadcast = broadcastJpaRepository.findById(this.broadcast.getId()).orElse(null);
        assertThat(count12Broadcast.getQuestionCount()).isEqualTo(expected);
    }

    @Test
    void updateBroadcastStatus() {
        Optional<Broadcast> byId = broadcastJpaRepository.findById(broadcast.getId());
        Broadcast broadcast = byId.get();
        broadcast.setBroadcastStatus(BroadcastStatus.COMPLETED);
        Broadcast savedBroadcast = broadcastJpaRepository.save(broadcast);
        flushAndClear();
        Broadcast completedBroadcast = broadcastJpaRepository.findById(savedBroadcast.getId()).orElse(null);
        assertThat(completedBroadcast.getBroadcastStatus()).isEqualByComparingTo(BroadcastStatus.COMPLETED);
    }

    @Test
    void deleteBroadcastById() {
        broadcastJpaRepository.deleteById(broadcast.getId());
        flushAndClear();
        Broadcast deletedBroadcast = broadcastJpaRepository.findById(this.broadcast.getId()).orElse(null);
        assertThat(deletedBroadcast).isNull();

    }
}