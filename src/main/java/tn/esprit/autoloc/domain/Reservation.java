package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    private StatutReservation statut;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Vehicule vehicule;

    @OneToOne
    private Contrat contrat;
}