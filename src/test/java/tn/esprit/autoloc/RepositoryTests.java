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

    // ---------- Partie 2 : Agence ----------
    @Test
    void testAgenceRepository() {
        long countBefore = agenceRepository.count();

        Agence agence = new Agence();
        agence.setNom("Agence Tunis Centre");
        agence.setVille("Tunis");
        agence.setAdresse("Avenue Habib Bourguiba");
        agence.setTelephone("71000000");

        Agence saved = agenceRepository.save(agence);
        assertNotNull(saved.getIdAgence());
        System.out.println("Agence enregistrée : id=" + saved.getIdAgence() + ", nom=" + saved.getNom());

        agenceRepository.findAll().forEach(a ->
                System.out.println("findAll -> " + a.getIdAgence() + " | " + a.getNom() + " | " + a.getVille()));

        Agence found = agenceRepository.findById(saved.getIdAgence()).orElseThrow();
        System.out.println("findById -> " + found.getNom());

        assertTrue(agenceRepository.existsById(saved.getIdAgence()));
        assertEquals(countBefore + 1, agenceRepository.count());
        System.out.println("count -> " + agenceRepository.count());

        agenceRepository.deleteById(saved.getIdAgence());
        assertFalse(agenceRepository.existsById(saved.getIdAgence()));
        assertTrue(agenceRepository.findById(saved.getIdAgence()).isEmpty());
        System.out.println("Agence supprimée.");
    }

    // ---------- Partie 3 : Client ----------
    @Test
    void testClientRepository() {
        long countBefore = clientRepository.count();

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

        clientRepository.findAll().forEach(c ->
                System.out.println("findAll -> " + c.getIdClient() + " | " + c.getNom() + " " + c.getPrenom()));

        Client found = clientRepository.findById(s1.getIdClient()).orElseThrow();
        assertEquals("Ben Ali", found.getNom());

        assertTrue(clientRepository.existsById(s1.getIdClient()));
        assertFalse(clientRepository.existsById(999_999L));

        assertEquals(countBefore + 2, clientRepository.count());
        System.out.println("count clients -> " + clientRepository.count());
    }

    // ---------- Partie 4 : Vehicule ----------
    @Test
    void testVehiculeRepository() {
        Vehicule v = new Vehicule();
        v.setImmatriculation("123 TUN 4567");
        v.setMarque("Renault");
        v.setModele("Clio");
        v.setCategorie(CategorieVehicule.CITADINE);
        v.setTarifJournalier(new BigDecimal("80.00"));
        v.setStatut(StatutVehicule.DISPONIBLE);

        Vehicule saved = vehiculeRepository.save(v);
        assertNotNull(saved.getIdVehicule());

        vehiculeRepository.findAll().forEach(x ->
                System.out.println("findAll -> " + x.getIdVehicule() + " | " + x.getMarque() + " " + x.getModele()));
        System.out.println("count vehicules -> " + vehiculeRepository.count());

        // modification puis save()
        saved.setStatut(StatutVehicule.LOUE);
        Vehicule updated = vehiculeRepository.save(saved);
        assertEquals(StatutVehicule.LOUE, updated.getStatut());
        assertEquals(StatutVehicule.LOUE,
                vehiculeRepository.findById(saved.getIdVehicule()).orElseThrow().getStatut());

        vehiculeRepository.deleteById(saved.getIdVehicule());
        assertFalse(vehiculeRepository.existsById(saved.getIdVehicule()));
    }

    // ---------- Partie 5 : exercice autonome (Employe, Equipement, Maintenance) ----------
    @Test
    void testEmployeRepository() {
        Employe e = new Employe();
        e.setNom("Gharbi");
        e.setPrenom("Omar");
        e.setRole(RoleEmploye.AGENT);

        Employe saved = employeRepository.save(e);
        assertNotNull(saved.getIdEmploye());
        employeRepository.findAll().forEach(x ->
                System.out.println("Employe -> " + x.getIdEmploye() + " | " + x.getNom() + " | " + x.getRole()));
        assertTrue(employeRepository.existsById(saved.getIdEmploye()));

        employeRepository.deleteById(saved.getIdEmploye());
        assertFalse(employeRepository.existsById(saved.getIdEmploye()));
    }

    @Test
    void testEquipementRepository() {
        Equipement eq = new Equipement();
        eq.setLibelle("GPS");

        Equipement saved = equipementRepository.save(eq);
        assertNotNull(saved.getIdEquipement());
        System.out.println("Equipement -> " + equipementRepository.findById(saved.getIdEquipement())
                .orElseThrow().getLibelle());
        assertTrue(equipementRepository.existsById(saved.getIdEquipement()));

        equipementRepository.deleteById(saved.getIdEquipement());
        assertFalse(equipementRepository.existsById(saved.getIdEquipement()));
    }

    @Test
    void testMaintenanceRepository() {
        Maintenance m = new Maintenance();
        m.setDateDebut(LocalDate.now());
        m.setDateFin(LocalDate.now().plusDays(2));
        m.setDescription("Vidange et contrôle des freins");

        Maintenance saved = maintenanceRepository.save(m);
        assertNotNull(saved.getIdMaintenance());
        maintenanceRepository.findAll().forEach(x ->
                System.out.println("Maintenance -> " + x.getIdMaintenance() + " | " + x.getDescription()));
        assertTrue(maintenanceRepository.existsById(saved.getIdMaintenance()));

        maintenanceRepository.deleteById(saved.getIdMaintenance());
        assertFalse(maintenanceRepository.existsById(saved.getIdMaintenance()));
    }
}