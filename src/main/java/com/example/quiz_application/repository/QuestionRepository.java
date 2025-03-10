package com.example.quiz_application.repository;
import com.example.quiz_application.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}

