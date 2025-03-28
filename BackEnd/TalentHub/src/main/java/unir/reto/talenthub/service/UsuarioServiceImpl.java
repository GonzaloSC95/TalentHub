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
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByEmailAndPassword'");
   }

   @Override
   public Usuario findByEmail(String email) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByEmail'");
   }

   @Override
   public Usuario findByNombreAndApellidos(String nombre, String apellidos) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByNombreAndApellidos'");
   }

   @Override
   public List<Usuario> findByNombre(String nombre) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByNombre'");
   }

   @Override
   public List<Usuario> findByApellidos(String apellidos) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByApellidos'");
   }

   @Override
   public List<Usuario> findByFechaRegistro(Date fechaRegistro) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByFechaRegistro'");
   }

   @Override
   public List<Usuario> findByEnabled(Enabled enabled) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByEnabled'");
   }

   @Override
   public List<Usuario> findByRol(Rol rol) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByRol'");
   }

   @Override
   public int save(Usuario usuario) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'save'");
   }

   @Override
   public int update(Usuario usuario) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'update'");
   }

   @Override
   public int delete(Usuario usuario) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'delete'");
   }
    

}
