package br.grupo2.springdemo.dto.mapper;

import org.mapstruct.Mapper;
import br.grupo2.springdemo.domain.Tag;
import br.grupo2.springdemo.dto.TagDto;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagDto toTagDto(Tag tag);

    Tag toTag(TagDto tagDto);

    Set<TagDto> toTagsDto(Set<Tag> tags);

    Set<Tag> toTags(Set<TagDto> tagsDto);
}
