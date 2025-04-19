package unir.reto.talenthub.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiOutput.Enabled;
import org.springframework.stereotype.Repository;

import unir.reto.talenthub.entity.Rol;
import unir.reto.talenthub.entity.Usuario;
import unir.reto.talenthub.repository.UsuarioRepository;

@Repository
public class UsuarioServiceImpl implements UsuarioService {

   @Autowired
   private UsuarioRepository usuarioRepository;
   
   @Override
   public Usuario findByEmailAndPassword(String email, String password) {
      return usuarioRepository.findByEmailAndPassword(email, password);
   }

   @Override
   public Usuario findByEmail(String email) {
      return usuarioRepository.findByEmail(email);
   }

   @Override
   public Usuario findByNombreAndApellidos(String nombre, String apellidos) {
      return usuarioRepository.findByNombreAndApellidos(nombre, apellidos);
   }

   @Override
   public List<Usuario> findAll() {
      return usuarioRepository.findAll();
   }

   @Override
   public List<Usuario> findByNombre(String nombre) {
      return usuarioRepository.findByNombre(nombre);
   }

   @Override
   public List<Usuario> findByApellidos(String apellidos) {
      return usuarioRepository.findByApellidos(apellidos);
   }

   @Override
   public List<Usuario> findByFechaRegistro(Date fechaRegistro) {
      return usuarioRepository.findByFechaRegistro(fechaRegistro);
   }

   @Override
   public List<Usuario> findByEnabled(Enabled enabled) {
      return usuarioRepository.findByEnabled(enabled);
   }

   @Override
   public List<Usuario> findByRol(Rol rol) {
      return usuarioRepository.findByRol(rol);
   }

   @Override
   public int save(Usuario usuario) {
      try {
         usuarioRepository.save(usuario);
         return 1;
      } catch (Exception e) {
         return 0;
      }
   }

   @Override
   public int update(Usuario usuario) {
      try{
         Usuario usuarioExistente = usuarioRepository.findById(usuario.getEmail()).orElse(null);
         if (usuarioExistente == null) {
            return 0;
         }
         usuarioRepository.save(usuario);
         return 1;
      } catch (Exception e) {
         return 0;
      }
   }

   @Override
   public int delete(Usuario usuario) {
      try{
         usuarioRepository.delete(usuario);;
         return 1;
      } catch (Exception e) {
         return 0;
      }
   }

}
