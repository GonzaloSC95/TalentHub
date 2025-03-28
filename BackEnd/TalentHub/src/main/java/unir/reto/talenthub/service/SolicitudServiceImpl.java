package unir.reto.talenthub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import unir.reto.talenthub.entity.Solicitud;
import unir.reto.talenthub.entity.Usuario;
import unir.reto.talenthub.entity.Vacante;
import unir.reto.talenthub.repository.SolicitudRepository;

@Repository
public class SolicitudServiceImpl implements SolicitudService {

   @Autowired
   private SolicitudRepository solicitudRepository;

   @Override
   public Solicitud findByIdSolicitud(int idSolicitud) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByIdSolicitud'");
   }

   @Override
   public List<Solicitud> findByUsuario(Usuario objUsuario) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByUsuario'");
   }

   @Override
   public List<Solicitud> findByVacante(Vacante vacante) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByVacante'");
   }

   @Override
   public int save(Solicitud solicitud) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'save'");
   }

   @Override
   public int update(Solicitud solicitud) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'update'");
   }

   @Override
   public int delete(Solicitud solicitud) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'delete'");
   }
  

}
