package cz.vacek.opocket;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String registrationNumber;

    @ManyToOne // FK
    @JoinColumn(name = "club_id")
    private Club club;
}