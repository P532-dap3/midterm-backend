package com.example.quiz_application.controllers;

import com.example.quiz_application.model.Question;
import com.example.quiz_application.model.Quizz;
import com.example.quiz_application.repository.QuestionRepository;
import com.example.quiz_application.repository.QuizzRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/quizzes")
public class QuizzController {
    private final QuizzRepository quizzRepository;
    private final QuestionRepository questionRepository;

    public QuizzController(QuizzRepository quizzRepository, QuestionRepository questionRepository) {
        this.quizzRepository = quizzRepository;
        this.questionRepository = questionRepository;
    }

    @GetMapping
    public List<Quizz> getAllQuizzes() {
        return quizzRepository.findAll();
    }

    @GetMapping("/{id}")
    public Quizz getQuizWithQuestions(@PathVariable Long id) {
        Quizz quiz = quizzRepository.findById(id).orElse(null);

        if (quiz != null) {
            List<Question> questions = questionRepository.findAllById(quiz.getQuestionIds());
            quiz.setQuestions(questions);
        }

        return quiz;
    }

    @PostMapping
    public Quizz createQuizz(@RequestBody Quizz quizz) {
        return quizzRepository.save(quizz);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quizz> updateQuiz(@PathVariable Long id, @RequestBody Quizz updatedQuizz) {
        Optional<Quizz> existingQuizzOpt = quizzRepository.findById(id);

        if (existingQuizzOpt.isPresent()) {
            Quizz existingQuizz = existingQuizzOpt.get();

            if (updatedQuizz.getTitle() != null && !updatedQuizz.getTitle().isEmpty()) {
                existingQuizz.setTitle(updatedQuizz.getTitle());
            }

            if (updatedQuizz.getQuestionIds() != null && !updatedQuizz.getQuestionIds().isEmpty()) {
                existingQuizz.setQuestionIds(updatedQuizz.getQuestionIds());
            }

            Quizz savedQuizz = quizzRepository.save(existingQuizz);

            return ResponseEntity.ok(savedQuizz);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
