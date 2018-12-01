package me.quiz_together.root.repository.arbitrary;

import static me.quiz_together.root.util.ArbitraryUtils.one;

import org.apache.commons.lang3.RandomUtils;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;

import me.quiz_together.root.AbstractDummy;
import me.quiz_together.root.model.question.CategoryType;
import me.quiz_together.root.model.question.Question;
import me.quiz_together.root.model.question.QuestionProp;

public class QuestionArbitrary {
    private Arbitrary<Long> userId = Arbitraries.constant(RandomUtils.nextLong());
    private Arbitrary<Long> broadcastId = Arbitraries.constant(RandomUtils.nextLong());
    private Arbitrary<Integer> step = Arbitraries.integers()
                                                 .greaterOrEqual(1)
                                                 .lessOrEqual(12).unique();

    private Arbitrary<Integer> answerNo = Arbitraries.integers()
                                                     .greaterOrEqual(1)
                                                     .lessOrEqual(3);

    private Arbitrary<QuestionProp> questionProp = Arbitraries.constant(QuestionPropArbitrary.defaultOne());

    private Arbitrary<CategoryType> category = Arbitraries.constant(
            AbstractDummy.getRandomValueType(CategoryType.class));

    public static Question defaultOne() {
        return new QuestionArbitrary().newOne();
    }

    public Arbitrary<Question> arbitrary() {
        return Combinators.combine(
                this.userId, this.broadcastId, this.step, this.answerNo, this.questionProp, this.category)
                          .as((userId, broadcastId, step, answerNo, questionProp, category) ->
                                      Question.builder()
                                              .userId(userId)
                                              .broadcastId(broadcastId)
                                              .step(step)
                                              .answerNo(answerNo)
                                              .questionProp(questionProp)
                                              .category(category)
                                              .build());
    }

    public Question newOne() {
        return one(this.arbitrary());
    }
}
