package com.duongw.universalpetcare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@PrimaryKeyJoinColumn(name = "veterinarian_id")
public class Veterinarian extends User {

    private Long id;
    private String specialization;


}
