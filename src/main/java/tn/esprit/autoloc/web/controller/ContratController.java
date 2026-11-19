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
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratDTO;

import java.util.List;

@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
@Tag(name = "Contrats", description = "Gestion des contrats de location")
public class ContratController {

    private final IContratService contratService;

    @Operation(summary = "Lister les contrats")
    @ApiResponse(responseCode = "200", description = "Liste des contrats")
    @GetMapping
    public ResponseEntity<List<ContratDTO>> findAll() {
        return ResponseEntity.ok(contratService.findAll());
    }

    @Operation(summary = "Consulter un contrat")
    @ApiResponse(responseCode = "200", description = "Contrat trouvé")
    @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    @GetMapping("/{id}")
    public ResponseEntity<ContratDTO> findById(@Parameter(description = "Identifiant du contrat") @PathVariable Long id) {
        return ResponseEntity.ok(contratService.findById(id));
    }

    @Operation(summary = "Créer un contrat")
    @ApiResponse(responseCode = "201", description = "Contrat créé")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @PostMapping
    public ResponseEntity<ContratDTO> creer(@Valid @RequestBody ContratDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contratService.creer(dto));
    }

    @Operation(summary = "Modifier un contrat")
    @ApiResponse(responseCode = "200", description = "Contrat modifié")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    @PutMapping("/{id}")
    public ResponseEntity<ContratDTO> update(@Parameter(description = "Identifiant du contrat") @PathVariable Long id,
                                            @Valid @RequestBody ContratDTO dto) {
        return ResponseEntity.ok(contratService.update(id, dto));
    }

    @Operation(summary = "Supprimer un contrat")
    @ApiResponse(responseCode = "204", description = "Contrat supprimé")
    @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "Identifiant du contrat") @PathVariable Long id) {
        contratService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
