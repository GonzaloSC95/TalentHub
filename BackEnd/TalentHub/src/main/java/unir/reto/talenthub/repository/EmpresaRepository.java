package unir.reto.talenthub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import unir.reto.talenthub.entity.Empresa;
import unir.reto.talenthub.entity.Usuario;


public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
   
   Empresa findByNombreEmpresa(String nombreEmpresa);
   Empresa findByDireccionFiscal(String direccionFiscal);
   Empresa findByUsuario(Usuario usuario);

   List <Empresa> findByPais(String pais);
   List <Empresa> findByNombreEmpresaContaining(String nombreEmpresa);
   

}
