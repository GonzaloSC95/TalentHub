package unir.reto.talenthub.dto;

import java.io.Serializable;

import org.modelmapper.ModelMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import unir.reto.talenthub.entity.Empresa;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EmpresaDto implements Serializable{
   //SerialVersionUID
	private static final long serialVersionUID = 1L;

   //ModelMapper
	private static ModelMapper modelMapper;

   @EqualsAndHashCode.Include
   private int idEmpresa;
   private String cif;
   private String nombreEmpresa;
   private String direccionFiscal;
   private String pais;
   private String usuario;

   //Metodo para convertir de Empresa a EmpresaDto
   public EmpresaDto mapFromEntity(Empresa empresa) {
      modelMapper = new ModelMapper();
      modelMapper.typeMap(Empresa.class, EmpresaDto.class)
      .addMapping(src -> src.getUsuario().getEmail(), EmpresaDto::setUsuario);
      return modelMapper.map(empresa, EmpresaDto.class);
   }
   //Metodo para convertir de EmpresaDto a Empresa
   public Empresa mapToEntity(EmpresaDto empresaDto) {
      modelMapper = new ModelMapper();
      return modelMapper.map(empresaDto, Empresa.class);
   }
}
