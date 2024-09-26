package com.telusko.quiz_service.service;


import com.telusko.quiz_service.dao.QuizDao;
import com.telusko.quiz_service.feign.Quizinterface;
import com.telusko.quiz_service.model.QuestionWrapper;
import com.telusko.quiz_service.model.Quiz;
import com.telusko.quiz_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@Service
public class QuizService
{
    @Autowired
    QuizDao quizDao;

    @Autowired
    Quizinterface quizinterface;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title)
    {
        try
        {
            Pageable pageable = PageRequest.of(0, numQ);
            List<Integer> questions =quizinterface.getQuestionsForQuiz(category,numQ).getBody();
            Quiz quiz = new Quiz();
            quiz.setTitle(title);
            quiz.setQuestionIds(questions);
            quizDao.save(quiz);

            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id)
    {
        Optional<Quiz> quiz= Optional.of(quizDao.findById(id).get());

        List<Integer> questionIds=quiz.get().getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questions =quizinterface.getQuestionsFromId(questionIds);

        return questions;

    }
//
    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses)
    {
        ResponseEntity<Integer>score=quizinterface.getScore(responses);

        return score;
    }
}
