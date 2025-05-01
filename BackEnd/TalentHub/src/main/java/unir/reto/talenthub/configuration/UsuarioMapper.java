package unir.reto.talenthub.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import unir.reto.talenthub.dto.UsuarioDto;
import unir.reto.talenthub.entity.Empresa;
import unir.reto.talenthub.entity.Usuario;
import unir.reto.talenthub.service.EmpresaService;

@Component
public class UsuarioMapper {

    @Autowired
    private EmpresaService empresaService;

    private final ModelMapper modelMapper = new ModelMapper();

    public UsuarioDto mapWithEmpresa(Usuario usuario) {
        UsuarioDto dto = modelMapper.map(usuario, UsuarioDto.class);
        Empresa empresa = empresaService.findByUsuarioEmail(usuario.getEmail());
        if (empresa != null) {
            dto.setEmpresaId(empresa.getIdEmpresa());
            dto.setNombreEmpresa(empresa.getNombreEmpresa());
        }
        return dto;
    }

    public Usuario mapToEntity(UsuarioDto dto) {
        return modelMapper.map(dto, Usuario.class);
    }
    
}

