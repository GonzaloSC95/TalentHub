package unir.reto.talenthub.entities;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="usuarios")
public class Usuario implements Serializable{
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

			@Id
			private String email;
			
			private String nombre;
			private String apellidos;
			private String password;
			
			@Enumerated(EnumType.ORDINAL)
			private Enabled enabled = Enabled.ENABLED; /* DEFAULT 1*/
			
	  		@Column(name="fecha_Registro")
	  		@Temporal(TemporalType.DATE)
	  		private Date fechaRegistro;
	  		
	  		@Enumerated(EnumType.STRING)
	  		private Rol rol;


}
