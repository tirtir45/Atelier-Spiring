package tn.esprit.autoloc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tn.esprit.autoloc.service.factory.NotificationFactory;
import tn.esprit.autoloc.service.observer.EmailNotificationObserver;
import tn.esprit.autoloc.service.observer.IReservationObserver;
import tn.esprit.autoloc.service.observer.SmsNotificationObserver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationFactoryTest {

    private final EmailNotificationObserver emailObserver = new EmailNotificationObserver();
    private final SmsNotificationObserver smsObserver = new SmsNotificationObserver();
    private final NotificationFactory factory = new NotificationFactory(emailObserver, smsObserver);

    @Test
    @DisplayName("Doit retourner EmailNotificationObserver pour le canal EMAIL")
    void doitRetournerEmailObserver() {
        IReservationObserver observer = factory.creerObservateur("EMAIL");
        assertThat(observer).isInstanceOf(EmailNotificationObserver.class);
    }

    @Test
    @DisplayName("Doit retourner SmsNotificationObserver pour le canal SMS")
    void doitRetournerSmsObserver() {
        IReservationObserver observer = factory.creerObservateur("SMS");
        assertThat(observer).isInstanceOf(SmsNotificationObserver.class);
    }

    @Test
    @DisplayName("Doit lever une exception pour un canal inconnu")
    void doitLeverExceptionPourCanalInconnu() {
        assertThatThrownBy(() -> factory.creerObservateur("WHATSAPP"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Canal de notification non supporté");
    }
}

