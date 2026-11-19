package tn.esprit.autoloc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.StatutVehicule;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehicule;

    private String immatriculation;
    private String marque;
    private String modele;

    @Enumerated(EnumType.STRING)
    private CategorieVehicule categorie;

    private BigDecimal tarifJournalier;

    @Enumerated(EnumType.STRING)
    private StatutVehicule statut;

    @ManyToOne
    @JsonIgnoreProperties({"vehicules", "employes"})
    private Agence agence;

    @OneToMany(mappedBy = "vehicule")
    private Set<Reservation> reservations;

    @OneToMany(mappedBy = "vehicule")
    @JsonIgnore
    private Set<Maintenance> maintenances;

    @ManyToMany
    @JsonIgnore
    private Set<Equipement> equipements;

    public Long getId() {
        return this.idVehicule;
    }

    public void setId(Long id) {
        this.idVehicule = id;
    }
}
