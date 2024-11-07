package com.duongw.universalpetcare.controller;

import com.duongw.universalpetcare.dto.response.ApiResponse;
import com.duongw.universalpetcare.exception.ResourceNotFoundException;
import com.duongw.universalpetcare.model.Photo;
import com.duongw.universalpetcare.service.IPhotoService;
import com.duongw.universalpetcare.utils.UrlMapping;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping(UrlMapping.PHOTO_URL)
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")


@Tag(name = "photo controller")

public class PhotoController {

    private final  IPhotoService photoService;

    @PostMapping(path = "/upload-photo")
    public ResponseEntity<ApiResponse> savePhoto(
            @RequestParam(name = "file") MultipartFile file,
            @RequestParam(name = "userId") Long userId
    ) {
        try {
            Photo photo = photoService.savePhoto(file, userId);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Upload success"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }



    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiResponse> getPhotoById(@PathVariable(name = "id") Long photoId) {
        try {
            Photo photo = photoService.getPhotoById(photoId);
            if (photo != null) {
                byte[] photoBytes = photoService.getImageData(photo.getId());
                return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Get success", photoBytes));
            }
        } catch (ResourceNotFoundException | SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error"));
    }

    @DeleteMapping(value = "/{id}/user/{userId}")
    public ResponseEntity<ApiResponse> deletePhoto(@PathVariable(name = "id") Long photoId, @PathVariable(name = "userId") Long userId) {
        try {
            Photo photo = photoService.getPhotoById(photoId);
            if (photo != null) {
                photoService.deletePhoto(photo.getId(), userId);
                return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "delete success", photo.getId()));
            }
        } catch (ResourceNotFoundException | SQLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "something  was wrong"));
    }

    @PutMapping(value = "update/{id}")
    public ResponseEntity<ApiResponse> updatePhoto(@PathVariable(name = "id") Long photoId, @RequestBody MultipartFile file) throws SQLException {
        try {
            Photo photo = photoService.updatePhoto(photoId, file);
            if (photo != null) {
                Photo updatedPhoto = photoService.updatePhoto(photo.getId(), file);
                return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "update success", updatedPhoto.getId()));
            }
        } catch (ResourceNotFoundException | IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NO_CONTENT.value(), e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "something was wrong"));

    }


}
