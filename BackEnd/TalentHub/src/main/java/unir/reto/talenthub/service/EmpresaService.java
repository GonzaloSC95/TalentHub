package unir.reto.talenthub.service;

import java.util.List;

import unir.reto.talenthub.entity.Empresa;
import unir.reto.talenthub.entity.Usuario;

public interface EmpresaService {

   Empresa findByidEmpresa(int idEmpresa);
   Empresa findByNombreEmpresa(String nombreEmpresa);
   Empresa findByDireccionFiscal(String direccionFiscal);
   Empresa findByUsuario(Usuario usuario);
   Empresa findByUsuarioEmail(String email);


   List <Empresa> findAll();
   List <Empresa> findByPais(String pais);
   List <Empresa> findByNombreEmpresaContaining(String nombreEmpresa);

   int save(Empresa empresa);
   int update(Empresa empresa);
   int delete(Empresa empresa);

}
