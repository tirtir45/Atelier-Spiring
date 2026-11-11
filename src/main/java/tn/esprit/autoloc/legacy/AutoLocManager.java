package tn.esprit.autoloc.legacy;

/**
 * AutoLoc v0 : Classe monolithique et problématique représentant la première version
 * de l'application AutoLoc (Atelier 6 - Analyse).
 * 
 * Cette classe viole plusieurs principes SOLID, notamment le SRP, l'OCP et le DIP.
 */
public class AutoLocManager {

    public void louerVehicule(String typeReservation, int nombreJours, String clientEmail) {
        double prix = 0;
        if ("LongDuree".equalsIgnoreCase(typeReservation)) {
            prix = nombreJours * 120.0;
        } else if ("Weekend".equalsIgnoreCase(typeReservation)) {
            prix = nombreJours * 180.0;
        } else if ("Standard".equalsIgnoreCase(typeReservation)) {
            prix = nombreJours * 150.0;
        }

        System.out.println("Contrat créé");
        System.out.println("Prix total : " + prix);
        envoyerEmail(clientEmail);
        enregistrerPaiement();
        sauvegarderContrat();
    }

    private void envoyerEmail(String email) {
        System.out.println("Email envoyé à " + email);
    }

    private void envoyerSMS(String sms) {
        System.out.println("SMS envoyé à " + sms);
    }

    private void sauvegarderContrat() {
        System.out.println("Contrat sauvegardé");
    }

    private void enregistrerPaiement() {
        System.out.println("Paiement enregistré");
    }
}

