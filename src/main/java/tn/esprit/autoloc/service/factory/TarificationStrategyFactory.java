package tn.esprit.autoloc.service.factory;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.service.strategy.ITarificationStrategy;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Sélectionne dynamiquement la TarificationStrategy adaptée à une réservation
 * (durée du séjour, jour de début). Toutes les implémentations Spring de ITarificationStrategy
 * sont injectées automatiquement (Map&lt;code, strategy&gt;).
 */
@Component
public class TarificationStrategyFactory {

    private static final Set<DayOfWeek> JOURS_WEEKEND =
            EnumSet.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    private final Map<String, ITarificationStrategy> strategies;

    public TarificationStrategyFactory(List<? extends ITarificationStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(ITarificationStrategy::getCode, Function.identity()));
    }

    /**
     * Résout la stratégie tarifaire appropriée en fonction des critères de la réservation :
     * <ul>
     *   <li>Séjour &gt;= 7 jours : {@code LONGUE_DUREE}</li>
     *   <li>Séjour &lt;= 3 jours débutant un vendredi, samedi ou dimanche : {@code WEEKEND}</li>
     *   <li>Autre durée : {@code STANDARD} (stratégie par défaut)</li>
     * </ul>
     */
    public ITarificationStrategy resoudre(Reservation reservation) {
        if (reservation == null || reservation.getDateDebut() == null || reservation.getDateFin() == null) {
            return strategies.get("STANDARD");
        }

        long jours = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());

        if (jours >= 7) {
            return strategies.get("LONGUE_DUREE");
        }
        if (jours <= 3 && estWeekend(reservation)) {
            return strategies.get("WEEKEND");
        }
        return strategies.get("STANDARD");
    }

    private boolean estWeekend(Reservation reservation) {
        if (reservation.getDateDebut() == null) {
            return false;
        }
        return JOURS_WEEKEND.contains(reservation.getDateDebut().getDayOfWeek());
    }
}