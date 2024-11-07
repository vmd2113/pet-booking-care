package com.duongw.universalpetcare.repository;

import com.duongw.universalpetcare.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("SELECT DISTINCT p.type FROM Pet p")
    List<String> getDistinctByType();

    @Query("SELECT DISTINCT p.color FROM Pet p")
    List<String> getDistinctByColor();

    @Query("SELECT DISTINCT p.breed FROM Pet p WHERE p.type = :petType")
    List<String> getDistinctByBreed(String petType);

}
