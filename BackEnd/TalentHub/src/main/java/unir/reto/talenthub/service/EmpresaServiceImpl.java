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
      return empresaRepository.findById(idEmpresa).orElse(null);
   }

   @Override
   public List<Empresa> findAll() {
      return empresaRepository.findAll();
   }

   @Override
   public Empresa findByNombreEmpresa(String nombreEmpresa) {
      return empresaRepository.findByNombreEmpresa(nombreEmpresa);
   }

   @Override
   public Empresa findByDireccionFiscal(String direccionFiscal) {
      return empresaRepository.findByDireccionFiscal(direccionFiscal);
   }

   @Override
   public Empresa findByUsuario(Usuario usuario) {
      return empresaRepository.findByUsuario(usuario);
   }

   @Override
   public List<Empresa> findByPais(String pais) {
      return empresaRepository.findByPais(pais);
   }

   @Override
   public List<Empresa> findByNombreEmpresaContaining(String nombreEmpresa) {
      return empresaRepository.findByNombreEmpresaContaining(nombreEmpresa);
   }

   @Override
   public int save(Empresa empresa) {
      try {
         empresaRepository.save(empresa);
         return 1;
      } catch (Exception e) {
         e.printStackTrace();
         return 0;
      }
   }

   @Override
   public int update(Empresa empresa) {
      try {
         Empresa empresaExistente = empresaRepository.findById(empresa.getIdEmpresa()).orElse(null);
         if (empresaExistente == null) {
            return 0;
         }
         empresaRepository.save(empresa);
         return 1;
      } catch (Exception e) {
         e.printStackTrace();
         return 0;
      }
   }

   @Override
   public int delete(Empresa empresa) {
      try {
         empresaRepository.delete(empresa);
         return 1;
      } catch (Exception e) {
         e.printStackTrace();
         return 0;
      }
   }

}
