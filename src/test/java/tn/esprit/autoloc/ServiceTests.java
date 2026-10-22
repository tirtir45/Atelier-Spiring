package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.domain.enums.*;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServiceTests {

    @Autowired private IAgenceService agenceService;
    @Autowired private IClientService clientService;
    @Autowired private IVehiculeService vehiculeService;
    @Autowired private IEmployeService employeService;
    @Autowired private IEquipementService equipementService;
    @Autowired private IContratService contratService;

    // ---------- Partie 2 : AgenceService ----------
    @Test
    void testAgenceService() {
        int countBefore = agenceService.findAll().size();
        System.out.println("Agences avant : " + countBefore);

        Agence agence = new Agence();
        agence.setNom("Agence Ben Arous");
        agence.setVille("Tunis");
        agence.setAdresse("Ben Arous");
        agence.setTelephone("12345678");

        Agence saved = agenceService.create(agence);
        assertNotNull(saved.getIdAgence());
        Long id = saved.getIdAgence();
        System.out.println("create -> id=" + id + ", nom=" + saved.getNom());

        System.out.println("findAll :");
        agenceService.findAll().forEach(a ->
                System.out.println(a.getIdAgence() + " | " + a.getNom() + " | " + a.getVille()));

        Agence found = agenceService.findById(id);
        assertEquals("Agence Ben Arous", found.getNom());
        System.out.println("findById(" + id + ") -> " + found.getNom());

        assertEquals(countBefore + 1, agenceService.findAll().size());
        System.out.println("Agences après create : " + agenceService.findAll().size());

        Agence modif = new Agence();
        modif.setNom("Agence Ben Arous");
        modif.setVille("Sfax");
        modif.setAdresse(found.getAdresse());
        modif.setTelephone(found.getTelephone());
        Agence updated = agenceService.update(id, modif);
        assertEquals("Sfax", updated.getVille());
        assertEquals("Sfax", agenceService.findById(id).getVille());
        System.out.println("update -> ville = " + updated.getVille());

        agenceService.deleteById(id);
        var ex = assertThrows(ResourceNotFoundException.class, () -> agenceService.findById(id));
        System.out.println("deleteById(" + id + ") puis findById -> " + ex.getMessage());
    }

    //-----------Client---------------------
    @Test
    void testClientService() {
        int countBefore = clientService.findAll().size();
        System.out.println("Clients avant : " + countBefore);

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

        Client s1 = clientService.create(c1);
        Client s2 = clientService.create(c2);
        assertNotNull(s1.getIdClient());
        assertNotNull(s2.getIdClient());
        Long id1 = s1.getIdClient();
        Long id2 = s2.getIdClient();
        System.out.println("create -> id=" + id1 + " (" + s1.getNom() + "), id=" + id2 + " (" + s2.getNom() + ")");

        System.out.println("findAll :");
        clientService.findAll().forEach(c ->
                System.out.println(c.getIdClient() + " | " + c.getNom() + " " + c.getPrenom()));

        Client found = clientService.findById(id1);
        assertEquals("Ben Ali", found.getNom());
        System.out.println("findById(" + id1 + ") -> " + found.getNom() + " " + found.getPrenom());

        assertEquals(countBefore + 2, clientService.findAll().size());
        System.out.println("Clients après create : " + clientService.findAll().size());

        clientService.deleteById(id1);
        clientService.deleteById(id2);
        assertThrows(ResourceNotFoundException.class, () -> clientService.findById(id1));
        assertThrows(ResourceNotFoundException.class, () -> clientService.findById(id2));
        assertEquals(countBefore, clientService.findAll().size());
        System.out.println("deleteById x2 -> clients restants : " + clientService.findAll().size());
    }

    // ---------- Partie 4 : VehiculeService ----------
    @Test
    void testVehiculeService() {
        int countBefore = vehiculeService.findAll().size();
        System.out.println("Véhicules avant : " + countBefore);

        Vehicule v = new Vehicule();
        v.setImmatriculation("123 TUN 4567");
        v.setMarque("Renault");
        v.setModele("Clio");
        v.setCategorie(CategorieVehicule.CITADINE);
        v.setTarifJournalier(new BigDecimal("80.00"));
        v.setStatut(StatutVehicule.DISPONIBLE);

        Vehicule saved = vehiculeService.create(v);
        assertNotNull(saved.getIdVehicule());
        Long id = saved.getIdVehicule();
        System.out.println("create -> id=" + id + ", statut=" + saved.getStatut());

        System.out.println("findAll :");
        vehiculeService.findAll().forEach(x ->
                System.out.println(x.getIdVehicule() + " | " + x.getMarque() + " " + x.getModele()
                        + " | " + x.getStatut()));

        Vehicule found = vehiculeService.findById(id);
        assertEquals("Clio", found.getModele());
        System.out.println("findById(" + id + ") -> " + found.getMarque() + " " + found.getModele());

        assertEquals(countBefore + 1, vehiculeService.findAll().size());
        System.out.println("Véhicules après create : " + vehiculeService.findAll().size());

        Vehicule negatif = new Vehicule();
        negatif.setTarifJournalier(new BigDecimal("-5"));
        var ex = assertThrows(IllegalArgumentException.class, () -> vehiculeService.create(negatif));
        System.out.println("create tarif négatif -> " + ex.getMessage());

        vehiculeService.deleteById(id);
        assertThrows(ResourceNotFoundException.class, () -> vehiculeService.findById(id));
        System.out.println("deleteById(" + id + ") puis findById -> ResourceNotFoundException");
    }

    // ---------- Partie 5 : exercice autonome (Employe, Equipement, Contrat) ----------
    @Test
    void testEmployeService() {
        Employe e = new Employe();
        e.setNom("Gharbi");
        e.setPrenom("Omar");
        e.setRole(RoleEmploye.AGENT);

        Employe saved = employeService.create(e);
        assertNotNull(saved.getIdEmploye());
        Long id = saved.getIdEmploye();
        System.out.println("create -> id=" + id + ", role=" + saved.getRole());

        System.out.println("findAll :");
        employeService.findAll().forEach(x ->
                System.out.println(x.getIdEmploye() + " | " + x.getNom() + " " + x.getPrenom()
                        + " | " + x.getRole()));

        Employe found = employeService.findById(id);
        assertEquals("Gharbi", found.getNom());
        System.out.println("findById(" + id + ") -> " + found.getNom());

        employeService.deleteById(id);
        assertThrows(ResourceNotFoundException.class, () -> employeService.findById(id));
        System.out.println("deleteById(" + id + ") puis findById -> ResourceNotFoundException");
    }

    @Test
    void testEquipementService() {
        Equipement eq = new Equipement();
        eq.setLibelle("GPS");

        Equipement saved = equipementService.create(eq);
        assertNotNull(saved.getIdEquipement());
        Long id = saved.getIdEquipement();
        System.out.println("create -> id=" + id + ", libelle=" + saved.getLibelle());

        Equipement found = equipementService.findById(id);
        assertEquals("GPS", found.getLibelle());
        System.out.println("findById(" + id + ") -> " + found.getLibelle());

        Equipement sansLibelle = new Equipement();
        var ex = assertThrows(IllegalArgumentException.class, () -> equipementService.create(sansLibelle));
        System.out.println("create sans libellé -> " + ex.getMessage());

        equipementService.deleteById(id);
        assertThrows(ResourceNotFoundException.class, () -> equipementService.findById(id));
        System.out.println("deleteById(" + id + ") puis findById -> ResourceNotFoundException");
    }

    @Test
    void testContratService() {
        Contrat c = new Contrat();
        c.setDateSignature(LocalDate.now());
        c.setMontantTotal(new BigDecimal("450.00"));
        c.setValide(true);

        Contrat saved = contratService.create(c);
        assertNotNull(saved.getIdContrat());
        Long id = saved.getIdContrat();
        System.out.println("create -> id=" + id + ", montant=" + saved.getMontantTotal());

        System.out.println("findAll :");
        contratService.findAll().forEach(x ->
                System.out.println(x.getIdContrat() + " | " + x.getMontantTotal() + " | valide=" + x.isValide()));

        Contrat modif = new Contrat();
        modif.setDateSignature(saved.getDateSignature());
        modif.setMontantTotal(new BigDecimal("500.00"));
        modif.setValide(true);
        Contrat updated = contratService.update(id, modif);
        assertEquals(0, new BigDecimal("500.00").compareTo(updated.getMontantTotal()));
        System.out.println("update -> montant = " + updated.getMontantTotal());

        Contrat negatif = new Contrat();
        negatif.setMontantTotal(new BigDecimal("-10"));
        var ex = assertThrows(IllegalArgumentException.class, () -> contratService.create(negatif));
        System.out.println("create montant négatif -> " + ex.getMessage());

        contratService.deleteById(id);
        assertThrows(ResourceNotFoundException.class, () -> contratService.findById(id));
        System.out.println("deleteById(" + id + ") puis findById -> ResourceNotFoundException");
    }



}