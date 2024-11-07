package com.duongw.universalpetcare.service;

import com.duongw.universalpetcare.dto.request.ReviewUpdateRequest;
import com.duongw.universalpetcare.model.Review;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IReviewService {
    Review saveReview(Review review, Long reviewerId, Long veterinarianId);
    double getAverageRattingForVet(Long veterinarianId);
    Review updateReview(Long reviewerId, ReviewUpdateRequest request);
    Page<Review> findAllReviewsByUserId(Long userId, int page, int size);

    void deleteReview(Long reviewerId);


    List<Review> findAllByUserId(Long userId);

    void deleteAll(List<Review> reviews);
}
