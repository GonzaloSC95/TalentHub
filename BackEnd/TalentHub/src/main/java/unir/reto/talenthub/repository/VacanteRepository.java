package unir.reto.talenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import unir.reto.talenthub.entity.Categoria;
import unir.reto.talenthub.entity.Destacado;
import unir.reto.talenthub.entity.Empresa;
import unir.reto.talenthub.entity.Estatus;
import unir.reto.talenthub.entity.Vacante;
import java.util.List;


public interface VacanteRepository extends JpaRepository<Vacante, Integer> {
   
   List <Vacante> findByDestacado(Destacado destacado);
   List <Vacante> findByEstatus(Estatus estatus);
   List <Vacante> findByEstatusAndDestacado(Estatus estatus, int destacado);

   List <Vacante> findByNombreContaining(String nombre);
   List <Vacante> findByDescripcionContaining(String descripcion);
   List <Vacante> findByCategoria(Categoria categoria);
   List <Vacante> findByEmpresa(Empresa empresa);


}
