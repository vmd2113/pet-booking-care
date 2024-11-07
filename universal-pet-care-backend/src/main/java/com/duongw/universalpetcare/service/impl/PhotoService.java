package com.duongw.universalpetcare.service.impl;

import com.duongw.universalpetcare.exception.ResourceNotFoundException;
import com.duongw.universalpetcare.model.Photo;
import com.duongw.universalpetcare.model.User;
import com.duongw.universalpetcare.repository.PhotoRepository;
import com.duongw.universalpetcare.repository.UserRepository;
import com.duongw.universalpetcare.service.IPhotoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoService implements IPhotoService {

    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;

    @Override
    public Photo savePhoto(MultipartFile photo, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Photo photo1 = new Photo();
        if (photo != null && !photo.isEmpty()) {
            try {
                byte[] photoBytes = photo.getBytes();
                Blob photoBlob = new SerialBlob(photoBytes);
                photo1.setImage(photoBlob);
                photo1.setFileName(photo.getContentType());

            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
            Photo savePhoto = photoRepository.save(photo1);
            savePhoto.setFileName(photo.getOriginalFilename());
            savePhoto.setFileType(photo.getContentType());
            user.ifPresent(user_ -> user_.setPhoto(savePhoto));
            userRepository.save(user.get());
            return savePhoto;

        } else {
            return null;
        }

    }

    @Override
    public Photo getPhotoById(Long id) {
        return photoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Photo", "id", id.toString()));
    }

    @Transactional
    @Override
    public void deletePhoto(Long id, Long userId) {
        userRepository.findById(userId).ifPresentOrElse(User::removeUserPhoto, () -> {
            throw new ResourceNotFoundException("Photo", "id", id.toString());
        });
        photoRepository.findById(id)
                .ifPresentOrElse(photoRepository::delete, () -> {
                    throw new ResourceNotFoundException("Photo", "id", id.toString());
                });

    }

    @Override
    public Photo updatePhoto(Long id, MultipartFile file) throws SQLException, IOException {
        Photo photo = getPhotoById(id);
        if (photo != null) {
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            photo.setImage(photoBlob);
            photo.setFileType(file.getContentType());
            photo.setFileName(file.getOriginalFilename());
            return photoRepository.save(photo);
        }
        throw new ResourceNotFoundException("Photo", "id", id.toString());
    }
    @Override
    public byte[] getImageData(Long id) throws SQLException {
        Photo photo = getPhotoById(id);
        if (photo != null) {
            Blob photoBlob = photo.getImage();
            int blobLength = (int) photoBlob.length();
            return photoBlob.getBytes(1, blobLength);
        }
        return null;
    }
}
