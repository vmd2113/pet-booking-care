/* eslint-disable no-useless-catch */
import { api } from "../utils/api";

// updateUserPhoto
export async function updateUserPhoto(photoId, photoData) {
    try {
        const response = await api.put(`/photos/update/${photoId}`, photoData, {
            headers: {
                "Content-Type": "multipart/form-data",
            },
        });
        return response.data;
    } catch (error) {
        throw error;
    }
}
// uploadUserPhoto
export async function uploadUserPhoto(userId, photoData) {
    try {
        const formData = new FormData();
        formData.append("file", photoData); // Đặt key là "file" để khớp với backend
        formData.append("userId", userId); // Gửi userId như một phần của form data

        const response = await api.post("/photos/upload-photo", formData, {
            headers: {
                "Content-Type": "multipart/form-data",
            },
        });
        return response.data;
    } catch (error) {
        throw error;
    }
}

export async function deleteUserPhoto(photoId, userId) {
    try {
        const response = await api.delete(`/photos/${photoId}/user/${userId}`);
        return response.data;
    } catch (error) {
        throw error;
    }
}
