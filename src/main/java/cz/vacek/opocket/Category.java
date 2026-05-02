package cz.vacek.opocket;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "categories")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double length;
    private int climbing;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event; //ke kteremu zavodu ta kategorie patri
}
