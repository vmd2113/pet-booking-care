import { api } from "../utils/api";

export async function getVeterinarians() {
    // eslint-disable-next-line no-useless-catch
    try {
        const result = await api.get("/veterinarians/get-all-veterinarians");
        console.log("The result ", result);
        console.log("The result ", result.data);
        return result.data;
    } catch (error) {
        throw error;
    }
}

export async function findAvailableVeterinarians(searchParams) {
    // eslint-disable-next-line no-useless-catch
    try {
        const queryParams = new URLSearchParams(searchParams);

        const result = await api.get(
            `/veterinarians/search-veterinarians?${queryParams}`
        );
        return result.data;
    } catch (error) {
        throw error;
    }
}

export const getAllSpecializations = async () => {
    // eslint-disable-next-line no-useless-catch
    try {
        const response = await api.get("/veterinarians/all-specializations");
        return response.data;
    } catch (error) {
        throw error;
    }
};
