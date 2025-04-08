package unir.reto.talenthub.dto;

import java.io.Serializable;

import org.modelmapper.ModelMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import unir.reto.talenthub.entity.Categoria;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CategoriaDto implements Serializable{

   //SerialVersionUID
	private static final long serialVersionUID = 1L;

   //ModelMapper
	private static ModelMapper modelMapper;

   @EqualsAndHashCode.Include
   private int idCategoria;

	private String nombre;
	private String descripcion;

   //Metodo para convertir de Categoria a CategoriaDto
   public CategoriaDto mapFromEntity(Categoria categoria) {
      modelMapper = new ModelMapper();
      return modelMapper.map(categoria, CategoriaDto.class);
   }
   //Metodo para convertir de CategoriaDto a Categoria
   public Categoria mapToEntity(CategoriaDto categoriaDto) {
      modelMapper = new ModelMapper();
      return modelMapper.map(categoriaDto, Categoria.class);
   }

}
