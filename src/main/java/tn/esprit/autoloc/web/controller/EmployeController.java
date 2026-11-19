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
import tn.esprit.autoloc.service.IEmployeService;
import tn.esprit.autoloc.web.dto.EmployeDTO;

import java.util.List;

@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
@Tag(name = "Employés", description = "Gestion des employés")
public class EmployeController {

    private final IEmployeService employeService;

    @Operation(summary = "Lister les employés")
    @ApiResponse(responseCode = "200", description = "Liste des employés")
    @GetMapping
    public ResponseEntity<List<EmployeDTO>> findAll() {
        return ResponseEntity.ok(employeService.findAll());
    }

    @Operation(summary = "Consulter un employé")
    @ApiResponse(responseCode = "200", description = "Employé trouvé")
    @ApiResponse(responseCode = "404", description = "Employé introuvable")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeDTO> findById(@Parameter(description = "Identifiant de l'employé") @PathVariable Long id) {
        return ResponseEntity.ok(employeService.findById(id));
    }

    @Operation(summary = "Créer un employé")
    @ApiResponse(responseCode = "201", description = "Employé créé")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @PostMapping
    public ResponseEntity<EmployeDTO> creer(@Valid @RequestBody EmployeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeService.creer(dto));
    }

    @Operation(summary = "Modifier un employé")
    @ApiResponse(responseCode = "200", description = "Employé modifié")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Employé introuvable")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeDTO> update(@Parameter(description = "Identifiant de l'employé") @PathVariable Long id,
                                            @Valid @RequestBody EmployeDTO dto) {
        return ResponseEntity.ok(employeService.update(id, dto));
    }

    @Operation(summary = "Supprimer un employé")
    @ApiResponse(responseCode = "204", description = "Employé supprimé")
    @ApiResponse(responseCode = "404", description = "Employé introuvable")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "Identifiant de l'employé") @PathVariable Long id) {
        employeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Affecter un employé à une agence")
    @ApiResponse(responseCode = "200", description = "Employé affecté avec succès")
    @ApiResponse(responseCode = "404", description = "Employé ou agence introuvable")
    @PutMapping("/{employeId}/affecter-agence/{agenceId}")
    public ResponseEntity<Void> affecterEmployeAAgence(
            @Parameter(description = "Identifiant de l'employé") @PathVariable Long employeId,
            @Parameter(description = "Identifiant de l'agence") @PathVariable Long agenceId) {
        employeService.affecterEmployeAAgence(employeId, agenceId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Désaffecter un employé de son agence")
    @ApiResponse(responseCode = "200", description = "Employé désaffecté avec succès")
    @ApiResponse(responseCode = "400", description = "Employé non rattaché à une agence")
    @ApiResponse(responseCode = "404", description = "Employé introuvable")
    @PutMapping("/{employeId}/desaffecter-agence")
    public ResponseEntity<Void> desaffecterEmployeDeAgence(
            @Parameter(description = "Identifiant de l'employé") @PathVariable Long employeId) {
        employeService.desaffecterEmployeDeAgence(employeId);
        return ResponseEntity.ok().build();
    }
}
