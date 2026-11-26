package tn.esprit.autoloc.service.strategy;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

@Component
public class TarifLongueDureeStrategy implements ITarificationStrategy, TarificationStrategy {

    private static final BigDecimal REMISE_LONGUE_DUREE = new BigDecimal("0.80");

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
        return totalBrut.multiply(REMISE_LONGUE_DUREE).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getCode() {
        return "LONGUE_DUREE";
    }
}

