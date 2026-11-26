package tn.esprit.autoloc.service.observer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;

@Slf4j
@Component
public class EmailNotificationObserver implements IReservationObserver, ReservationObserver {

    @Override
    public void onReservationConfirmee(Reservation reservation) {
        String email = (reservation != null && reservation.getClient() != null)
                ? reservation.getClient().getEmail() : "inconnu";
        Long id = reservation != null ? reservation.getId() : null;

        log.info("[EMAIL] Confirmation envoyee au client {} pour la reservation #{}", email, id);
    }

    @Override
    public void onReservationAnnulee(Reservation reservation) {
        String email = (reservation != null && reservation.getClient() != null)
                ? reservation.getClient().getEmail() : "inconnu";
        Long id = reservation != null ? reservation.getId() : null;

        log.info("[EMAIL] Annulation notifiee au client {} pour la reservation #{}", email, id);
    }
}

