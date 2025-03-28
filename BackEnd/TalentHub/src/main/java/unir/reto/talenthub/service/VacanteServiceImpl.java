package unir.reto.talenthub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import unir.reto.talenthub.entity.Categoria;
import unir.reto.talenthub.entity.Empresa;
import unir.reto.talenthub.entity.Estatus;
import unir.reto.talenthub.entity.Vacante;
import unir.reto.talenthub.repository.VacanteRepository;

@Repository
public class VacanteServiceImpl implements VacanteService {

   @Autowired
   private VacanteRepository vacanteRepository;
   
   @Override
   public Vacante findByIdVacante(int idVacante) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByIdVacante'");
   }

   @Override
   public List<Vacante> findByDestacado(int destacado) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByDestacado'");
   }

   @Override
   public List<Vacante> findByEstatus(Estatus estatus) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByEstatus'");
   }

   @Override
   public List<Vacante> findByEstatusAndDestacado(Estatus estatus, int destacado) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByEstatusAndDestacado'");
   }

   @Override
   public List<Vacante> findByNombreContaining(String nombre) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByNombreContaining'");
   }

   @Override
   public List<Vacante> findByDescripcionContaining(String descripcion) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByDescripcionContaining'");
   }

   @Override
   public List<Vacante> findByCategoria(Categoria categoria) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByCategoria'");
   }

   @Override
   public List<Vacante> findByIdEmpresa(Empresa empresa) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByIdEmpresa'");
   }

   @Override
   public int save(Vacante vacante) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'save'");
   }

   @Override
   public int update(Vacante vacante) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'update'");
   }

   @Override
   public int delete(Vacante vacante) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'delete'");
   }
    
}
