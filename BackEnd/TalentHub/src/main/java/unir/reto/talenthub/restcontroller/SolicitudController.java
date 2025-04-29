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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import unir.reto.talenthub.dto.SolicitudDto;
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
   public ResponseEntity<SolicitudDto> getSolicitudById(@PathVariable("id") Integer id) {
       Solicitud solicitud = solicitudService.findByIdSolicitud(id);
       if (solicitud != null) {
           SolicitudDto solicitudDto = new SolicitudDto();
           return ResponseEntity.ok(solicitudDto.mapFromEntity(solicitud));
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
   public ResponseEntity<List<SolicitudDto>> getAllSolicitudes() {
      List<SolicitudDto> solicitudesDto = solicitudService.findAll().stream()
            .map(solicitud -> new SolicitudDto().mapFromEntity(solicitud))
            .toList();
      return ResponseEntity.ok(solicitudesDto);
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
   public ResponseEntity<SolicitudDto> crearSolicitud(@RequestBody SolicitudDto solicitud) {
      int result = solicitudService.save(solicitud.mapToEntity(solicitud));
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
   public ResponseEntity<SolicitudDto> actualizarSolicitud(@RequestBody SolicitudDto solicitud) {
      int result = solicitudService.update(solicitud.mapToEntity(solicitud));
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
   public ResponseEntity<SolicitudDto> eliminarSolicitud(@RequestBody SolicitudDto solicitud) {
      int result = solicitudService.delete(solicitud.mapToEntity(solicitud));
      if (result != 0) {
         return ResponseEntity.ok(solicitud);
      } else {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }
   
   @Operation(
		      summary = "Listar todas las solicitudes presentadas, estado 0",
		      description = "Este endpoint permite listar todas las solicitudes presentadas, estado 0."
		   )
		   @ApiResponses({
		      @ApiResponse(responseCode = "200", description = "Solicitudes encontradas")
		   })
		   @GetMapping("/presentadas")
		   public ResponseEntity<List<SolicitudDto>> getSolicitudesPresentadas() {
		      List<SolicitudDto> solicitudesDto = solicitudService.findByEstadoPresentado(0).stream()
		            .map(solicitud -> new SolicitudDto().mapFromEntity(solicitud))
		            .toList();
		      return ResponseEntity.ok(solicitudesDto);
		   }
   
   @Operation(
		      summary = "Listar todas las solicitudes adjudicadas, estado 1",
		      description = "Este endpoint permite listar todas las solicitudes adjudicadas, estado 1."
		   )
		   @ApiResponses({
		      @ApiResponse(responseCode = "200", description = "Solicitudes encontradas")
		   })
		   @GetMapping("/adjudicadas")
		   public ResponseEntity<List<SolicitudDto>> getSolicitudesAdjudicadas() {
		      List<SolicitudDto> solicitudesDto = solicitudService.findByEstadoAdjudicado(1).stream()
		            .map(solicitud -> new SolicitudDto().mapFromEntity(solicitud))
		            .toList();
		      return ResponseEntity.ok(solicitudesDto);
		   }
   @Operation(
		   summary = "Listar solicitudes presentadas por usuario (estado 0)",
		   description = "Este endpoint permite listar todas las solicitudes con estado 0 (presentadas) filtrando por email de usuario."
		 )
		 @ApiResponses({
		   @ApiResponse(responseCode = "200", description = "Solicitudes encontradas"),
		   @ApiResponse(responseCode = "400", description = "Email inválido o no proporcionado")
		 })
		 @GetMapping("/presentadas/usuario")
		 public ResponseEntity<List<SolicitudDto>> getSolicitudesPresentadasPorUsuario(@RequestParam String email) {
		     if (email == null || email.isBlank()) {
		         return ResponseEntity.badRequest().build();
		     }
		     
		     List<SolicitudDto> solicitudesDto = solicitudService.findByEstadoAndUsuarioEmail(0, email).stream()
		         .map(solicitud -> new SolicitudDto().mapFromEntity(solicitud))
		         .toList();
		     return ResponseEntity.ok(solicitudesDto);
		 }

}
