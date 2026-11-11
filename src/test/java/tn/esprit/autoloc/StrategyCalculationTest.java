package tn.esprit.autoloc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.strategy.TarifLongueDureeStrategy;
import tn.esprit.autoloc.service.strategy.TarifStandardStrategy;
import tn.esprit.autoloc.service.strategy.TarifWeekendStrategy;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class StrategyCalculationTest {

    private final TarifStandardStrategy standardStrategy = new TarifStandardStrategy();
    private final TarifWeekendStrategy weekendStrategy = new TarifWeekendStrategy();
    private final TarifLongueDureeStrategy longueDureeStrategy = new TarifLongueDureeStrategy();

    @Test
    @DisplayName("Calcul tarif standard : 4 jours * 100.00 = 400.00")
    void testCalculStandard() {
        Vehicule vehicule = new Vehicule();
        vehicule.setTarifJournalier(new BigDecimal("100.00"));

        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2024, 3, 4));
        reservation.setDateFin(LocalDate.of(2024, 3, 8)); // 4 jours

        BigDecimal prix = standardStrategy.calculerTarif(vehicule, reservation);
        assertThat(prix).isEqualByComparingTo("400.00");
    }

    @Test
    @DisplayName("Calcul tarif weekend (10% remise) : 2 jours * 100.00 * 0.90 = 180.00")
    void testCalculWeekendAvecRemise() {
        Vehicule vehicule = new Vehicule();
        vehicule.setTarifJournalier(new BigDecimal("100.00"));

        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2024, 3, 8));
        reservation.setDateFin(LocalDate.of(2024, 3, 10)); // 2 jours

        BigDecimal prix = weekendStrategy.calculerTarif(vehicule, reservation);
        assertThat(prix).isEqualByComparingTo("180.00");
    }

    @Test
    @DisplayName("Calcul tarif longue durée (20% remise) : 10 jours * 100.00 * 0.80 = 800.00")
    void testCalculLongueDureeAvecRemise() {
        Vehicule vehicule = new Vehicule();
        vehicule.setTarifJournalier(new BigDecimal("100.00"));

        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2024, 1, 1));
        reservation.setDateFin(LocalDate.of(2024, 1, 11)); // 10 jours

        BigDecimal prix = longueDureeStrategy.calculerTarif(vehicule, reservation);
        assertThat(prix).isEqualByComparingTo("800.00");
    }
}

