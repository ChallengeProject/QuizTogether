package me.quiz_together.root.repository.question;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.quiz_together.root.JpaRepositoryTest;
import me.quiz_together.root.model.question.Question;
import me.quiz_together.root.repository.arbitrary.QuestionArbitrary;
import me.quiz_together.root.util.ArbitraryUtils;

class QuestionRepositoryTest extends JpaRepositoryTest {
    @Autowired
    private QuestionJpaRepository questionJpaRepository;
    QuestionArbitrary questionArbitrary = new QuestionArbitrary();
    List<Question> questions;
    int size = 12;
    private Question question;

    @BeforeEach
    void setUp() {
        questions = ArbitraryUtils.list(questionArbitrary.arbitrary(), size);
        assertThat(questions).isNotEmpty();
        for (Question question : questions) {
            questionJpaRepository.save(question);
        }
        assertThat(questions.size()).isEqualTo(size);
        question = questionJpaRepository.findByBroadcastId(questions.iterator().next().getBroadcastId())
                                        .iterator().next();
    }

    @Test
    void selectQuestionListByBroadcastId() {
        long broadcastId = question.getBroadcastId();
        List<Question> questions = questionJpaRepository.findByBroadcastId(broadcastId);

        assertThat(questions).isNotEmpty();
    }

    @Test
    void selectQuestionByQuestionId() {
        Question resultQuestion = questionJpaRepository.findById(question.getId()).orElse(null);

        isEqualToQuestion(resultQuestion);
    }

    private void isEqualToQuestion(Question resultQuestion) {
        assertThat(resultQuestion).isNotNull();
        assertThat(resultQuestion.getId()).isEqualTo(question.getId());
    }

    @Test
    void selectQuestionByBroadcastIdAndStep() {
        Question resultQuestion = questionJpaRepository.findByBroadcastIdAndStep(
                this.question.getBroadcastId(), question.getStep());
        isEqualToQuestion(resultQuestion);

    }

    @Test
    void insertQuestionList() {
        long broadcastId = questions.get(0).getBroadcastId();
        List<Question> resultQuestions = questionJpaRepository.findByBroadcastId(broadcastId);
        assertThat(resultQuestions).isNotEmpty();
        assertThat(resultQuestions.size()).isEqualTo(questions.size());
    }

    @Test
    void updateQuestionListByQuestionId() {
        List<Question> resultQuestions = questionJpaRepository.findByBroadcastId(
                question.getBroadcastId());

        resultQuestions.forEach(question1 -> questionJpaRepository.save(question1));
    }
}