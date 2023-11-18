package br.grupo2.springdemo.service;

import br.grupo2.springdemo.domain.Image;

import java.util.Optional;

public interface ImageService {

    Optional<Image> findById(Long id);

    Image save(Image image);

    void deleteById(Long id);

}
