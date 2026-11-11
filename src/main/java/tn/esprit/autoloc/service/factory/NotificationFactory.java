package tn.esprit.autoloc.service.factory;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.service.observer.EmailNotificationObserver;
import tn.esprit.autoloc.service.observer.IReservationObserver;
import tn.esprit.autoloc.service.observer.SmsNotificationObserver;

/**
 * Patron GoF : FACTORY METHOD appliqué aux observateurs de notification.
 * Permet d'obtenir le bon canal de notification (email, sms, ...) sans exposer
 * les classes concrètes au code appelant.
 */
@Component
public class NotificationFactory {

    private final EmailNotificationObserver emailNotificationObserver;
    private final SmsNotificationObserver smsNotificationObserver;

    public NotificationFactory(EmailNotificationObserver emailNotificationObserver,
                               SmsNotificationObserver smsNotificationObserver) {
        this.emailNotificationObserver = emailNotificationObserver;
        this.smsNotificationObserver = smsNotificationObserver;
    }

    /**
     * Crée / Fournit l'observateur correspondant au canal demandé.
     *
     * @param canal Canal souhaité ("EMAIL", "SMS")
     * @return L'instance concrète de l'observateur masquée derrière son abstraction
     */
    public IReservationObserver creerObservateur(String canal) {
        if (canal == null) {
            throw new IllegalArgumentException("Le canal de notification ne peut pas être null");
        }

        return switch (canal.trim().toUpperCase()) {
            case "EMAIL" -> emailNotificationObserver;
            case "SMS" -> smsNotificationObserver;
            default -> throw new IllegalArgumentException("Canal de notification non supporté : " + canal);
        };
    }
}

