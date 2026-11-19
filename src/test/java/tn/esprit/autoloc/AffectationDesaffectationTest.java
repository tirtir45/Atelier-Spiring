package tn.esprit.autoloc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.RoleEmploye;
import tn.esprit.autoloc.domain.enums.StatutVehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IEmployeService;
import tn.esprit.autoloc.service.IVehiculeService;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class AffectationDesaffectationTest {

    @Autowired
    private IVehiculeService vehiculeService;

    @Autowired
    private IEmployeService employeService;

    @Autowired
    private IAgenceRepository agenceRepository;

    @Autowired
    private IVehiculeRepository vehiculeRepository;

    @Autowired
    private IEmployeRepository employeRepository;

    @Test
    @DisplayName("Partie 1.5 : Affecter un véhicule à une agence existante")
    void testAffecterVehiculeAAgence() {
        Agence agence = agenceRepository.save(Agence.builder().nom("Agence Tunis").ville("Tunis").build());
        Vehicule vehicule = vehiculeRepository.save(Vehicule.builder()
                .immatriculation("123-TUN-456")
                .marque("Renault")
                .modele("Clio")
                .categorie(CategorieVehicule.CITADINE)
                .tarifJournalier(new BigDecimal("90.00"))
                .statut(StatutVehicule.DISPONIBLE)
                .build());

        vehiculeService.affecterVehiculeAAgence(vehicule.getIdVehicule(), agence.getIdAgence());

        Vehicule updated = vehiculeRepository.findById(vehicule.getIdVehicule()).orElseThrow();
        assertThat(updated.getAgence()).isNotNull();
        assertThat(updated.getAgence().getIdAgence()).isEqualTo(agence.getIdAgence());
    }

    @Test
    @DisplayName("Partie 1.5 : Affecter avec identifiant inexistant -> Exception")
    void testAffectationInexistante() {
        Agence agence = agenceRepository.save(Agence.builder().nom("Agence Sousse").ville("Sousse").build());
        Long idAgence = agence.getIdAgence();

        // Véhicule inexistant
        assertThatThrownBy(() -> vehiculeService.affecterVehiculeAAgence(99999L, idAgence))
                .isInstanceOf(ResourceNotFoundException.class);

        // Agence inexistante
        Vehicule vehicule = vehiculeRepository.save(Vehicule.builder()
                .immatriculation("789-TUN-101")
                .marque("Peugeot")
                .modele("208")
                .categorie(CategorieVehicule.CITADINE)
                .tarifJournalier(new BigDecimal("100.00"))
                .build());
        Long idVehicule = vehicule.getIdVehicule();

        assertThatThrownBy(() -> vehiculeService.affecterVehiculeAAgence(idVehicule, 99999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Partie 1.5 : Transférer un véhicule d'une agence vers une autre")
    void testTransfertAgence() {
        Agence agence1 = agenceRepository.save(Agence.builder().nom("Agence 1").ville("Tunis").build());
        Agence agence2 = agenceRepository.save(Agence.builder().nom("Agence 2").ville("Sfax").build());

        Vehicule vehicule = vehiculeRepository.save(Vehicule.builder()
                .immatriculation("999-TUN-999")
                .marque("Volkswagen")
                .modele("Golf")
                .categorie(CategorieVehicule.COMPACTE)
                .tarifJournalier(new BigDecimal("120.00"))
                .agence(agence1)
                .build());

        // Transfert vers agence 2
        vehiculeService.affecterVehiculeAAgence(vehicule.getIdVehicule(), agence2.getIdAgence());

        Vehicule updated = vehiculeRepository.findById(vehicule.getIdVehicule()).orElseThrow();
        assertThat(updated.getAgence().getIdAgence()).isEqualTo(agence2.getIdAgence());
    }

    @Test
    @DisplayName("Partie 1.5 : Désaffecter un véhicule d'une agence")
    void testDesaffecterVehicule() {
        Agence agence = agenceRepository.save(Agence.builder().nom("Agence Nabeul").ville("Nabeul").build());
        Vehicule vehicule = vehiculeRepository.save(Vehicule.builder()
                .immatriculation("111-TUN-222")
                .marque("Toyota")
                .modele("Yaris")
                .categorie(CategorieVehicule.CITADINE)
                .tarifJournalier(new BigDecimal("95.00"))
                .agence(agence)
                .build());

        vehiculeService.desaffecterVehiculeDeAgence(vehicule.getIdVehicule());

        Vehicule updated = vehiculeRepository.findById(vehicule.getIdVehicule()).orElseThrow();
        assertThat(updated.getAgence()).isNull();
    }

    @Test
    @DisplayName("Partie 1.5 : Affecter et désaffecter un employé")
    void testAffecterEtDesaffecterEmploye() {
        Agence agence = agenceRepository.save(Agence.builder().nom("Agence Bizerte").ville("Bizerte").build());
        Employe employe = employeRepository.save(Employe.builder()
                .nom("Gharbi")
                .prenom("Sami")
                .role(RoleEmploye.AGENT)
                .build());

        // Affecter
        employeService.affecterEmployeAAgence(employe.getIdEmploye(), agence.getIdAgence());
        Employe updated = employeRepository.findById(employe.getIdEmploye()).orElseThrow();
        assertThat(updated.getAgence()).isNotNull();
        assertThat(updated.getAgence().getIdAgence()).isEqualTo(agence.getIdAgence());

        // Désaffecter
        employeService.desaffecterEmployeDeAgence(employe.getIdEmploye());
        Employe desaffecte = employeRepository.findById(employe.getIdEmploye()).orElseThrow();
        assertThat(desaffecte.getAgence()).isNull();
    }
}