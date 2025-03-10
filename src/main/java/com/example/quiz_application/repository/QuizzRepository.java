package com.example.quiz_application.repository;
import com.example.quiz_application.model.Question;
import com.example.quiz_application.model.Quizz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizzRepository extends JpaRepository<Quizz, Long> {
}

