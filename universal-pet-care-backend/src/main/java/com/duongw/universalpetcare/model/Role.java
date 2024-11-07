package com.duongw.universalpetcare.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.Collection;
import java.util.HashSet;

@Table
@Entity
@Setter
@Getter
@NoArgsConstructor

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users = new HashSet<> ();

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name!= null  ? name: "";
    }




}
