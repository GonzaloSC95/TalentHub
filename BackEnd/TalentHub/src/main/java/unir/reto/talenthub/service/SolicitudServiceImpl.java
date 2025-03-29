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
      return solicitudRepository.findById(idSolicitud).orElse(null);
   }

   @Override
   public List<Solicitud> findAll() {
      return solicitudRepository.findAll();
   }

   @Override
   public List<Solicitud> findByUsuario(Usuario objUsuario) {
      return solicitudRepository.findByUsuario(objUsuario);
   }

   @Override
   public List<Solicitud> findByVacante(Vacante vacante) {
      return solicitudRepository.findByVacante(vacante);
   }

   @Override
   public int save(Solicitud solicitud) {
      try {
         solicitudRepository.save(solicitud);
         return 1;
      } catch (Exception e) {
         return 0;
      }
   }

   @Override
   public int update(Solicitud solicitud) {
      try {
         Solicitud solicitudExistente = solicitudRepository.findById(solicitud.getIdSolicitud()).orElse(null);
         if (solicitudExistente == null) {
            return 0;
         }
         solicitudRepository.save(solicitud);
         return 1;
      } catch (Exception e) {
         return 0;
      }
   }

   @Override
   public int delete(Solicitud solicitud) {
      try {
         solicitudRepository.delete(solicitud);
         return 1;
      } catch (Exception e) {
         return 0;
      }
   }

}
