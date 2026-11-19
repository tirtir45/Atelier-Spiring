package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.validator.ReservationRequestValidator;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Réservations", description = "Gestion des réservations")
public class ReservationController {

    private final IReservationService reservationService;
    private final ReservationRequestValidator reservationRequestValidator;

    @Operation(summary = "Lister les réservations")
    @ApiResponse(responseCode = "200", description = "Liste des réservations")
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> findAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @Operation(summary = "Consulter une réservation")
    @ApiResponse(responseCode = "200", description = "Réservation trouvée")
    @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> findById(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        return ResponseEntity.ok(reservationService.findById(id));
    }

    @Operation(summary = "Créer une réservation avec validation")
    @ApiResponse(responseCode = "201", description = "Réservation créée")
    @ApiResponse(responseCode = "400", description = "Données invalides ou incohérence de dates")
    @PostMapping
    public ResponseEntity<?> creer(
            @Valid @RequestBody ReservationRequestDTO dto,
            BindingResult result) {
        reservationRequestValidator.validate(dto, result);
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    result.getFieldErrors().stream()
                            .map(error -> error.getField() + " : " + error.getDefaultMessage())
                            .toList()
            );
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.creer(dto));
    }

    @Operation(summary = "Modifier une réservation")
    @ApiResponse(responseCode = "200", description = "Réservation modifiée")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> update(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id,
                                                         @Valid @RequestBody ReservationRequestDTO dto) {
        return ResponseEntity.ok(reservationService.update(id, dto));
    }

    @Operation(summary = "Supprimer une réservation")
    @ApiResponse(responseCode = "204", description = "Réservation supprimée")
    @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
