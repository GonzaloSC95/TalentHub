package unir.reto.talenthub.dto;

import java.io.Serializable;
import java.util.Date;

import org.modelmapper.ModelMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import unir.reto.talenthub.entity.Usuario;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UsuarioDto implements Serializable{
   //SerialVersionUID
	private static final long serialVersionUID = 1L;

   //ModelMapper
	private static ModelMapper modelMapper;

   @EqualsAndHashCode.Include
   private String email;
   private String nombre;
   private String apellidos;
   private String password;
   private int enabled;
   private Date fechaRegistro;
   private String rol;

   //Metodo para convertir de Usuario a UsuarioDto
   public UsuarioDto mapFromEntity(Usuario usuario) {
      modelMapper = new ModelMapper();
      return modelMapper.map(usuario, UsuarioDto.class);
   }
   //Metodo para convertir de UsuarioDto a Usuario
   public Usuario mapToEntity(UsuarioDto usuarioDto) {
      modelMapper = new ModelMapper();
      return modelMapper.map(usuarioDto, Usuario.class);
   }
}
