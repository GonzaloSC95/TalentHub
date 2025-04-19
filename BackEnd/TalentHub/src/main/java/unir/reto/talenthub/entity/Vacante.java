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
@Table(name="vacantes")
public class Vacante implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_vacante")
	private int idVacante;
	private String nombre;
   private String descripcion;
	@Temporal(TemporalType.DATE)
	private Date  fecha;
	private double salario;
	@Enumerated(EnumType.STRING)
	private Estatus estatus;
	@Enumerated(EnumType.ORDINAL)
	private Destacado destacado = Destacado.NO;
	private String  imagen;
	private String  detalles;
	
	@ManyToOne
	@JoinColumn(name="id_Categoria")
	private Categoria categoria;
	
	@ManyToOne
	@JoinColumn(name="id_empresa")
	private Empresa empresa;
}
