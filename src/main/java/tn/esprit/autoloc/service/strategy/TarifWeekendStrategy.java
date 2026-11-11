package tn.esprit.autoloc.service.strategy;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

/**
 * Stratégie concrète : Tarif week-end (court séjour <= 3 jours commençant le week-end)
 * avec 10% de remise (coefficient 0.90).
 */
@Component
public class TarifWeekendStrategy implements ITarificationStrategy, TarificationStrategy {

    private static final BigDecimal REMISE_WEEKEND = new BigDecimal("0.90");

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
        BigDecimal totalBrut = tarifJournalier.multiply(BigDecimal.valueOf(jours));
        return totalBrut.multiply(REMISE_WEEKEND).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getCode() {
        return "WEEKEND";
    }
}

