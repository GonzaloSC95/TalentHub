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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import unir.reto.talenthub.dto.VacanteDto;
import unir.reto.talenthub.entity.Vacante;
import unir.reto.talenthub.service.VacanteService;

/**
 * Controlador REST para gestionar las vacantes.
 * http://localhost:8085/swagger-ui/index.html.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("talenthub/api/vacante")
public class VacanteController {

   @Autowired
   private VacanteService vacanteService;

   @Operation(
      summary = "Obtener vacante por ID.",
      description = "Devuelve una vacante por su ID."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Vacante obtenida"),
      @ApiResponse(responseCode = "404", description = "Vacante no encontrada")
   })
   @GetMapping("/{id}")
   public ResponseEntity<VacanteDto> getVacanteById(@PathVariable int id) {
      VacanteDto vacanteDto = new VacanteDto();
      Vacante vacante = vacanteService.findByIdVacante(id);
      if (vacante != null) {
         return ResponseEntity.status(HttpStatus.OK).body(vacanteDto.mapFromEntity(vacante));
      } else {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @Operation(
      summary = "Obtener todas las vacantes.",
      description = "Devuelve todas las vacantes."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Vacantes obtenidas")
   })
   @GetMapping("/all")
   public ResponseEntity<List<VacanteDto>> getAllVacantes() {
      List<VacanteDto> vacantes = vacanteService.findAll().stream()
            .map(vacante -> new VacanteDto().mapFromEntity(vacante))
            .toList();
      return ResponseEntity.ok(vacantes);
   }

   @Operation(
      summary = "Crear vacante.",
      description = "Devuelve la vacante creada."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Vacante creada"),
      @ApiResponse(responseCode = "400", description = "Error al crear la vacante")
   })
   @PostMapping("/crear")
   public ResponseEntity<VacanteDto> crearVacante(@RequestBody VacanteDto vacante) {
      int result = vacanteService.save(vacante.mapToEntity(vacante));
      if (result == 1) {
         return ResponseEntity.status(HttpStatus.CREATED).body(vacante);
      } else {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
   }

   @Operation(
      summary = "Actualizar vacante.",
      description = "Devuelve la vacante actualizada."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Vacante actualizada"),
      @ApiResponse(responseCode = "400", description = "Error al actualizar la vacante")
   })
   @PutMapping("/actualizar")
   public ResponseEntity<VacanteDto> updateVacante(@RequestBody VacanteDto vacante) {
      int result = vacanteService.update(vacante.mapToEntity(vacante));
      if (result == 1) {
         return ResponseEntity.status(HttpStatus.OK).body(vacante);
      } else {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
   }

   @Operation(
      summary = "Eliminar vacante.",
      description = "Devuelve la vacante eliminada."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Vacante eliminada"),
      @ApiResponse(responseCode = "400", description = "Error al eliminar la vacante")
   })
   @DeleteMapping("/eliminar")
   public ResponseEntity<VacanteDto> deleteVacante(@RequestBody VacanteDto vacante) {
      int result = vacanteService.delete(vacante.mapToEntity(vacante));
      if (result == 1) {
         return ResponseEntity.status(HttpStatus.OK).body(vacante);
      } else {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
   }

}
