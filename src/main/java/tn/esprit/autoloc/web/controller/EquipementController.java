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
import tn.esprit.autoloc.service.IEquipementService;
import tn.esprit.autoloc.web.dto.EquipementDTO;

import java.util.List;

@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
@Tag(name = "Équipements", description = "Gestion des équipements")
public class EquipementController {

    private final IEquipementService equipementService;

    @Operation(summary = "Lister les équipements")
    @ApiResponse(responseCode = "200", description = "Liste des équipements")
    @GetMapping
    public ResponseEntity<List<EquipementDTO>> findAll() {
        return ResponseEntity.ok(equipementService.findAll());
    }

    @Operation(summary = "Consulter un équipement")
    @ApiResponse(responseCode = "200", description = "Équipement trouvé")
    @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    @GetMapping("/{id}")
    public ResponseEntity<EquipementDTO> findById(@Parameter(description = "Identifiant de l'équipement") @PathVariable Long id) {
        return ResponseEntity.ok(equipementService.findById(id));
    }

    @Operation(summary = "Créer un équipement")
    @ApiResponse(responseCode = "201", description = "Équipement créé")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @PostMapping
    public ResponseEntity<EquipementDTO> creer(@Valid @RequestBody EquipementDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(equipementService.creer(dto));
    }

    @Operation(summary = "Modifier un équipement")
    @ApiResponse(responseCode = "200", description = "Équipement modifié")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    @PutMapping("/{id}")
    public ResponseEntity<EquipementDTO> update(@Parameter(description = "Identifiant de l'équipement") @PathVariable Long id,
                                               @Valid @RequestBody EquipementDTO dto) {
        return ResponseEntity.ok(equipementService.update(id, dto));
    }

    @Operation(summary = "Supprimer un équipement")
    @ApiResponse(responseCode = "204", description = "Équipement supprimé")
    @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "Identifiant de l'équipement") @PathVariable Long id) {
        equipementService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
