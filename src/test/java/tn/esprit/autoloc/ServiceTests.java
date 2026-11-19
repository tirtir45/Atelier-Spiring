package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.enums.*;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.*;
import tn.esprit.autoloc.web.dto.*;

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

        AgenceDTO agence = new AgenceDTO();
        agence.setNom("Agence Ben Arous");
        agence.setVille("Tunis");
        agence.setAdresse("Ben Arous");
        agence.setTelephone("12345678");

        AgenceDTO saved = agenceService.creer(agence);
        assertNotNull(saved.getId());
        Long id = saved.getId();
        System.out.println("creer -> id=" + id + ", nom=" + saved.getNom());

        System.out.println("findAll :");
        agenceService.findAll().forEach(a ->
                System.out.println(a.getId() + " | " + a.getNom() + " | " + a.getVille()));

        AgenceDTO found = agenceService.findById(id);
        assertEquals("Agence Ben Arous", found.getNom());
        System.out.println("findById(" + id + ") -> " + found.getNom());

        assertEquals(countBefore + 1, agenceService.findAll().size());
        System.out.println("Agences après create : " + agenceService.findAll().size());

        AgenceDTO modif = new AgenceDTO();
        modif.setNom("Agence Ben Arous");
        modif.setVille("Sfax");
        modif.setAdresse(found.getAdresse());
        modif.setTelephone(found.getTelephone());
        AgenceDTO updated = agenceService.update(id, modif);
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

        ClientDTO c1 = new ClientDTO();
        c1.setNom("Ben Ali");
        c1.setPrenom("Sami");
        c1.setEmail("sami.benali@mail.com");
        c1.setTelephone("20111222");
        c1.setNumPermis("P-1001");
        c1.setDateInscription(LocalDate.now());

        ClientDTO c2 = new ClientDTO();
        c2.setNom("Trabelsi");
        c2.setPrenom("Leila");
        c2.setEmail("leila.trabelsi@mail.com");
        c2.setTelephone("20333444");
        c2.setNumPermis("P-1002");
        c2.setDateInscription(LocalDate.now());

        ClientDTO s1 = clientService.creer(c1);
        ClientDTO s2 = clientService.creer(c2);
        assertNotNull(s1.getId());
        assertNotNull(s2.getId());
        Long id1 = s1.getId();
        Long id2 = s2.getId();
        System.out.println("creer -> id=" + id1 + " (" + s1.getNom() + "), id=" + id2 + " (" + s2.getNom() + ")");

        System.out.println("findAll :");
        clientService.findAll().forEach(c ->
                System.out.println(c.getId() + " | " + c.getNom() + " " + c.getPrenom()));

        ClientDTO found = clientService.findById(id1);
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

        VehiculeDTO v = new VehiculeDTO();
        v.setImmatriculation("123 TUN 4567");
        v.setMarque("Renault");
        v.setModele("Clio");
        v.setCategorie(CategorieVehicule.CITADINE);
        v.setTarifJournalier(new BigDecimal("80.00"));
        v.setStatut(StatutVehicule.DISPONIBLE);

        VehiculeDTO saved = vehiculeService.creer(v);
        assertNotNull(saved.getId());
        Long id = saved.getId();
        System.out.println("creer -> id=" + id + ", statut=" + saved.getStatut());

        System.out.println("findAll :");
        vehiculeService.findAll().forEach(x ->
                System.out.println(x.getId() + " | " + x.getMarque() + " " + x.getModele()
                        + " | " + x.getStatut()));

        VehiculeDTO found = vehiculeService.findById(id);
        assertEquals("Clio", found.getModele());
        System.out.println("findById(" + id + ") -> " + found.getMarque() + " " + found.getModele());

        assertEquals(countBefore + 1, vehiculeService.findAll().size());
        System.out.println("Véhicules après create : " + vehiculeService.findAll().size());

        vehiculeService.deleteById(id);
        assertThrows(ResourceNotFoundException.class, () -> vehiculeService.findById(id));
        System.out.println("deleteById(" + id + ") puis findById -> ResourceNotFoundException");
    }

    // ---------- Partie 5 : Employe, Equipement, Contrat ----------
    @Test
    void testEmployeService() {
        EmployeDTO e = new EmployeDTO();
        e.setNom("Gharbi");
        e.setPrenom("Omar");
        e.setRole(RoleEmploye.AGENT);

        EmployeDTO saved = employeService.creer(e);
        assertNotNull(saved.getId());
        Long id = saved.getId();
        System.out.println("creer -> id=" + id + ", role=" + saved.getRole());

        System.out.println("findAll :");
        employeService.findAll().forEach(x ->
                System.out.println(x.getId() + " | " + x.getNom() + " " + x.getPrenom()
                        + " | " + x.getRole()));

        EmployeDTO found = employeService.findById(id);
        assertEquals("Gharbi", found.getNom());
        System.out.println("findById(" + id + ") -> " + found.getNom());

        employeService.deleteById(id);
        assertThrows(ResourceNotFoundException.class, () -> employeService.findById(id));
        System.out.println("deleteById(" + id + ") puis findById -> ResourceNotFoundException");
    }

    @Test
    void testEquipementService() {
        EquipementDTO eq = new EquipementDTO();
        eq.setLibelle("GPS");

        EquipementDTO saved = equipementService.creer(eq);
        assertNotNull(saved.getId());
        Long id = saved.getId();
        System.out.println("creer -> id=" + id + ", libelle=" + saved.getLibelle());

        EquipementDTO found = equipementService.findById(id);
        assertEquals("GPS", found.getLibelle());
        System.out.println("findById(" + id + ") -> " + found.getLibelle());

        equipementService.deleteById(id);
        assertThrows(ResourceNotFoundException.class, () -> equipementService.findById(id));
        System.out.println("deleteById(" + id + ") puis findById -> ResourceNotFoundException");
    }

    @Test
    void testContratService() {
        ContratDTO c = new ContratDTO();
        c.setDateSignature(LocalDate.now());
        c.setMontantTotal(new BigDecimal("450.00"));
        c.setValide(true);

        ContratDTO saved = contratService.creer(c);
        assertNotNull(saved.getId());
        Long id = saved.getId();
        System.out.println("creer -> id=" + id + ", montant=" + saved.getMontantTotal());

        System.out.println("findAll :");
        contratService.findAll().forEach(x ->
                System.out.println(x.getId() + " | " + x.getMontantTotal() + " | valide=" + x.getValide()));

        ContratDTO modif = new ContratDTO();
        modif.setDateSignature(saved.getDateSignature());
        modif.setMontantTotal(new BigDecimal("500.00"));
        modif.setValide(true);
        ContratDTO updated = contratService.update(id, modif);
        assertEquals(0, new BigDecimal("500.00").compareTo(updated.getMontantTotal()));
        System.out.println("update -> montant = " + updated.getMontantTotal());

        contratService.deleteById(id);
        assertThrows(ResourceNotFoundException.class, () -> contratService.findById(id));
        System.out.println("deleteById(" + id + ") puis findById -> ResourceNotFoundException");
    }
}
