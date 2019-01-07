package me.quiz_together.root.repository.question;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.quiz_together.root.model.question.Question;

public interface QuestionJpaRepository extends JpaRepository<Question, Long> {

    List<Question> findByBroadcastId(Long broadcastId);

    Question findByBroadcastIdAndStep(Long broadcastId, int step);

}
