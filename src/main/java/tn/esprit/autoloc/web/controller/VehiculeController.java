package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeDTO;

import java.util.List;

@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
@Tag(name = "Véhicules", description = "Gestion des véhicules")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @Operation(summary = "Lister les véhicules")
    @ApiResponse(responseCode = "200", description = "Liste des véhicules")
    @GetMapping
    public ResponseEntity<List<VehiculeDTO>> findAll() {
        return ResponseEntity.ok(vehiculeService.findAll());
    }

    @Operation(summary = "Consulter un véhicule")
    @ApiResponse(responseCode = "200", description = "Véhicule trouvé")
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDTO> findById(@Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        return ResponseEntity.ok(vehiculeService.findById(id));
    }

    @Operation(summary = "Créer un véhicule")
    @ApiResponse(responseCode = "201", description = "Véhicule créé")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @PostMapping
    public ResponseEntity<VehiculeDTO> creer(@Valid @RequestBody VehiculeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehiculeService.creer(dto));
    }

    @Operation(summary = "Modifier un véhicule")
    @ApiResponse(responseCode = "200", description = "Véhicule modifié")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    @PutMapping("/{id}")
    public ResponseEntity<VehiculeDTO> update(@Parameter(description = "Identifiant du véhicule") @PathVariable Long id,
                                             @Valid @RequestBody VehiculeDTO dto) {
        return ResponseEntity.ok(vehiculeService.update(id, dto));
    }

    @Operation(summary = "Supprimer un véhicule")
    @ApiResponse(responseCode = "204", description = "Véhicule supprimé")
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        vehiculeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Affecter un véhicule à une agence")
    @ApiResponse(responseCode = "200", description = "Véhicule affecté avec succès")
    @ApiResponse(responseCode = "404", description = "Véhicule ou agence introuvable")
    @PutMapping("/{vehiculeId}/affecter-agence/{agenceId}")
    public ResponseEntity<Void> affecterVehiculeAAgence(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long vehiculeId,
            @Parameter(description = "Identifiant de l'agence") @PathVariable Long agenceId) {
        vehiculeService.affecterVehiculeAAgence(vehiculeId, agenceId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Désaffecter un véhicule de son agence")
    @ApiResponse(responseCode = "200", description = "Véhicule désaffecté avec succès")
    @ApiResponse(responseCode = "400", description = "Véhicule non rattaché à une agence")
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    @PutMapping("/{vehiculeId}/desaffecter-agence")
    public ResponseEntity<Void> desaffecterVehiculeDeAgence(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long vehiculeId) {
        vehiculeService.desaffecterVehiculeDeAgence(vehiculeId);
        return ResponseEntity.ok().build();
    }
}
