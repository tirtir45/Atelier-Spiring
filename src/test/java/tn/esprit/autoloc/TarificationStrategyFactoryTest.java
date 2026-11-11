package tn.esprit.autoloc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.service.factory.TarificationStrategyFactory;
import tn.esprit.autoloc.service.strategy.ITarificationStrategy;
import tn.esprit.autoloc.service.strategy.TarifLongueDureeStrategy;
import tn.esprit.autoloc.service.strategy.TarifStandardStrategy;
import tn.esprit.autoloc.service.strategy.TarifWeekendStrategy;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TarificationStrategyFactoryTest {

    private TarificationStrategyFactory factory;
    private TarifStandardStrategy standard;
    private TarifWeekendStrategy weekend;
    private TarifLongueDureeStrategy longueDuree;

    @BeforeEach
    void setUp() {
        standard = new TarifStandardStrategy();
        weekend = new TarifWeekendStrategy();
        longueDuree = new TarifLongueDureeStrategy();

        factory = new TarificationStrategyFactory(List.of(standard, weekend, longueDuree));
    }

    @Test
    @DisplayName("Test 1 : Longue durée (>= 7 jours) -> LONGUE_DUREE")
    void doitRetournerLongueDureePour7JoursOuPlus() {
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2024, 1, 1));
        reservation.setDateFin(LocalDate.of(2024, 1, 8)); // 7 jours

        ITarificationStrategy resultat = factory.resoudre(reservation);

        assertThat(resultat).isInstanceOf(TarifLongueDureeStrategy.class);
        assertThat(resultat.getCode()).isEqualTo("LONGUE_DUREE");
    }

    @Test
    @DisplayName("Test 2 : Borne exacte à 7 jours -> LONGUE_DUREE")
    void doitRetournerLongueDureePourExactement7Jours() {
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2024, 5, 1));
        reservation.setDateFin(LocalDate.of(2024, 5, 8)); // exactement 7 jours

        ITarificationStrategy resultat = factory.resoudre(reservation);

        assertThat(resultat.getCode()).isEqualTo("LONGUE_DUREE");
    }

    @Test
    @DisplayName("Test 3 : Court séjour commençant un vendredi (<= 3 jours) -> WEEKEND")
    void doitRetournerWeekendPourCourtSejourVendredi() {
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2024, 3, 8)); // Vendredi
        reservation.setDateFin(LocalDate.of(2024, 3, 10));  // 2 jours (<= 3)

        ITarificationStrategy resultat = factory.resoudre(reservation);

        assertThat(resultat).isInstanceOf(TarifWeekendStrategy.class);
        assertThat(resultat.getCode()).isEqualTo("WEEKEND");
    }

    @Test
    @DisplayName("Test 4 : Durée moyenne (4 à 6 jours) -> STANDARD")
    void doitRetournerStandardPourMoyenneDuree() {
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.of(2024, 3, 4)); // Lundi
        reservation.setDateFin(LocalDate.of(2024, 3, 8));   // 4 jours

        ITarificationStrategy resultat = factory.resoudre(reservation);

        assertThat(resultat.getCode()).isEqualTo("STANDARD");
    }
}

