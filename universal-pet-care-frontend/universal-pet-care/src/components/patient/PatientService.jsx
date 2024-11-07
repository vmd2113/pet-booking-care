/* eslint-disable no-useless-catch */
import { api } from "../utils/api";
export async function getPatients() {
    try {
        const result = await api.get("/patients/all-patients");
        return result.data;
    } catch (error) {
        throw error;
    }
}
export async function getPatientById(patientId) {
    try {
        const result = await api.get(`/patients/by-id/${patientId}`);
        return result.data;
    } catch (error) {
        throw error;
    }
}
