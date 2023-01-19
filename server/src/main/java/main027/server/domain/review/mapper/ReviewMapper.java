package main027.server.domain.review.mapper;

import main027.server.domain.review.dto.ReviewDto;
import main027.server.domain.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "emojiId", target = "emoji.emojiId")
    @Mapping(source = "memberId", target = "member.memberId")
    @Mapping(source = "placeId", target = "place.placeId")
    Review PostToEntity(ReviewDto.Post postDto);

    @Mapping(source = "emoji.emojiId", target = "emojiId")
    @Mapping(source = "member.memberId", target = "memberId")
    @Mapping(source = "place.placeId", target = "placeId")
    ReviewDto.Response entityToResponse(Review review);

    @Mapping(source = "content", target = "reviewList")
    @Mapping(target = "presentPage", expression = "java(Long.valueOf(reviews.getPageable().getPageNumber() + 1))")
    ReviewDto.ListResponse pageToList(Page<Review> reviews);
    
}
