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
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.web.dto.ClientDTO;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "Gestion des clients")
public class ClientController {

    private final IClientService clientService;

    @Operation(summary = "Lister les clients")
    @ApiResponse(responseCode = "200", description = "Liste des clients")
    @GetMapping
    public ResponseEntity<List<ClientDTO>> findAll() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @Operation(summary = "Consulter un client")
    @ApiResponse(responseCode = "200", description = "Client trouvé")
    @ApiResponse(responseCode = "404", description = "Client introuvable")
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> findById(@Parameter(description = "Identifiant du client") @PathVariable Long id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @Operation(summary = "Créer un client")
    @ApiResponse(responseCode = "201", description = "Client créé")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @PostMapping
    public ResponseEntity<ClientDTO> creer(@Valid @RequestBody ClientDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientService.creer(dto));
    }

    @Operation(summary = "Modifier un client")
    @ApiResponse(responseCode = "200", description = "Client modifié")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Client introuvable")
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> update(@Parameter(description = "Identifiant du client") @PathVariable Long id,
                                           @Valid @RequestBody ClientDTO dto) {
        return ResponseEntity.ok(clientService.update(id, dto));
    }

    @Operation(summary = "Supprimer un client")
    @ApiResponse(responseCode = "204", description = "Client supprimé")
    @ApiResponse(responseCode = "404", description = "Client introuvable")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "Identifiant du client") @PathVariable Long id) {
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
