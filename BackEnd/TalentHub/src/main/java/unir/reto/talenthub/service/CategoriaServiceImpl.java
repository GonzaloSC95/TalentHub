package unir.reto.talenthub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import unir.reto.talenthub.entity.Categoria;
import unir.reto.talenthub.repository.CategoriaRepository;

@Repository
public class CategoriaServiceImpl implements CategoriaService {

   @Autowired
   private CategoriaRepository categoriaRepository;

   @Override
   public Categoria findByIdCategoria(int idCategoria) {
      return categoriaRepository.findById(idCategoria).orElse(null);
   }

   @Override
   public Categoria findByNombre(String nombre) {
      return categoriaRepository.findByNombre(nombre);
   }

   @Override
   public List<Categoria> findAll() {
      return categoriaRepository.findAll();
   }

   @Override
   public List<Categoria> findByNombreContaining(String nombre) {
      return categoriaRepository.findByNombreContaining(nombre);
   }

   @Override
   public List<Categoria> findByDescripcionContaining(String descripcion) {
      return categoriaRepository.findByDescripcionContaining(descripcion);
   }

   @Override
   public int save(Categoria categoria) {
      try {
         categoriaRepository.save(categoria);
         return 1;
      } catch (Exception e) {
         e.printStackTrace();
         return 0;
      }
   }

   @Override
   public int update(Categoria categoria) {
      try {
         Categoria categoriaExistente = categoriaRepository.findById(categoria.getIdCategoria()).orElse(null);
         if (categoriaExistente == null) {
            return 0;
         }
         categoriaRepository.save(categoria);
         return 1;
      } catch (Exception e) {
         e.printStackTrace();
         return 0;
      }
   }

   @Override
   public int delete(Categoria categoria) {
      try {
         categoriaRepository.delete(categoria);
         return 1;
      } catch (Exception e) {
         e.printStackTrace();
         return 0;
      }
   }
}
