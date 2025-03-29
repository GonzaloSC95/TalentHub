package unir.reto.talenthub.service;

import java.util.List;

import unir.reto.talenthub.entity.Solicitud;
import unir.reto.talenthub.entity.Usuario;
import unir.reto.talenthub.entity.Vacante;

public interface SolicitudService {

   Solicitud findByIdSolicitud(int idSolicitud);

   List<Solicitud> findAll();
   List<Solicitud> findByUsuario(Usuario objUsuario);
   List<Solicitud> findByVacante(Vacante vacante);

   int save(Solicitud solicitud);
   int update(Solicitud solicitud);
   int delete(Solicitud solicitud);
}
