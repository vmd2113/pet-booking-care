/* eslint-disable no-useless-catch */
import { api } from "../utils/api";

const token = localStorage.getItem("authToken");

export async function bookAppointment(
    senderId,
    recipientId,
    appointmentRequest
) {
    try {
        const token = localStorage.getItem("authToken");
        const result = await api.post(
            `/appointments/book-appointment?senderId=${senderId}&recipientId=${recipientId}`,
            appointmentRequest,
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            }
        );
        console.log("The result from here :", result);
        return result.data;
    } catch (error) {
        throw error;
    }
}

export const updateAppointment = async (appointmentId, appointmentData) => {
    try {
        const response = await api.put(
            `appointments/update/${appointmentId}`,
            appointmentData
        );
        console.log("Two :", response.data.message);
        return response;
    } catch (error) {
        throw error;
    }
};

export async function cancelAppointment(appointmentId) {
    try {
        const response = await api.put(
            `/appointments/cancel-appointment/${appointmentId}`,
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                    Accept: "application/json", 
                },
            }
        );
        return response.data;
    } catch (error) {
        throw error;
    }
}

export async function approveAppointment(appointmentId) {
    try {
        const response = await api.put(
            `/appointments/approve-appointment/${appointmentId}`
        );
        return response.data;
    } catch (error) {
        throw error;
    }
}

export async function declineAppointment(appointmentId) {
    try {
        const response = await api.put(
            `/appointments/decline-appointment/${appointmentId}`
        );
        return response.data;
    } catch (error) {
        throw error;
    }
}

export const getAppointmentById = async (appointmentId) => {
    try {
        const result = await api.get(`/appointments/by-id/${appointmentId}`);
        return result.data;
    } catch (error) {
        throw error;
    }
};

export async function countAppointments() {
    try {
        const result = await api.get("/appointments/count-appointments");
        console.log("The result ", result.data);
        return result.data;
    } catch (error) {
        throw error;
    }
}

export const getAppointmentsSummary = async () => {
    try {
        const response = await api.get(
            "/appointments/summary/appointments-summary"
        );
        return response.data;
    } catch (error) {
        throw error;
    }
};
