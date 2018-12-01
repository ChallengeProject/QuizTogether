package me.quiz_together.root.repository.arbitrary;

import static me.quiz_together.root.util.ArbitraryUtils.one;

import java.util.List;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;

import me.quiz_together.root.model.question.QuestionProp;

public class QuestionPropArbitrary {
    private Arbitrary<String> title = Arbitraries.strings()
                                                 .alpha()
                                                 .ofMinLength(1)
                                                 .ofMaxLength(50)
                                                 .unique();

    private Arbitrary<List<String>> options = Arbitraries.strings()
                                                         .alpha()
                                                         .ofMinLength(1)
                                                         .ofMaxLength(50)
                                                         .list()
                                                         .ofSize(3);

    public static QuestionProp defaultOne() {
        return new QuestionPropArbitrary().newOne();
    }

    public Arbitrary<QuestionProp> arbitrary() {
        return Combinators.combine(
                this.title, this.options)
                          .as(QuestionProp::new);
    }

    public QuestionProp newOne() {
        return one(this.arbitrary());
    }
}
