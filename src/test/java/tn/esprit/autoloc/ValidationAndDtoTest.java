package tn.esprit.autoloc;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.ModePaiement;
import tn.esprit.autoloc.web.dto.*;
import tn.esprit.autoloc.web.validator.ReservationRequestValidator;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationAndDtoTest {

    private static final ZoneId ZONE = ZoneId.of("Africa/Tunis");
    private static final Clock FIXED_CLOCK =
            Clock.fixed(Instant.parse("2026-11-24T10:00:00Z"), ZONE);

    private static Validator beanValidator;
    private final ReservationRequestValidator springValidator =
            new ReservationRequestValidator(FIXED_CLOCK);

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        beanValidator = factory.getValidator();
    }

    @Test
    @DisplayName("5.6 : Créer un client avec un email invalide -> rejeté")
    void testClientEmailInvalide() {
        ClientDTO dto = ClientDTO.builder()
                .nom("Ben Ali")
                .prenom("Karim")
                .email("email-invalide-sans-arobase")
                .build();

        Set<ConstraintViolation<ClientDTO>> violations = beanValidator.validate(dto);
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    @DisplayName("5.6 : Créer un véhicule sans immatriculation -> rejeté")
    void testVehiculeSansImmatriculation() {
        VehiculeDTO dto = VehiculeDTO.builder()
                .immatriculation("")
                .marque("Peugeot")
                .modele("308")
                .categorie(CategorieVehicule.BERLINE)
                .tarifJournalier(new BigDecimal("120.00"))
                .agenceId(1L)
                .build();

        Set<ConstraintViolation<VehiculeDTO>> violations = beanValidator.validate(dto);
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("immatriculation"));
    }

    @Test
    @DisplayName("5.6 : Créer un paiement avec un montant nul ou négatif -> rejeté")
    void testPaiementMontantNegatif() {
        PaiementDTO dto = PaiementDTO.builder()
                .montant(new BigDecimal("-50.00"))
                .modePaiement(ModePaiement.CARTE)
                .contratId(1L)
                .build();

        Set<ConstraintViolation<PaiementDTO>> violations = beanValidator.validate(dto);
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("montant"));
    }

    @Test
    @DisplayName("5.6 : Envoyer une réservation sans date de début ou de fin -> rejeté")
    void testReservationSansDates() {
        ReservationRequestDTO dto = ReservationRequestDTO.builder()
                .clientId(1L)
                .vehiculeId(2L)
                .dateDebut(null)
                .dateFin(null)
                .build();

        Set<ConstraintViolation<ReservationRequestDTO>> violations = beanValidator.validate(dto);
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("dateDebut"))
                .anyMatch(v -> v.getPropertyPath().toString().equals("dateFin"));
    }

    @Test
    @DisplayName("5.6 : Réservation dont dateFin <= dateDebut -> le validateur personnalisé retourne une erreur")
    void testReservationDateFinAnterieureOuEgale() {
        LocalDate today = LocalDate.now(FIXED_CLOCK);

        ReservationRequestDTO dto = ReservationRequestDTO.builder()
                .clientId(1L)
                .vehiculeId(2L)
                .dateDebut(today.plusDays(5))
                .dateFin(today.plusDays(3)) // Antérieure
                .build();

        Errors errors = new BeanPropertyBindingResult(dto, "reservationRequestDTO");
        springValidator.validate(dto, errors);

        assertThat(errors.hasFieldErrors("dateFin")).isTrue();
        assertThat(errors.getFieldError("dateFin").getDefaultMessage())
                .contains("postérieure à la date de début");
    }

    @Test
    @DisplayName("5.6 : Réservation avec dateDebut dans le passé -> rejeté")
    void testReservationDateDebutPassee() {
        LocalDate today = LocalDate.now(FIXED_CLOCK);

        ReservationRequestDTO dto = ReservationRequestDTO.builder()
                .clientId(1L)
                .vehiculeId(2L)
                .dateDebut(today.minusDays(1))
                .dateFin(today.plusDays(2))
                .build();

        Errors errors = new BeanPropertyBindingResult(dto, "reservationRequestDTO");
        springValidator.validate(dto, errors);

        assertThat(errors.hasFieldErrors("dateDebut")).isTrue();
    }

    @Test
    @DisplayName("5.6 : Réservation avec dates cohérentes -> la validation réussit")
    void testReservationDatesCoherentes() {
        LocalDate today = LocalDate.now(FIXED_CLOCK);

        ReservationRequestDTO dto = ReservationRequestDTO.builder()
                .clientId(1L)
                .vehiculeId(2L)
                .dateDebut(today.plusDays(1))
                .dateFin(today.plusDays(5))
                .build();

        Errors errors = new BeanPropertyBindingResult(dto, "reservationRequestDTO");
        springValidator.validate(dto, errors);

        assertThat(errors.hasErrors()).isFalse();
    }
}