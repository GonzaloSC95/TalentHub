package unir.reto.talenthub.configuration;


import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import unir.reto.talenthub.dto.VacanteDto;
import unir.reto.talenthub.entity.Categoria;
import unir.reto.talenthub.entity.Destacado;
import unir.reto.talenthub.entity.Empresa;
import unir.reto.talenthub.entity.Vacante;
import unir.reto.talenthub.service.CategoriaService;
import unir.reto.talenthub.service.EmpresaService;

@Component

public class VacanteMapper {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private CategoriaService categoriaService;

    private final ModelMapper modelMapper = new ModelMapper();

    public VacanteMapper() {
        // Configurar el mapeo para que ignore el campo "destacado"
        modelMapper.addMappings(new PropertyMap<Vacante, VacanteDto>() {
            protected void configure() {
                skip(destination.isDestacado()); // Ignorar campo destacado
            }
        });
        modelMapper.addMappings(new PropertyMap<VacanteDto, Vacante>() {
            protected void configure() {
                skip(destination.getDestacado()); // Ignorar campo destacado da problemas...
            }
        });
    }

    public VacanteDto mapToDto(Vacante vacante) {
        VacanteDto dto = modelMapper.map(vacante, VacanteDto.class);
        // Mapear el campo destacado de forma personalizada
        dto.setDestacado(vacante.getDestacado() == Destacado.SI);
        if (vacante.getEmpresa() != null) {
            dto.setIdEmpresa(vacante.getEmpresa().getIdEmpresa());
            dto.setNombreEmpresa(vacante.getEmpresa().getNombreEmpresa());
        }
        if (vacante.getCategoria() != null) {
            dto.setIdCategoria(vacante.getCategoria().getIdCategoria());
        }
        return dto;
    }

    public Vacante mapToEntity(VacanteDto dto) {
        Vacante vacante = modelMapper.map(dto, Vacante.class);
        // Mapear el campo destacado de forma personalizada
        vacante.setDestacado(dto.isDestacado() ? Destacado.SI : Destacado.NO);
        
        Empresa empresa = empresaService.findByidEmpresa(dto.getIdEmpresa());
        if (empresa != null) {
            vacante.setEmpresa(empresa);
        }

        Categoria categoria = categoriaService.findByIdCategoria(dto.getIdCategoria());
        if (categoria != null) {
            vacante.setCategoria(categoria);
        }

        return vacante;
    }
}
