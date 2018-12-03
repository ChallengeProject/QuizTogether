package me.quiz_together.root.repository.arbitrary;

import static me.quiz_together.root.util.ArbitraryUtils.one;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;

import me.quiz_together.root.AbstractDummy;
import me.quiz_together.root.model.broadcast.Broadcast;
import me.quiz_together.root.model.broadcast.BroadcastStatus;
import me.quiz_together.root.model.broadcast.GiftType;

public class BroadcastArbitrary {
    private Arbitrary<Long> userId = Arbitraries.longs()
                                                .greaterOrEqual(1)
                                                .unique();
    private Arbitrary<String> title = Arbitraries.strings()
                                                 .all()
                                                 .ofMinLength(1)
                                                 .ofMaxLength(50);
    private Arbitrary<String> description = Arbitraries.strings()
                                                       .all()
                                                       .ofMinLength(1)
                                                       .ofMaxLength(300);
    private Arbitrary<String> winnerMessage = Arbitraries.strings()
                                                         .all()
                                                         .ofMinLength(1)
                                                         .ofMaxLength(200);
    private Arbitrary<String> giftDescription = Arbitraries.strings()
                                                           .all()
                                                           .ofMinLength(1)
                                                           .ofMaxLength(100);
    private Arbitrary<Long> prize = Arbitraries.longs()
                                               .greaterOrEqual(1);
    private Arbitrary<Long> scheduledTime = Arbitraries.longs()
                                                       .greaterOrEqual(0);
    private Arbitrary<BroadcastStatus> broadcastStatus = Arbitraries.constant(AbstractDummy.getRandomValueType(BroadcastStatus.class))
            .filter(broadcastStatus1 -> broadcastStatus1 != BroadcastStatus.COMPLETED);
    private Arbitrary<GiftType> giftType = Arbitraries.constant(
            AbstractDummy.getRandomValueType(GiftType.class));

    public static Broadcast defaultOne() {
        return new BroadcastArbitrary().newOne();
    }

    public Arbitrary<Broadcast> arbitrary() {
        return Combinators.combine(
                this.userId, this.title, this.winnerMessage,
                this.giftDescription, this.prize, this.scheduledTime, this.broadcastStatus, this.giftType)
                          .as((userId, title, winnerMessage, giftDescription, prize, scheduledTime, broadcastStatus, giftType) ->
                                      Broadcast.builder()
                                               .userId(userId)
                                               .title(title)
                                               .winnerMessage(winnerMessage)
                                               .giftDescription(giftDescription)
                                               .prize(prize)
                                               .scheduledTime(scheduledTime)
                                               .broadcastStatus(broadcastStatus)
                                               .giftType(giftType)
                                               .build());
    }

    public Broadcast newOne() {
        return one(this.arbitrary());
    }
}
