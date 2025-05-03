package unir.reto.talenthub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RegistroEmpresaRequestDto {

    // @Valid // Opcional: para validar los DTOs internos
    // @NotNull // Opcional: si ambos son siempre requeridos
    private UsuarioDto usuario;

    // @Valid
    // @NotNull
    private EmpresaDto empresa;

    // --- Getters y Setters ---

    public UsuarioDto getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDto usuario) {
        this.usuario = usuario;
    }

    public EmpresaDto getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaDto empresa) {
        this.empresa = empresa;
    }
}