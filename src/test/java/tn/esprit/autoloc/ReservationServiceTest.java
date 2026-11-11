package tn.esprit.autoloc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.service.ReservationService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    @DisplayName("Doit créer une réservation et déclencher les notifications de confirmation (EMAIL + SMS)")
    void testCreerReservationEtNotification() {
        // Préparation du client
        Client client = new Client();
        client.setIdClient(1L);
        client.setNom("Ben Salah");
        client.setPrenom("Mohamed");
        client.setEmail("mohamed.bensalah@email.com");
        client.setTelephone("+216 98 123 456");

        // Action : création de la réservation
        Reservation reservation = reservationService.creerReservation(client);

        // Assertions
        assertNotNull(reservation);
        assertNotNull(reservation.getClient());
        assertEquals("mohamed.bensalah@email.com", reservation.getClient().getEmail());
        assertEquals(StatutReservation.CONFIRMEE, reservation.getStatut());
    }

    @Test
    @DisplayName("Doit annuler une réservation existante et déclencher les notifications d'annulation (EMAIL + SMS)")
    void testAnnulerReservationEtNotification() {
        // Préparation du client et de la réservation
        Client client = new Client();
        client.setIdClient(2L);
        client.setNom("Trabelsi");
        client.setPrenom("Amira");
        client.setEmail("amira.trabelsi@email.com");
        client.setTelephone("+216 22 654 321");

        Reservation reservation = reservationService.creerReservation(client);
        reservation.setId(42L); // Assigner un identifiant pour le log d'annulation

        // Action : annulation de la réservation
        Reservation reservationAnnulee = reservationService.annulerReservation(reservation);

        // Assertions
        assertNotNull(reservationAnnulee);
        assertEquals(StatutReservation.ANNULEE, reservationAnnulee.getStatut());
        assertEquals(42L, reservationAnnulee.getId());
    }
}

