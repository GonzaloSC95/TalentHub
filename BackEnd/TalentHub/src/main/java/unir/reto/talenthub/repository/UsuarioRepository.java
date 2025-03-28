package unir.reto.talenthub.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import unir.reto.talenthub.entity.Rol;
import unir.reto.talenthub.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

   Usuario findByEmailAndPassword(String email, String password);
   Usuario findByEmail(String email);
   Usuario findByNombreAndApellidos(String nombre, String apellidos);

   List <Usuario> findByNombre(String nombre);
   List <Usuario> findByApellidos(String apellidos);
   List <Usuario> findByFechaRegistro(Date fechaRegistro);
   List <Usuario> findByEnabled(String enabled);
   List <Usuario> findByRol(Rol rol);

   

   @Query("SELECT u FROM Usuario u WHERE u.fechaRegistro BETWEEN :fechaRegistroInicio AND :fechaRegistroFin")
   List <Usuario>  findByFechaRegistroBetween(Date fechaRegistroInicio, Date fechaRegistroFin);
}
