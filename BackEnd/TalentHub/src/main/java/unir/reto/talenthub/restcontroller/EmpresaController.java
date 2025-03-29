package unir.reto.talenthub.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import unir.reto.talenthub.entity.Empresa;
import unir.reto.talenthub.entity.Estatus;
import unir.reto.talenthub.entity.Vacante;
import unir.reto.talenthub.service.EmpresaService;
import unir.reto.talenthub.service.VacanteService;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("talenthub/api/empresa")
public class EmpresaController {

   @Autowired
   private EmpresaService empresaService;

   @Autowired
   private VacanteService vacanteService;

   @Operation(
      summary = "Obtener una empresa por su id.",
      description = "Devuelve la empresa por su id."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Empresa encontrada."),
      @ApiResponse(responseCode = "404", description = "Empresa no encontrada.")
   })
   @GetMapping("/{id}")
   public ResponseEntity<Empresa> getEmpresa(@PathVariable int id) {
      Empresa empresa = empresaService.findByidEmpresa(id);
      if (empresa == null) {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
      return ResponseEntity.ok(empresa);
   }

   @Operation(
      summary = "Obtener empresas.",
      description = "Devuelve todas las empresas."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de empresas obtenida."),
   })
   @GetMapping("/all")
   public ResponseEntity<List<Empresa>> getEmpresas() {
      return ResponseEntity.ok(empresaService.findAll());
   }

   @Operation(
      summary = "Crear empresa.",
      description = "Devuelve la empresa creada."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Empresa creada."),
      @ApiResponse(responseCode = "400", description = "Empresa no creada.")
   })
   @PostMapping("/crear")
   public ResponseEntity<Empresa> crearEmpresa(@RequestBody Empresa empresa) {
      int empresaCreadaNm = empresaService.save(empresa);
      if (empresaCreadaNm == 0) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
      else{
         return ResponseEntity.status(HttpStatus.CREATED).body(empresa);
      }
   }

   @Operation(
      summary = "Actualizar empresa.",
      description = "Devuelve la empresa actualizada."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Empresa actualizada."),
      @ApiResponse(responseCode = "404", description = "Empresa no encontrada.")
   })
   @PutMapping("/actualizar/{id}")
   public ResponseEntity<Empresa> actualizarEmpresa(@PathVariable int id, @RequestBody Empresa empresaActualizada) {
      Empresa empresa = empresaService.findByidEmpresa(id);
      if (empresa == null) {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
      int empresaActualizadaNm = empresaService.update(empresa);
      if (empresaActualizadaNm == 0) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
      return ResponseEntity.ok(empresaActualizada);
   }

   @Operation(
      summary = "Eliminar empresa.",
      description = "Elimina la empresa."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Empresa eliminada."),
      @ApiResponse(responseCode = "404", description = "Empresa no encontrada.")
   })
   @DeleteMapping("/eliminar/{id}")
   public ResponseEntity<Empresa> eliminarEmpresa(@PathVariable int id) {
      Empresa empresa = empresaService.findByidEmpresa(id);
      if (empresa == null) {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
      int empresaEliminadaNm = empresaService.delete(empresa);
      if (empresaEliminadaNm == 0) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
      return ResponseEntity.ok(empresa);
   }
   
   @Operation(
      summary = "Publicar una nueva vacante",
      description = "Crea una nueva vacante con estado 'CREADA'."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Vacante creada"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida")
   })
   @PostMapping("/publicar/vacante")
   public ResponseEntity<Vacante> publicarVacante(@RequestBody Vacante vacante) {
      vacante.setEstatus(Estatus.CREADA);
      int vacanteCreadaNm = vacanteService.save(vacante);
      if (vacanteCreadaNm == 0) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
      else{
         return ResponseEntity.status(HttpStatus.CREATED).body(vacante);
      }
   }

   @Operation(
      summary = "Gestionar una vacante",
      description = "Permite cancelar una vacante (estado 'CANCELADA'), sin eliminarla de la base de datos."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Vacante cancelada"),
      @ApiResponse(responseCode = "404", description = "Vacante no encontrada")
   })
   @PutMapping("/cancelar/vacante/{id}")
   public ResponseEntity<Vacante> cancelarVacante(@PathVariable int id) {
      Vacante vacante = vacanteService.findByIdVacante(id);
      if (vacante == null) {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
      vacante.setEstatus(Estatus.CANCELADA);
      vacanteService.update(vacante);
      return ResponseEntity.ok(vacante);
   }

}
