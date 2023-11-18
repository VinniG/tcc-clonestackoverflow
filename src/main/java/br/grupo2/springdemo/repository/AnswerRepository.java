package br.grupo2.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.grupo2.springdemo.domain.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
