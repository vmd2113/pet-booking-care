package com.duongw.universalpetcare.controller;

import com.duongw.universalpetcare.dto.request.ReviewUpdateRequest;
import com.duongw.universalpetcare.dto.response.ApiResponse;
import com.duongw.universalpetcare.dto.response.ReviewDTO;
import com.duongw.universalpetcare.exception.AlreadyExistsException;
import com.duongw.universalpetcare.exception.ResourceNotFoundException;
import com.duongw.universalpetcare.model.Review;
import com.duongw.universalpetcare.service.IReviewService;
import com.duongw.universalpetcare.utils.UrlMapping;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping(UrlMapping.REVIEW_URL)
@RequiredArgsConstructor

@Tag(name = "review controller")
@CrossOrigin("http://localhost:5173")
public class ReviewController {

    private final IReviewService reviewService;
    private final ModelMapper modelMapper;

    @PostMapping("/submit-review")
    public ResponseEntity<ApiResponse> saveReview(@RequestBody Review review, @RequestParam Long reviewerId, @RequestParam Long vetId) {
        try {
            Review savedReview = reviewService.saveReview(review, reviewerId, vetId);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Success", savedReview.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(BAD_REQUEST.value(), e.getMessage()));

        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(CONFLICT.value(), e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(NOT_FOUND
                    .value(), e.getMessage()));
        }
    }


    @PutMapping(path = "/update/{id}")
    public ResponseEntity<ApiResponse> updateReview(@RequestBody ReviewUpdateRequest updateRequest,
                                                    @PathVariable(name = "id") Long reviewId) {
        try {
            Review updatedReview = reviewService.updateReview(reviewId, updateRequest);
            return ResponseEntity.ok(new ApiResponse(OK.value(), "updated success", updatedReview.getId()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(NOT_FOUND.value(), e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable(name = "id") Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok(new ApiResponse(OK.value(), "delete success"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(NOT_FOUND.value(), e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getReviewsByUserID(@PathVariable(name = "id") Long userId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size) {
        Page<Review> reviewPage = reviewService.findAllReviewsByUserId(userId, page, size);
        Page<ReviewDTO> reviewDTOs = reviewPage.map((element) -> modelMapper.map(element, ReviewDTO.class));
        return ResponseEntity.status(FOUND).body(new ApiResponse(OK.value(), "success", reviewDTOs));
    }

    @GetMapping("/rating/{id}")
    public ResponseEntity<ApiResponse> getAverageRatingForVet(@PathVariable(name = "id") Long vetId) {
        double averageRating = reviewService.getAverageRattingForVet(vetId);
        return ResponseEntity.ok(new ApiResponse(OK.value(), "success", averageRating));
    }


}
