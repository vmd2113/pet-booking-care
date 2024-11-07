/* eslint-disable no-useless-catch */
import { api } from "../utils/api";

// verify email
export const verifyEmail = async (token) => {
    try {
        const response = await api.get(`auth/verify-your-email?token=${token}`);
        return response.data;
    } catch (error) {
        throw error;
    }
};

// resend verification token
export async function resendVerificationToken(oldToken) {
    try {
        const response = await api.put(
            `/auth/resend-verification-token?token=${oldToken}`
        );
        return response.data;
    } catch (error) {
        throw error;
    }
}
// request password reset
export async function requestPasswordReset(email) {
    try {
        const response = await api.post("/auth/request-password-reset", {
            email,
        });
        return response.data;
    } catch (error) {
        throw error;
    }
}

/* This function validates a given token */
export async function validateToken(token) {
    try {
        const result = await api.get(
            `/verification/check-token-expiration?token=${token}`
        );
        return result.data;
    } catch (error) {
        throw error;
    }
}

export async function resetPassword(token, newPassword) {
    try {
        const response = await api.post("/auth/reset-password", {
            token,
            newPassword,
        });
        return response.data;
    } catch (error) {
        throw error;
    }
}

export const loginUser = async (email, password) => {
    try {
        const response = await api.post("/auth/login", { email, password });
        return response.data.data;
    } catch (error) {
        throw error;
    }
};

export const logout = () => {
    localStorage.removeItem("authToken");
    localStorage.removeItem("userId");
    localStorage.removeItem("userRoles");
    window.location.href = "/";
};
