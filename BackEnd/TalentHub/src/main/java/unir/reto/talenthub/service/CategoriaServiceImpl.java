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
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByIdCategoria'");
   }

   @Override
   public Categoria findByNombre(String nombre) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByNombre'");
   }

   @Override
   public List<Categoria> findAllCategorias() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findAllCategorias'");
   }

   @Override
   public List<Categoria> findByNombreContaining(String nombre) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByNombreContaining'");
   }

   @Override
   public List<Categoria> findByDescripcionContaining(String descripcion) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByDescripcionContaining'");
   }

   @Override
   public int save(Categoria categoria) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'save'");
   }

   @Override
   public int update(Categoria categoria) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'update'");
   }

   @Override
   public int delete(Categoria categoria) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'delete'");
   }
   
   


}
