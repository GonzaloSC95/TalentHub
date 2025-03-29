package unir.reto.talenthub.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@EqualsAndHashCode.Include
	private String email;
	
	private String nombre;
	private String apellidos;

	private String password;
	
	@Enumerated(EnumType.ORDINAL)
	private Enabled enabled = Enabled.ENABLED;
	
	@Column(name="fecha_Registro")
	@Temporal(TemporalType.DATE)
	private Date fechaRegistro;
	
	@Enumerated(EnumType.STRING)
	private Rol rol;

	@OneToOne(mappedBy = "usuario")
   private Empresa empresa;

	@OneToMany(mappedBy = "usuario")
   private List<Solicitud> solicitudes;


}
