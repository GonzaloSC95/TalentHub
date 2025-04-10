package unir.reto.talenthub.dto;

import java.io.Serializable;
import java.util.Date;

import org.modelmapper.ModelMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import unir.reto.talenthub.entity.Solicitud;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SolicitudDto implements Serializable{
   //SerialVersionUID
	private static final long serialVersionUID = 1L;

   //ModelMapper
	private static ModelMapper modelMapper;
   
   @EqualsAndHashCode.Include
   private int idSolicitud;
   private Date fecha;
   private String archivo;
   private String comentarios;
   private String estado;
   private String curriculum;
   private int idVacante;
   private String emailUsuario;

   //Metodo para convertir de Solicitud a SolicitudDto
   public SolicitudDto mapFromEntity(Solicitud solicitud) {
      modelMapper = new ModelMapper();
      return modelMapper.map(solicitud, SolicitudDto.class);
   }
   //Metodo para convertir de SolicitudDto a Solicitud
   public Solicitud mapToEntity(SolicitudDto solicitudDto) {
      modelMapper = new ModelMapper();
      return modelMapper.map(solicitudDto, Solicitud.class);
   }
}
