package unir.reto.talenthub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import unir.reto.talenthub.entity.Empresa;
import unir.reto.talenthub.entity.Usuario;
import unir.reto.talenthub.repository.EmpresaRepository;

@Repository
public class EmpresaServiceImpl implements EmpresaService {

   @Autowired
   private EmpresaRepository empresaRepository;

   @Override
   public Empresa findByidEmpresa(int idEmpresa) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByidEmpresa'");
   }

   @Override
   public Empresa findByNombreEmpresa(String nombreEmpresa) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByNombreEmpresa'");
   }

   @Override
   public Empresa findByDireccionFiscal(String direccionFiscal) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByDireccionFiscal'");
   }

   @Override
   public Empresa findByUsuario(Usuario usuario) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByUsuario'");
   }

   @Override
   public List<Empresa> findByPais(String pais) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByPais'");
   }

   @Override
   public List<Empresa> findByNombreEmpresaContaining(String nombreEmpresa) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'findByNombreEmpresaContaining'");
   }

   @Override
   public int save(Empresa empresa) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'save'");
   }

   @Override
   public int update(Empresa empresa) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'update'");
   }

   @Override
   public int delete(Empresa empresa) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'delete'");
   }

   

}
