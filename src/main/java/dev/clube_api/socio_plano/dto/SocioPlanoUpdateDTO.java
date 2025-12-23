package dev.clube_api.socio_plano.dto;

import dev.clube_api.socio_plano.enums.StatusSocioPlano;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocioPlanoUpdateDTO {

    @NotNull
    private StatusSocioPlano status;
}
