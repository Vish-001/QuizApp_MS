package com.telusko.question_service.dao;


import com.telusko.question_service.model.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question, Integer>
{

    List<Question> findByCategory(String category);

    @Query(value = "SELECT q.id FROM questions q WHERE q.category = :category ORDER BY RANDOM()", nativeQuery = true)
    List<Integer> findRandomQuestionsByCategory(String category, Pageable pageable);
}
