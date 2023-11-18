package br.grupo2.springdemo.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.grupo2.springdemo.domain.Image;
import br.grupo2.springdemo.repository.ImageRepository;
import br.grupo2.springdemo.service.ImageService;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Image> findById(Long id) {
        return imageRepository.findById(id);
    }

    @Override
    public Image save(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public void deleteById(Long id) {
        imageRepository.deleteById(id);
    }

}
