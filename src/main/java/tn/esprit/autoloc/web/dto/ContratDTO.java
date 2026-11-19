package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratDTO {

    private Long id;

    private LocalDate dateSignature;

    @NotNull(message = "Le montant total est obligatoire")
    @PositiveOrZero(message = "Le montant total doit être positif ou nul")
    private BigDecimal montantTotal;

    private Boolean valide;

    private Long reservationId;
}

