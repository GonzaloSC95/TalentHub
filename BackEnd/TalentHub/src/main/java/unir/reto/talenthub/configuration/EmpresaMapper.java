package unir.reto.talenthub.configuration;



import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import unir.reto.talenthub.dto.EmpresaDto;
import unir.reto.talenthub.entity.Empresa;
import unir.reto.talenthub.entity.Usuario;
import unir.reto.talenthub.service.UsuarioService;

@Component
public class EmpresaMapper {

    @Autowired
    private UsuarioService usuarioService;

    private final ModelMapper modelMapper = new ModelMapper();

    public EmpresaDto mapToDto(Empresa empresa) {
        EmpresaDto dto = modelMapper.map(empresa, EmpresaDto.class);

        // Asegúrate de que el usuario no sea null
        if (empresa.getUsuario() != null) {
            dto.setEmail(empresa.getUsuario().getEmail());
        }

        return dto;
    }

    public Empresa mapToEntity(EmpresaDto dto) {
        Empresa empresa = modelMapper.map(dto, Empresa.class);

        // Asociar usuario si el email está presente
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            Usuario usuario = usuarioService.findByEmail(dto.getEmail());
            empresa.setUsuario(usuario);
        }

        return empresa;
    }
}

