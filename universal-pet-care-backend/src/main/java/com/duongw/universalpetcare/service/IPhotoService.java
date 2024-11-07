package com.duongw.universalpetcare.service;

import com.duongw.universalpetcare.model.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public interface IPhotoService {
    Photo savePhoto(MultipartFile photo, Long userId);

    Photo getPhotoById(Long id);

    void deletePhoto(Long id, Long userId) throws SQLException;

    Photo updatePhoto(Long id, MultipartFile file) throws SQLException, IOException;

    byte[] getImageData(Long id) throws SQLException;


}
