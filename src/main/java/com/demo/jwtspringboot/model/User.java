package com.demo.jwtspringboot.model;

import lombok.Setter;
import lombok.Getter;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users") // Mengubah nama tabel menjadi "users"
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;
}
