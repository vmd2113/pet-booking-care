package com.duongw.universalpetcare.service.impl;

import com.duongw.universalpetcare.exception.ResourceNotFoundException;
import com.duongw.universalpetcare.model.Pet;
import com.duongw.universalpetcare.repository.PetRepository;
import com.duongw.universalpetcare.service.IPetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService implements IPetService {

    private final PetRepository petRepository;

    @Override
    public List<Pet> savePetForAppointment(List<Pet> pets) {
        return petRepository.saveAll(pets);
    }

    @Override
    public Pet updatePet(Pet pet, Long id) {
        Pet existingPet = getPetById(id);
        existingPet.setName(pet.getName());
        existingPet.setAge(pet.getAge());
        existingPet.setColor(pet.getColor());
        existingPet.setType(pet.getType());
        existingPet.setBreed(pet.getBreed());
        existingPet.setAge(pet.getAge());
        return petRepository.save(existingPet);
    }

    @Override
    public void deletePet(Long id) {
        petRepository.findById(id)
                .ifPresentOrElse(petRepository::delete,
                        () -> {
                            throw new ResourceNotFoundException("Pet", "id", id.toString());
                        });

    }


    @Override
    public Pet getPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet", "id", id.toString()));
    }
    @Override
    public List<String> getPetTypes() {
        return petRepository.getDistinctByType();
    }

    @Override

    public List<String> getPetColor(){
        return petRepository.getDistinctByColor();
    }
    @Override
    public List<String> getPetBreed(String petType){
        return petRepository.getDistinctByBreed(petType);
    } 
}
