package tn.esprit.autoloc.service.strategy;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

/**
 * Stratégie concrète : Tarif standard sans remise.
 */
@Component
public class TarifStandardStrategy implements ITarificationStrategy, TarificationStrategy {

    @Override
    public BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation) {
        if (vehicule == null || reservation == null || reservation.getDateDebut() == null || reservation.getDateFin() == null) {
            return BigDecimal.ZERO;
        }

        long jours = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
        if (jours <= 0) {
            jours = 1;
        }

        BigDecimal tarifJournalier = vehicule.getTarifJournalier() != null ? vehicule.getTarifJournalier() : BigDecimal.ZERO;
        return tarifJournalier.multiply(BigDecimal.valueOf(jours));
    }

    @Override
    public String getCode() {
        return "STANDARD";
    }
}

