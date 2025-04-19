package unir.reto.talenthub.dto;

import java.io.Serializable;
import java.util.Date;

import org.modelmapper.ModelMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import unir.reto.talenthub.entity.Vacante;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VacanteDto implements Serializable{
   //SerialVersionUID
	private static final long serialVersionUID = 1L;



   @EqualsAndHashCode.Include
   private int idVacante;
   private String nombre;
   private String descripcion;
   private Date fecha;
   private double salario;
   private String estatus;
   private boolean destacado;
   private String imagen;
   private String detalles;
   private int idCategoria;
   private int idEmpresa;
   private String nombreEmpresa; 


}
