package unir.reto.talenthub.entity;



import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="empresas")
public class Empresa implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_empresa")
	private int isEmpresa;
	
	private String cif;/* unique*/
	
	@Column(name="nombre_empresa") 
	private String nombreEmpresa;
	
	@Column(name="direccion_fiscal") 
	private String direccionFiscal;
	
 
	private String pais;
	
	@OneToOne
	@JoinColumn(name="email")
	private Usuario usuario;


}
