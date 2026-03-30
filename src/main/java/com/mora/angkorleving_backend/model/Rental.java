package com.mora.angkorleving_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "rentals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable=false)
    private User tenant;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable=false)
    private Room room;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(nullable=false)
    private String status; // ACTIVE, ENDED, PENDING

    private Double monthlyRent;

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    private List<Payment> payments;
}
