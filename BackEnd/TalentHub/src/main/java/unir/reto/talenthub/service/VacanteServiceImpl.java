package unir.reto.talenthub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import unir.reto.talenthub.entity.Categoria;
import unir.reto.talenthub.entity.Destacado;
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
      return vacanteRepository.findById(idVacante).orElse(null);
   }

   @Override
   public List<Vacante> findByDestacado(Destacado destacado) {
      return vacanteRepository.findByDestacado(destacado);
   }

   @Override
   public List<Vacante> findByEstatus(Estatus estatus) {
      return vacanteRepository.findByEstatus(estatus);
   }

   @Override
   public List<Vacante> findByEstatusAndDestacado(Estatus estatus, int destacado) {
      return vacanteRepository.findByEstatusAndDestacado(estatus, destacado);
   }

   @Override
   public List<Vacante> findByNombreContaining(String nombre) {
      return vacanteRepository.findByNombreContaining(nombre);
   }

   @Override
   public List<Vacante> findByDescripcionContaining(String descripcion) {
      return vacanteRepository.findByDescripcionContaining(descripcion);
   }

   @Override
   public List<Vacante> findByCategoria(Categoria categoria) {
      return vacanteRepository.findByCategoria(categoria);
   }

   @Override
   public List<Vacante> findByEmpresa(Empresa empresa) {
      return vacanteRepository.findByEmpresa(empresa);
   }

   @Override
   public int save(Vacante vacante) {
      try {
         vacanteRepository.save(vacante);
         return 1;
      } catch (Exception e) {
         e.printStackTrace();
         return 0;
      }
   }

   @Override
   public int update(Vacante vacante) {
      try {
         Vacante vacanteExistente = vacanteRepository.findById(vacante.getIdVacante()).orElse(null);
         if (vacanteExistente == null) {
            return 0;
         }
         vacanteRepository.save(vacante);
         return 1;
      } catch (Exception e) {
         e.printStackTrace();
         return 0;
      }
   }

   @Override
   public int delete(Vacante vacante) {
      try {
         vacanteRepository.delete(vacante);
         return 1;
      } catch (Exception e) {
         e.printStackTrace();
         return 0;
      }
   }
}
