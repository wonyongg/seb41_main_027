package main027.server.domain.review.service;

import lombok.RequiredArgsConstructor;
import main027.server.domain.review.entity.Review;
import main027.server.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

}