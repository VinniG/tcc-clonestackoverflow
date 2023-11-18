package br.grupo2.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.grupo2.springdemo.domain.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
