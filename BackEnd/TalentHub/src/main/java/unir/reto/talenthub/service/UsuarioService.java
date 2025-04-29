package unir.reto.talenthub.service;

import java.util.Date;
import java.util.List;

import org.springframework.boot.ansi.AnsiOutput.Enabled;

import unir.reto.talenthub.entity.Rol;
import unir.reto.talenthub.entity.Usuario;

public interface UsuarioService {

   Usuario findByEmailAndPassword(String email, String password);
   Usuario findByEmail(String email);
   Usuario findByNombreAndApellidos(String nombre, String apellidos);

   List<Usuario> findAll();
   List <Usuario> findByNombre(String nombre);
   List <Usuario> findByApellidos(String apellidos);
   List <Usuario> findByFechaRegistro(Date fechaRegistro);
   List <Usuario> findByEnabled(Enabled enabled);
   List <Usuario> findByRol(Rol rol);
   
   
   int save(Usuario usuario);
   int update(Usuario usuario);
   int delete(Usuario usuario);
}
