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
}
