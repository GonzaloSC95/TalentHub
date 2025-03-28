package unir.reto.talenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import unir.reto.talenthub.entity.Categoria;
import java.util.List;


public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{

   Categoria findByIdCategoria(int idCategoria);
   Categoria findByNombre(String nombre);

   List<Categoria> findByNombreContaining(String nombre);
   List<Categoria> findByDescripcionContaining(String descripcion);
   
}
