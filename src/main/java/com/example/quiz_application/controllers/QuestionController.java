package com.example.quiz_application.controllers;

import com.example.quiz_application.model.Question;
import com.example.quiz_application.repository.QuestionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

class FileHandlerService{
    private String imageFolderPath = "questions/images/";

    public FileHandlerService(){
        File ducksImagesDirectory = new File(imageFolderPath);
        if(!ducksImagesDirectory.exists()) {
            ducksImagesDirectory.mkdirs();
        }
    }

    public String uploadImage(Long id, MultipartFile file) throws IOException {
        String fileExtension = ".png";
        String filePath = this.imageFolderPath + id + fileExtension;
        Path path = Paths.get(filePath);
        System.out.println("The file " + path + " was saved successfully.");
        file.transferTo(path);
        return filePath;
    }

    public byte[] getImage(Long id) throws IOException {
        String fileExtension = ".png";
        Path path = Paths.get(this.imageFolderPath
                + id + fileExtension);
        byte[] file = Files.readAllBytes(path);
        return file;
    }
}

@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionRepository questionRepository;
    private final FileHandlerService fileHandlerService;

    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
        this.fileHandlerService = new FileHandlerService();
    }

    @GetMapping
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Question> getQuestionById(@PathVariable Long id) {
        return questionRepository.findById(id);
    }

    @PostMapping
    public Question createQuestion(@RequestBody Question question) {
        if (question.getImageUrl() == null || question.getImageUrl().isEmpty()) {
            question.setImageUrl("default-image-url.jpg"); // Set a default placeholder
        }
        return questionRepository.save(question);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Question> updateImage(@PathVariable Long id,
                               @RequestParam MultipartFile file) {
        Optional<Question> q = questionRepository.findById(id);

        if(q.isPresent()){
            Question question = q.get();

            try{
                String filePath = fileHandlerService.uploadImage(id, file);
                question.setImageUrl(filePath);
                questionRepository.save(question);
                return ResponseEntity
                        .status(HttpStatus.FOUND)
                        .body(question);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<?> getImage(@PathVariable Long id) {
        Optional<Question> q = questionRepository.findById(id);

        if(q.isPresent()){
            try{
                byte[] image = fileHandlerService.getImage(id);
                return ResponseEntity.status(HttpStatus.FOUND)
                        .contentType(MediaType.IMAGE_PNG)
                        .body(image);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }
}
