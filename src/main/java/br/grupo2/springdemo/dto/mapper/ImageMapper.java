package br.grupo2.springdemo.dto.mapper;

import org.mapstruct.Mapper;
import br.grupo2.springdemo.domain.Image;
import br.grupo2.springdemo.dto.ImageDto;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    Image toImage(ImageDto imageDto);

    ImageDto toImageDto(Image image);

}
