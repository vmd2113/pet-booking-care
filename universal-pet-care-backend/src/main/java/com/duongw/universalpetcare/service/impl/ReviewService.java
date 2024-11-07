package com.duongw.universalpetcare.service.impl;

import com.duongw.universalpetcare.dto.request.ReviewUpdateRequest;
import com.duongw.universalpetcare.exception.AlreadyExistsException;
import com.duongw.universalpetcare.exception.ResourceNotFoundException;

import com.duongw.universalpetcare.model.Review;
import com.duongw.universalpetcare.model.User;
import com.duongw.universalpetcare.repository.AppointmentRepository;
import com.duongw.universalpetcare.repository.ReviewRepository;
import com.duongw.universalpetcare.repository.UserRepository;
import com.duongw.universalpetcare.service.IReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public Review saveReview(Review review, Long reviewerId, Long veterinarianId) {

        if (veterinarianId.equals(reviewerId)) {
            throw new IllegalArgumentException("Can not review");
        }

        Optional<Review> existingReview = reviewRepository.findByVeterinarianIdAndPatientId(veterinarianId, reviewerId);
        if (existingReview.isPresent()) {
            throw new AlreadyExistsException("Already existed review");
        }

//        boolean hadCompletedAppointments = appointmentRepository.existsByVeterinarianIdAndPatientIdAndStatus(veterinarianId, reviewerId, AppointmentStatus.COMPLETED);
//        if (!hadCompletedAppointments) {
//            throw new IllegalStateException("Not allowed");
//        }

        User veterinarian = userRepository.findById(veterinarianId).orElseThrow(() -> new ResourceNotFoundException("User", "id", reviewerId.toString()));
        User patient = userRepository.findById(reviewerId).orElseThrow(() -> new ResourceNotFoundException("User", "id", reviewerId.toString()));

        Review savedReview = new Review();
        savedReview.setVeterinarian(veterinarian);
        savedReview.setPatient(patient);
        savedReview.setStarsRate(review.getStarsRate());
        savedReview.setFeedback(review.getFeedback());


        return reviewRepository.save(savedReview);
    }

    @Transactional
    @Override
    public double getAverageRattingForVet(Long veterinarianId) {
        List<Review> reviews = reviewRepository.findByVeterinarianId(veterinarianId);

        if (reviews.isEmpty()) {
            return 0;
        }

        int totalStars = 0;
        for (Review review : reviews) {
            totalStars += review.getStarsRate();
        }

        return (double) totalStars / reviews.size();

    }

    @Override
    public Review updateReview(
            Long reviewerId, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(reviewerId).orElseThrow(() -> new ResourceNotFoundException("Review", "ID", reviewerId.toString()));
        review.setStarsRate(request.getStars());
        review.setFeedback(request.getFeedback());
        reviewRepository.save(review);
        return review;

    }

    @Override
    public Page<Review> findAllReviewsByUserId(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return reviewRepository.findAllByUserId(userId, pageRequest);
    }

    @Override
    public void deleteReview(Long reviewId) {
        // Tìm review theo reviewId
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);

        // Kiểm tra nếu review có tồn tại
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();

            // Loại bỏ mối quan hệ với bác sĩ thú y và bệnh nhân
            review.removeRelationShip();

            // Xóa review khỏi cơ sở dữ liệu
            reviewRepository.deleteById(reviewId);
        } else {
            // Nếu không tìm thấy review, ném ngoại lệ ResourceNotFoundException
            throw new ResourceNotFoundException("Review", "id", reviewId.toString());
        }
    }
    @Override
    public List<Review> findAllByUserId(Long userId) {
        return reviewRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteAll(List<Review> reviews) {
        reviewRepository.deleteAll(reviews);
    }
}
