package com.ecom.userService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable=false)
    private String email;
    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String firstName;
    @Column(nullable=false)
    private String lastName;


    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable=false)
    private String phone;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable=false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate(){
        this.createdAt=LocalDateTime.now();
    }

    @OneToMany(mappedBy = "user",cascade=CascadeType.ALL,orphanRemoval = true)
    private List<Address> addresses=new ArrayList<>();
}
