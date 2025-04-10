package unir.reto.talenthub.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="solicitudes")
public class Solicitud implements Serializable{

	private static final long serialVersionUID = 1L;
		
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_solicitud")
	private int idSolicitud;
	@Temporal(TemporalType.DATE)
	private Date fecha;
	private String archivo;
	private String comentarios;
	private int estado = 1;
	private String curriculum;
	
	@ManyToOne
	@JoinColumn(name="id_vacante")
	private Vacante vacante;
	
	@ManyToOne
	@JoinColumn(name="email")
	private Usuario usuario;


}
