package tn.esprit.autoloc.legacy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AutoLoc v0 : Classe monolithique et problématique représentant la première version
 * de l'application AutoLoc (Atelier 6 - Analyse).
 *
 * Cette classe viole plusieurs principes SOLID, notamment le SRP, l'OCP et le DIP.
 */
public class AutoLocManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoLocManager.class);

    public void louerVehicule(String typeReservation, int nombreJours, String clientEmail) {
        double prix = 0;
        if ("LongDuree".equalsIgnoreCase(typeReservation)) {
            prix = nombreJours * 120.0;
        } else if ("Weekend".equalsIgnoreCase(typeReservation)) {
            prix = nombreJours * 180.0;
        } else if ("Standard".equalsIgnoreCase(typeReservation)) {
            prix = nombreJours * 150.0;
        }

        LOGGER.info("Contrat créé");
        LOGGER.info("Prix total : {}", prix);
        envoyerEmail(clientEmail);
        enregistrerPaiement();
        sauvegarderContrat();
    }

    private void envoyerEmail(String email) {
        LOGGER.info("Email envoyé à {}", email);
    }

    private void sauvegarderContrat() {
        LOGGER.info("Contrat sauvegardé");
    }

    private void enregistrerPaiement() {
        LOGGER.info("Paiement enregistré");
    }
}