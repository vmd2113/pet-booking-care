package com.duongw.universalpetcare.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

/**@MappedSuperclass
 * @Inheritaince(strategy  = InheritanceType.SINGLE_TABLE) @DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.String:
 * @TablePerClass
 * @Joined
 * Purpose: annotation is used when you have a superclass that contains common properties that need to be share among multiple subclass, but the superclass itself is not a persistent entity (not a table) in the database */


@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    private String password;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Transient
    private String specialization;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDate createdAt;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles = new HashSet<>();


    @Transient
    List<Appointment> appointments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<VerificationToken> verificationTokens = new ArrayList<>();

    @Transient
    private List<Review> reviews = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Photo photo;

    public void removeUserPhoto() {
        if (this.getPhoto() != null) {
            this.setPhoto(null);
        }
    }


}
