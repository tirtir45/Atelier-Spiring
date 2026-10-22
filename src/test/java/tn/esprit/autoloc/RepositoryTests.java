package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.domain.enums.*;
import tn.esprit.autoloc.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RepositoryTests {

    @Autowired private IAgenceRepository agenceRepository;
    @Autowired private IClientRepository clientRepository;
    @Autowired private IVehiculeRepository vehiculeRepository;
    @Autowired private IEmployeRepository employeRepository;
    @Autowired private IEquipementRepository equipementRepository;
    @Autowired private IMaintenanceRepository maintenanceRepository;

    private void log(String message) {
        System.out.println("=== " + message);
    }

    // ---------- Partie 2 : Agence (CRUD complet) ----------
    @Test
    void testAgenceRepository() {
        log("AGENCE : début du test");
        long countBefore = agenceRepository.count();
        log("count avant = " + countBefore);

        Agence agence = new Agence();
        agence.setNom("Agence Tunis Centre");
        agence.setVille("Tunis");
        agence.setAdresse("Avenue Habib Bourguiba");
        agence.setTelephone("71000000");

        Agence saved = agenceRepository.save(agence);
        assertNotNull(saved.getIdAgence());
        log("save -> id=" + saved.getIdAgence() + ", nom=" + saved.getNom());

        log("findAll :");
        agenceRepository.findAll().forEach(a ->
                System.out.println("    " + a.getIdAgence() + " | " + a.getNom() + " | " + a.getVille()));

        Agence found = agenceRepository.findById(saved.getIdAgence()).orElseThrow();
        assertEquals("Agence Tunis Centre", found.getNom());
        log("findById(" + saved.getIdAgence() + ") -> " + found.getNom());

        boolean existe = agenceRepository.existsById(saved.getIdAgence());
        assertTrue(existe);
        log("existsById(" + saved.getIdAgence() + ") -> " + existe);

        assertEquals(countBefore + 1, agenceRepository.count());
        log("count après save = " + agenceRepository.count());

        /*agenceRepository.deleteById(saved.getIdAgence());
        boolean existeApres = agenceRepository.existsById(saved.getIdAgence());
        assertFalse(existeApres);
        assertTrue(agenceRepository.findById(saved.getIdAgence()).isEmpty());
        log("deleteById(" + saved.getIdAgence() + ") puis existsById -> " + existeApres);
        log("AGENCE : OK");*/
    }


    // ---------- Partie 3 : Client ----------
    @Test
    void testClientRepository() {
        log("CLIENT : début du test");
        long countBefore = clientRepository.count();
        log("count avant = " + countBefore);

        Client c1 = new Client();
        c1.setNom("Ben Ali");
        c1.setPrenom("Sami");
        c1.setEmail("sami.benali@mail.com");
        c1.setTelephone("20111222");
        c1.setNumPermis("P-1001");
        c1.setDateInscription(LocalDate.now());

        Client c2 = new Client();
        c2.setNom("Trabelsi");
        c2.setPrenom("Leila");
        c2.setEmail("leila.trabelsi@mail.com");
        c2.setTelephone("20333444");
        c2.setNumPermis("P-1002");
        c2.setDateInscription(LocalDate.now());

        Client s1 = clientRepository.save(c1);
        Client s2 = clientRepository.save(c2);
        assertNotNull(s1.getIdClient());
        assertNotNull(s2.getIdClient());
        log("save -> id=" + s1.getIdClient() + " (" + s1.getNom() + "), id="
                + s2.getIdClient() + " (" + s2.getNom() + ")");

        log("findAll :");
        clientRepository.findAll().forEach(c ->
                System.out.println("    " + c.getIdClient() + " | " + c.getNom() + " " + c.getPrenom()));

        Client found = clientRepository.findById(s1.getIdClient()).orElseThrow();
        assertEquals("Ben Ali", found.getNom());
        log("findById(" + s1.getIdClient() + ") -> " + found.getNom() + " " + found.getPrenom());

        boolean existe = clientRepository.existsById(s1.getIdClient());
        boolean inexistant = clientRepository.existsById(999_999L);
        assertTrue(existe);
        assertFalse(inexistant);
        log("existsById(" + s1.getIdClient() + ") -> " + existe);
        log("existsById(999999) -> " + inexistant);

        assertEquals(countBefore + 2, clientRepository.count());
        log("count après save = " + clientRepository.count());
        log("CLIENT : OK (les 2 clients restent en base pour vérification dans phpMyAdmin)");
    }

    // ---------- Partie 4 : Vehicule (save, update, delete) ----------
    @Test
    void testVehiculeRepository() {
        log("VEHICULE : début du test");

        Vehicule v = new Vehicule();
        v.setImmatriculation("123 TUN 4567");
        v.setMarque("Renault");
        v.setModele("Clio");
        v.setCategorie(CategorieVehicule.CITADINE);
        v.setTarifJournalier(new BigDecimal("80.00"));
        v.setStatut(StatutVehicule.DISPONIBLE);

        Vehicule saved = vehiculeRepository.save(v);
        assertNotNull(saved.getIdVehicule());
        log("save -> id=" + saved.getIdVehicule() + ", statut=" + saved.getStatut());

        log("findAll :");
        vehiculeRepository.findAll().forEach(x ->
                System.out.println("    " + x.getIdVehicule() + " | " + x.getMarque() + " "
                        + x.getModele() + " | " + x.getStatut()));
        log("count = " + vehiculeRepository.count());

        saved.setStatut(StatutVehicule.LOUE);
        Vehicule updated = vehiculeRepository.save(saved);
        assertEquals(StatutVehicule.LOUE, updated.getStatut());
        assertEquals(StatutVehicule.LOUE,
                vehiculeRepository.findById(saved.getIdVehicule()).orElseThrow().getStatut());
        log("update -> statut DISPONIBLE devient " + updated.getStatut());

        /*vehiculeRepository.deleteById(saved.getIdVehicule());
        boolean existeApres = vehiculeRepository.existsById(saved.getIdVehicule());
        assertFalse(existeApres);
        log("deleteById(" + saved.getIdVehicule() + ") puis existsById -> " + existeApres);
        log("VEHICULE : OK");*/
    }

    // ---------- Partie 5 : exercice autonome ----------
    @Test
    void testEmployeRepository() {
        log("EMPLOYE : début du test");

        Employe e = new Employe();
        e.setNom("Gharbi");
        e.setPrenom("Omar");
        e.setRole(RoleEmploye.AGENT);

        Employe saved = employeRepository.save(e);
        assertNotNull(saved.getIdEmploye());
        log("save -> id=" + saved.getIdEmploye() + ", role=" + saved.getRole());

        log("findAll :");
        employeRepository.findAll().forEach(x ->
                System.out.println("    " + x.getIdEmploye() + " | " + x.getNom() + " " + x.getPrenom()
                        + " | " + x.getRole()));

        boolean existe = employeRepository.existsById(saved.getIdEmploye());
        assertTrue(existe);
        log("existsById(" + saved.getIdEmploye() + ") -> " + existe);

        employeRepository.deleteById(saved.getIdEmploye());
        boolean existeApres = employeRepository.existsById(saved.getIdEmploye());
        assertFalse(existeApres);
        log("deleteById puis existsById -> " + existeApres);
        log("EMPLOYE : OK");
    }

    @Test
    void testEquipementRepository() {
        log("EQUIPEMENT : début du test");

        Equipement eq = new Equipement();
        eq.setLibelle("GPS");

        Equipement saved = equipementRepository.save(eq);
        assertNotNull(saved.getIdEquipement());
        log("save -> id=" + saved.getIdEquipement() + ", libelle=" + saved.getLibelle());

        Equipement found = equipementRepository.findById(saved.getIdEquipement()).orElseThrow();
        assertEquals("GPS", found.getLibelle());
        log("findById(" + saved.getIdEquipement() + ") -> " + found.getLibelle());

        boolean existe = equipementRepository.existsById(saved.getIdEquipement());
        assertTrue(existe);
        log("existsById -> " + existe);

        equipementRepository.deleteById(saved.getIdEquipement());
        boolean existeApres = equipementRepository.existsById(saved.getIdEquipement());
        assertFalse(existeApres);
        log("deleteById puis existsById -> " + existeApres);
        log("EQUIPEMENT : OK");
    }

    @Test
    void testMaintenanceRepository() {
        log("MAINTENANCE : début du test");

        Maintenance m = new Maintenance();
        m.setDateDebut(LocalDate.now());
        m.setDateFin(LocalDate.now().plusDays(2));
        m.setDescription("Vidange et contrôle des freins");

        Maintenance saved = maintenanceRepository.save(m);
        assertNotNull(saved.getIdMaintenance());
        log("save -> id=" + saved.getIdMaintenance());

        log("findAll :");
        maintenanceRepository.findAll().forEach(x ->
                System.out.println("    " + x.getIdMaintenance() + " | " + x.getDescription()));

        boolean existe = maintenanceRepository.existsById(saved.getIdMaintenance());
        assertTrue(existe);
        log("existsById -> " + existe);

        maintenanceRepository.deleteById(saved.getIdMaintenance());
        boolean existeApres = maintenanceRepository.existsById(saved.getIdMaintenance());
        assertFalse(existeApres);
        log("deleteById puis existsById -> " + existeApres);
        log("MAINTENANCE : OK");
    }


}