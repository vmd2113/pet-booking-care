/* eslint-disable no-useless-catch */
import { api } from "../utils/api";

//TODO: change URL to the actual URL
export async function getUserById(userId) {
    try {
        const result = await api.get(`/users/${userId}`);
        return result.data;
    } catch (error) {
        throw error;
    }
}

export async function registerUser(user) {
    try {
        const result = await api.post("/users/register", user);
        return result.data;
    } catch (error) {
        throw error;
    }
}

export async function changeUserPassword(
    userId,
    currentPassword,
    newPassword,
    confirmPassword
) {
    try {
        const requestData = {
            currentPassword,
            newPassword,
            confirmPassword,
        };
        const result = await api.put(
            `/users/change-password/${userId}`,
            requestData
        );
        return result.data;
    } catch (error) {
        throw error;
    }
}
export async function updateUser(userData, userId) {
    try {
        const response = await api.put(`/users/update/${userId}`, userData);
        return response.data;
    } catch (error) {
        throw error;
    }
}

export async function deleteUser(userId) {
    try {
        const response = await api.delete(`/users/delete/${userId}`);
        return response.data;
    } catch (error) {
        throw error;
    }
}

export async function countVeterinarians() {
    try {
        const result = await api.get("/users/count-veterinarians");
        return result.data;
    } catch (error) {
        throw error;
    }
}

export async function countPatients() {
    try {
        const result = await api.get("/users/count-patients");
        console.log("Patients :", result.data);
        return result.data;
    } catch (error) {
        throw error;
    }
}

export async function countUsers() {
    try {
        const result = await api.get("/users/count-users");
        return result.data;
    } catch (error) {
        throw error;
    }
}

export const getAggregateUsersByMonthAndType = async () => {
    try {
        const response = await api.get("/users/aggregated-users");
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const getAggregatedUsersAccountByActiveStatus = async () => {
    try {
        const response = await api.get("/users/account/aggregated-by-status");
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const aggregateVetBySpecialization = async () => {
    try {
        const response = await api.get("/veterinarians/get-by-specialization");
        return response.data;
    } catch (error) {
        throw error;
    }
};

export async function lockUserAccount(userId) {
    try {
        const result = await api.put(
            `/users/account/${userId}/lock-user-account`
        );
        return result.data;
    } catch (error) {
        throw error;
    }
}

export async function unlockUserAccount(userId) {
    try {
        const result = await api.put(
            `/users/account/${userId}/unlock-user-account`
        );
        return result.data;
    } catch (error) {
        throw error;
    }
}
