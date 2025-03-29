package unir.reto.talenthub.service;

import java.util.List;

import unir.reto.talenthub.entity.Categoria;
import unir.reto.talenthub.entity.Destacado;
import unir.reto.talenthub.entity.Empresa;
import unir.reto.talenthub.entity.Estatus;
import unir.reto.talenthub.entity.Vacante;

public interface VacanteService {

   Vacante findByIdVacante(int idVacante);
   
   List <Vacante> findAll();
   List <Vacante> findByDestacado(Destacado destacado);
   List <Vacante> findByEstatus(Estatus estatus);
   List <Vacante> findByEstatusAndDestacado(Estatus estatus, int destacado);

   List <Vacante> findByNombreContaining(String nombre);
   List <Vacante> findByDescripcionContaining(String descripcion);
   List <Vacante> findByCategoria(Categoria categoria);
   List <Vacante> findByEmpresa(Empresa empresa);

   int save(Vacante vacante);
   int update(Vacante vacante);
   int delete(Vacante vacante);
}
