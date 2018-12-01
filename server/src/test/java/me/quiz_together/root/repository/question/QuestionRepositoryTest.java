package me.quiz_together.root.repository.question;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.quiz_together.root.IntegrationTest;
import me.quiz_together.root.model.question.Question;
import me.quiz_together.root.repository.arbitrary.QuestionArbitrary;
import me.quiz_together.root.util.ArbitraryUtils;

class QuestionRepositoryTest extends IntegrationTest {
    @Autowired
    private QuestionRepository questionRepository;
    QuestionArbitrary questionArbitrary = new QuestionArbitrary();
    List<Question> questions;
    int size = 12;
    private Question question;

    @BeforeEach
    void setUp() {
        questions = ArbitraryUtils.list(questionArbitrary.arbitrary(), size);
        assertThat(questions).isNotEmpty();
        questionRepository.insertQuestionList(questions);
        assertThat(questions.size()).isEqualTo(size);
        question = questionRepository.selectQuestionListByBroadcastId(questions.iterator().next().getBroadcastId())
                          .iterator().next();
    }

    @Test
    void selectQuestionListByBroadcastId() {
        long broadcastId = question.getBroadcastId();
        List<Question> questions = questionRepository.selectQuestionListByBroadcastId(broadcastId);

        assertThat(questions).isNotEmpty();
    }

    @Test
    void selectQuestionByQuestionId() {
        Question resultQuestion = questionRepository.selectQuestionByQuestionId(question.getId());

        isEqualToQuestion(resultQuestion);
    }

    private void isEqualToQuestion(Question resultQuestion) {
        assertThat(resultQuestion).isNotNull();
        assertThat(resultQuestion.getId()).isEqualTo(question.getId());
    }

    @Test
    void selectQuestionByBroadcastIdAndStep() {
        Question resultQuestion = questionRepository.selectQuestionByBroadcastIdAndStep(
                this.question.getBroadcastId(), questionRepository
                        .selectQuestionByBroadcastIdAndStep(question.getBroadcastId(), question.getStep()).getStep());
        isEqualToQuestion(resultQuestion);

    }

    @Test
    void insertQuestionList() {
        long broadcastId = questions.get(0).getBroadcastId();
        List<Question> resultQuestions = questionRepository.selectQuestionListByBroadcastId(broadcastId);
        assertThat(resultQuestions).isNotEmpty();
        assertThat(resultQuestions.size()).isEqualTo(questions.size());
    }

    @Test
    void updateQuestionListByQuestionId() {
        List<Question> resultQuestions = questionRepository.selectQuestionListByBroadcastId(
                question.getBroadcastId());


        resultQuestions.forEach(question1 -> questionRepository.updateQuestionByQuestionId(question1));
    }
}