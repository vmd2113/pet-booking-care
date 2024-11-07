package com.duongw.universalpetcare.service.impl;


import com.duongw.universalpetcare.dto.EntityConverter;
import com.duongw.universalpetcare.dto.request.RegistrationRequest;
import com.duongw.universalpetcare.dto.request.UserUpdateRequest;
import com.duongw.universalpetcare.dto.response.AppointmentDTO;
import com.duongw.universalpetcare.dto.response.ReviewDTO;
import com.duongw.universalpetcare.dto.response.UserDTO;
import com.duongw.universalpetcare.exception.ResourceNotFoundException;
import com.duongw.universalpetcare.factory.UserFactory;
import com.duongw.universalpetcare.model.Appointment;
import com.duongw.universalpetcare.model.Review;
import com.duongw.universalpetcare.model.User;
import com.duongw.universalpetcare.repository.UserRepository;
import com.duongw.universalpetcare.service.IAppointmentService;
import com.duongw.universalpetcare.service.IUserService;
import org.springframework.data.domain.Page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final EntityConverter<User, RegistrationRequest> entityConverter;
    private final EntityConverter<User, UserDTO> userDTOEntityConverter;
    private final IAppointmentService appointmentService;
    private final PhotoService photoService;
    private final ReviewService reviewService;

    @Override
    public User createUser(RegistrationRequest registrationRequest) {
        return userFactory.createUser(registrationRequest);

    }

    @Override
    public List<UserDTO> getAllUsers() {
       List<User> users = userRepository.findAll();
       return users.stream()
               .map(user -> userDTOEntityConverter.mapEntityToDTO(user, UserDTO.class))
               .collect(Collectors.toList());
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "Id", id.toString()));
    }



    @Override
    public User updateUser(Long id, UserUpdateRequest userUpdateRequest) {
        User user = getUserById(id);

        user.setFirstName(userUpdateRequest.getFirstName());
        user.setLastName(userUpdateRequest.getLastName());

        user.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        user.setGender(userUpdateRequest.getGender());
        user.setSpecialization(userUpdateRequest.getSpecialization());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userToDelete ->{
                    List<Review> reviews = new ArrayList<>(reviewService.findAllByUserId(userId));
                    reviewService.deleteAll(reviews);
                    List<Appointment> appointments = new ArrayList<>(appointmentService.findAllAppointmentsByUserId(userId));
                    appointmentService.deleteAll(appointments);
                    userRepository.deleteById(userId);

                }, () -> { throw new ResourceNotFoundException("User", "Id", userId.toString());});
    }


    @Override
    public UserDTO getUserWithDetails(Long userId) throws SQLException {
        //1. get the user
        User user = getUserById(userId);
        //2. convert the user to a userDto
        UserDTO userDto = userDTOEntityConverter.mapEntityToDTO(user, UserDTO.class);
        //3. get user appointments ( users ( patient and a vet))
        setUserAppointment(userDto);
        //.4 get users photo
        setUserPhoto(userDto, user);
        setUserReviews(userDto, userId);
        return  userDto;
    }

    private void setUserAppointment(UserDTO userDto) {
        List<AppointmentDTO> appointments = appointmentService.getUserAppointments(userDto.getId());
        userDto.setAppointments(appointments);
    }
    private void setUserPhoto(UserDTO userDto, User user) throws SQLException {
        if (user.getPhoto() != null) {
            userDto.setPhotoId(user.getPhoto().getId());
            userDto.setPhoto(photoService.getImageData(user.getPhoto().getId()));
        }
    }


    private void setUserReviews(UserDTO userDto, Long userId) {
        Page<Review> reviewPage = reviewService.findAllReviewsByUserId(userId, 0, Integer.MAX_VALUE);
        List<ReviewDTO> reviewDto = reviewPage.getContent()
                .stream()
                .map(this::mapReviewToDto).toList();
        if(!reviewDto.isEmpty()) {
            double averageRating = reviewService.getAverageRattingForVet(userId);
            userDto.setAverageRating(averageRating);
        }
        userDto.setReviews(reviewDto);
    }

    private ReviewDTO mapReviewToDto(Review review) {
        ReviewDTO reviewDto = new ReviewDTO();
        reviewDto.setId(review.getId());
        reviewDto.setStars(review.getStarsRate());
        reviewDto.setFeedback(review.getFeedback());
        mapVeterinarianInfo(reviewDto, review);
        mapPatientInfo(reviewDto, review);
        return reviewDto;
    }
    private void mapVeterinarianInfo(ReviewDTO reviewDto, Review review){
        if (review.getVeterinarian() != null) {
            reviewDto.setVeterinarianId(review.getVeterinarian().getId());
            reviewDto.setVeterinarianName(review.getVeterinarian().getFirstName()+ " " + review.getVeterinarian().getLastName());
            // set the photo
            setVeterinarianPhoto(reviewDto, review);
        }
    }

    private void mapPatientInfo(ReviewDTO reviewDto, Review review) {
        if (review.getPatient() != null) {
            reviewDto.setPatientId(review.getPatient().getId());
            reviewDto.setPatientName(review.getPatient().getFirstName()+ " " + review.getPatient().getLastName());
            // set the photo
            setReviewerPhoto(reviewDto, review);
        }
    }

    private void setReviewerPhoto(ReviewDTO reviewDto, Review review) {
        if(review.getPatient().getPhoto() != null){
            try {
                reviewDto.setPatientImage(photoService.getImageData(review.getPatient().getPhoto().getId()));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }else {
            reviewDto.setPatientImage(null);
        }
    }

    private void setVeterinarianPhoto(ReviewDTO reviewDto, Review review) {
        if(review.getVeterinarian().getPhoto() != null){
            try {
                reviewDto.setVeterinarianImage(photoService.getImageData(review.getVeterinarian().getPhoto().getId()));
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }else {
            reviewDto.setVeterinarianImage(null);
        }
    }

    @Service
    public static class ChangePasswordService {

    }

    @Override
    public long countUserByUserType(String userType) {
        return userRepository.countAllByUserType(userType);
    }

    @Override
    public long countVeterinarianByUserType() {
        return countUserByUserType("VET");

    }

    @Override
    public long countPatientByUserType() {
        return countUserByUserType("PATIENT");

    }



    @Override
    public long countAllUsers(){
        return userRepository.count();
    }


    @Override
    public Map<String, Map<String,Long>> aggregateUsersByMonthAndType(){
        List<User> users = userRepository.findAll();
        return users.stream().collect(Collectors.groupingBy(user -> Month.of(user.getCreatedAt().getMonthValue())
                        .getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                Collectors.groupingBy(User ::getUserType, Collectors.counting())));
    }

    @Override
    public Map<String, Map<String, Long>> aggregateUsersByEnabledStatusAndType(){
        List<User> users = userRepository.findAll();
        return users.stream()
                .collect(Collectors.groupingBy(user ->  user.isEnabled() ? "Enabled" : "Non-Enabled",
                        Collectors.groupingBy(User ::getUserType, Collectors.counting())));
    }



    public void lockUserAccount(Long userId){
        userRepository.updateUserEnabledStatus(userId, false);
    }

    public void unLockUserAccount(Long userId){
        userRepository.updateUserEnabledStatus(userId, true);
    }


}
