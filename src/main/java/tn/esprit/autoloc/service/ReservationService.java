package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.service.observer.ReservationSubject;

import java.time.Clock;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationSubject reservationSubject;
    private final Clock clock;

    public Reservation creerReservation(Client client) {
        Reservation reservation = Reservation.builder()
                .client(client)
                .dateDebut(LocalDate.now(clock))
                .statut(StatutReservation.CONFIRMEE.name())
                .build();

        reservationSubject.notifierConfirmation(reservation);

        return reservation;
    }

    public Reservation annulerReservation(Reservation reservation) {
        reservation.setStatut(StatutReservation.ANNULEE.name());
        log.info("Reservation #{} annulée pour le client {}",
                reservation.getId(),
                reservation.getClient() != null ? reservation.getClient().getEmail() : "N/A");

        reservationSubject.notifierAnnulation(reservation);

        return reservation;
    }
}