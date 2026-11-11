package tn.esprit.autoloc.service.strategy;

import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;

import java.math.BigDecimal;

/**
 * Patron GoF : STRATEGY.
 * Interface commune pour tous les algorithmes de calcul du tarif d'une réservation.
 */
public interface ITarificationStrategy {

    /**
     * Calcule le tarif de location pour un véhicule et une réservation donnés.
     *
     * @param vehicule    Le véhicule concerné par la location
     * @param reservation La réservation contenant les dates
     * @return Le montant total calculé en BigDecimal
     */
    BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation);

    /**
     * Code unique identifiant la stratégie (ex. STANDARD, WEEKEND, LONGUE_DUREE).
     * Utilisé pour la sélection dynamique dans la Factory.
     */
    String getCode();
}

