package tn.esprit.autoloc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "equipement")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipement;

    private String libelle;

    @ManyToMany(mappedBy = "equipements")
    @JsonIgnore
    private Set<Vehicule> vehicules;

    public Long getId() {
        return this.idEquipement;
    }

    public void setId(Long id) {
        this.idEquipement = id;
    }
}
