package tn.esprit.autoloc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    private StatutReservation statut;

    @ManyToOne
    @JsonIgnoreProperties("reservations")
    private Client client;

    @ManyToOne
    @JsonIgnoreProperties("reservations")
    private Vehicule vehicule;

    @OneToOne
    @JsonIgnoreProperties("reservation")
    private Contrat contrat;

    // Helper getter/setter compatible avec les signatures standard getId() / setId()
    public Long getId() {
        return this.idReservation;
    }

    public void setId(Long id) {
        this.idReservation = id;
    }

    // Accepte un String ou un Enum pour statut
    public void setStatut(String statutStr) {
        if (statutStr != null) {
            this.statut = StatutReservation.valueOf(statutStr);
        }
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    // Extensions du Builder généré par Lombok pour supporter statut(String) et id(Long)
    public static class ReservationBuilder {
        public ReservationBuilder statut(String statutStr) {
            this.statut = (statutStr != null) ? StatutReservation.valueOf(statutStr) : null;
            return this;
        }

        public ReservationBuilder statut(StatutReservation statut) {
            this.statut = statut;
            return this;
        }

        public ReservationBuilder id(Long id) {
            this.idReservation = id;
            return this;
        }
    }
}
