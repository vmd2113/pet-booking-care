package com.duongw.universalpetcare.service;

import com.duongw.universalpetcare.model.Pet;

import java.util.List;

public interface IPetService {
    List<Pet> savePetForAppointment(List<Pet> pets);
    Pet updatePet(Pet pet, Long id);
    void deletePet(Long id);
    Pet getPetById(Long id);

    List<String> getPetTypes();

    List<String> getPetColor();

    List<String> getPetBreed(String petType);
}
