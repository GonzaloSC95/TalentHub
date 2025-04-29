package unir.reto.talenthub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import unir.reto.talenthub.entity.Solicitud;
import unir.reto.talenthub.entity.Usuario;
import unir.reto.talenthub.entity.Vacante;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {
   
   List<Solicitud> findByUsuario(Usuario objUsuario);
   List<Solicitud> findByVacante(Vacante vacante);
   Solicitud findByVacanteAndUsuario(Vacante vacante, Usuario usuario);
   List<Solicitud> findByEstado(int estado);
   
// Nuevo método para filtrar por estado y por el email del usuario
   List<Solicitud> findByEstadoAndUsuarioEmail(int estado, String email);
   
}
