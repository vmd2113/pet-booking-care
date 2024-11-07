/* eslint-disable no-useless-catch */
import { api } from "../utils/api";

export async function getAllPet() {
    try {
        const result = await api.get("/pets/");
        return result.data;
    } catch (error) {
        throw error;
    }
}

export async function getAllPetTypes() {
    try {
        const result = await api.get("/pets/types");
        return result.data;
    } catch (error) {
        throw error;
    }
}

export async function getAllPetColors() {
    try {
        const result = await api.get("/pets/colors");
        return result.data;
    } catch (error) {
        throw error;
    }
}

export async function getAllPetBreeds(petType) {
    try {
        const result = await api.get(`/pets/breeds?petType=${petType}`);
        return result.data;
    } catch (error) {
        throw error;
    }
}

export const updatePet = async (petId, updatedPet) => {
    try {
        const response = await api.put(`/pets/update/${petId}`, updatedPet);
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const deletePet = async (id) => {
    try {
        const response = await api.delete(`/pets/${id}`);
        return response.data;
    } catch (error) {
        throw error;
    }
};
