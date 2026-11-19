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
import tn.esprit.autoloc.service.IMaintenanceService;
import tn.esprit.autoloc.web.dto.MaintenanceDTO;

import java.util.List;

@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
@Tag(name = "Maintenances", description = "Gestion des maintenances")
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;

    @Operation(summary = "Lister les maintenances")
    @ApiResponse(responseCode = "200", description = "Liste des maintenances")
    @GetMapping
    public ResponseEntity<List<MaintenanceDTO>> findAll() {
        return ResponseEntity.ok(maintenanceService.findAll());
    }

    @Operation(summary = "Consulter une maintenance")
    @ApiResponse(responseCode = "200", description = "Maintenance trouvée")
    @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceDTO> findById(@Parameter(description = "Identifiant de la maintenance") @PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.findById(id));
    }

    @Operation(summary = "Créer une maintenance")
    @ApiResponse(responseCode = "201", description = "Maintenance créée")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @PostMapping
    public ResponseEntity<MaintenanceDTO> creer(@Valid @RequestBody MaintenanceDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(maintenanceService.creer(dto));
    }

    @Operation(summary = "Modifier une maintenance")
    @ApiResponse(responseCode = "200", description = "Maintenance modifiée")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceDTO> update(@Parameter(description = "Identifiant de la maintenance") @PathVariable Long id,
                                                 @Valid @RequestBody MaintenanceDTO dto) {
        return ResponseEntity.ok(maintenanceService.update(id, dto));
    }

    @Operation(summary = "Supprimer une maintenance")
    @ApiResponse(responseCode = "204", description = "Maintenance supprimée")
    @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "Identifiant de la maintenance") @PathVariable Long id) {
        maintenanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
