package unir.reto.talenthub.service;

import java.util.List;

import unir.reto.talenthub.entity.Categoria;

public interface CategoriaService {

   Categoria findByIdCategoria(int idCategoria);
   Categoria findByNombre(String nombre);

   List<Categoria> findAllCategorias();
   List<Categoria> findByNombreContaining(String nombre);
   List<Categoria> findByDescripcionContaining(String descripcion);


   int save(Categoria categoria);
   int update(Categoria categoria);
   int delete(Categoria categoria);



}
