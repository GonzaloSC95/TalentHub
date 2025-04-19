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

	

   @EqualsAndHashCode.Include
   private String email;
   private String nombre;
   private String apellidos;
   private String password;
   private int enabled;
   private Date fechaRegistro;
   private String rol;
   private Integer empresaId;     // Para envío y persistencia
   private String nombreEmpresa;  // Solo para mostrar
   

   // Métodos getter y setter para empresa
   public Integer getEmpresaId() {
       return empresaId;
   }

   public void setEmpresaId(Integer empresaId) {
       this.empresaId = empresaId;
   }

   public String getNombreEmpresa() {
       return nombreEmpresa;
   }

   public void setNombreEmpresa(String nombreEmpresa) {
       this.nombreEmpresa = nombreEmpresa;
   }

   // Método para mapear desde Usuario a UsuarioDto
   public UsuarioDto mapFromEntity(Usuario usuario) {
       ModelMapper modelMapper = new ModelMapper();
       return modelMapper.map(usuario, UsuarioDto.class);
   }
   public Usuario mapToEntity(UsuarioDto usuarioDto) {
	    ModelMapper modelMapper = new ModelMapper();
	    Usuario usuario = modelMapper.map(usuarioDto, Usuario.class);
	    
	    // porque Usuario no tiene una referencia a Empresa en la entidad.

	    return usuario;
	}






}
