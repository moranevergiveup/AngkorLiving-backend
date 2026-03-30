package com.mora.angkorleving_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNumber;

    @ManyToOne
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;   // link to Floor entity

    @Column(nullable = false)
    private String status; // AVAILABLE, RENTED

    private String imageUrl;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Rental> rentals;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Expense> expenses;

    // Derived price from floor
    public Double getPrice() {
        return floor != null ? floor.getPrice() : null;
    }
}
