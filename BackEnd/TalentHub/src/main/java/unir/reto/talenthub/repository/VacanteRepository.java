package unir.reto.talenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import unir.reto.talenthub.entity.Categoria;
import unir.reto.talenthub.entity.Empresa;
import unir.reto.talenthub.entity.Estatus;
import unir.reto.talenthub.entity.Vacante;
import java.util.List;


public interface VacanteRepository extends JpaRepository<Vacante, Integer> {
   
   Vacante findByIdVacante(int idVacante);
   List <Vacante> findByDestacado(int destacado);
   List <Vacante> findByEstatus(Estatus estatus);
   List <Vacante> findByEstatusAndDestacado(Estatus estatus, int destacado);

   List <Vacante> findByNombreContaining(String nombre);
   List <Vacante> findByDescripcionContaining(String descripcion);
   List <Vacante> findByCategoria(Categoria categoria);
   List <Vacante> findByIdEmpresa(Empresa empresa);


}
