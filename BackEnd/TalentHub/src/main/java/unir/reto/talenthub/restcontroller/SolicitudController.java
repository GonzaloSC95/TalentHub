package unir.reto.talenthub.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import unir.reto.talenthub.entity.Solicitud;
import unir.reto.talenthub.service.SolicitudService;

/**
 * Controlador REST para gestionar las solicitudes.
 * http://localhost:8085/swagger-ui/index.html.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("talenthub/api/solicitud")
public class SolicitudController {

   @Autowired
   private SolicitudService solicitudService;

   @Operation(
      summary = "Buscar solicitud por ID",
      description = "Este endpoint permite buscar una solicitud por su ID."
   )
   @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
      @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
   })
   @GetMapping("/{id}")
   public ResponseEntity<Solicitud> getSolicitudById(int id) {
      Solicitud solicitud = solicitudService.findByIdSolicitud(id);
      if (solicitud != null) {
         return ResponseEntity.ok(solicitud);
      } else {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @Operation(
      summary = "Listar todas las solicitudes",
      description = "Este endpoint permite listar todas las solicitudes."
   )
   @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Solicitudes encontradas")
   })
   @GetMapping("/all")
   public ResponseEntity<List<Solicitud>> getAllSolicitudes() {
      return ResponseEntity.ok(solicitudService.findAll());
   }

   @Operation(
      summary = "Crear una nueva solicitud",
      description = "Este endpoint permite crear una nueva solicitud."
   )
   @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Solicitud creada"),
      @ApiResponse(responseCode = "400", description = "Error al crear la solicitud")
   })
   @PostMapping("/crear")
   public ResponseEntity<Solicitud> crearSolicitud(@RequestBody Solicitud solicitud) {
      int result = solicitudService.save(solicitud);
      if (result != 0) {
         return ResponseEntity.status(HttpStatus.CREATED).body(solicitud);
      } else {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
   }

   @Operation(
      summary = "Actualizar una solicitud",
      description = "Este endpoint permite actualizar una solicitud existente."
   )
   @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Solicitud actualizada"),
      @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
   })
   @PutMapping("/actualizar")
   public ResponseEntity<Solicitud> actualizarSolicitud(@RequestBody Solicitud solicitud) {
      int result = solicitudService.update(solicitud);
      if (result != 0) {
         return ResponseEntity.ok(solicitud);
      } else {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @Operation(
      summary = "Eliminar una solicitud",
      description = "Este endpoint permite eliminar una solicitud."
   )
   @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Solicitud eliminada"),
      @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
   })
   @DeleteMapping("/eliminar")
   public ResponseEntity<Solicitud> eliminarSolicitud(@RequestBody Solicitud solicitud) {
      int result = solicitudService.delete(solicitud);
      if (result != 0) {
         return ResponseEntity.ok(solicitud);
      } else {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

}
