package me.quiz_together.root.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.quiz_together.root.model.JpaModel;

public interface JpaModelRepository extends JpaRepository<JpaModel, Integer> {
}
