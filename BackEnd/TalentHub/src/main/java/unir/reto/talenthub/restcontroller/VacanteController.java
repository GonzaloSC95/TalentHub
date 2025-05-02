package unir.reto.talenthub.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import unir.reto.talenthub.configuration.VacanteMapper;
import unir.reto.talenthub.dto.VacanteDto;
import unir.reto.talenthub.entity.Empresa;
import unir.reto.talenthub.entity.Estatus;
import unir.reto.talenthub.entity.Solicitud;
import unir.reto.talenthub.entity.Usuario;
import unir.reto.talenthub.entity.Vacante;
import unir.reto.talenthub.service.EmpresaService;
import unir.reto.talenthub.service.SolicitudService;
import unir.reto.talenthub.service.UsuarioService;
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
	 	
	 	@Autowired 
	 	private SolicitudService solicitudService;
	 	
	 	@Autowired 
	 	private UsuarioService usuarioService;
	 	
	 	@Autowired 
	 	private EmpresaService empresaService;
	 	
	 	@Autowired 
	 	private VacanteMapper vacanteMapper;

	 	
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
	      Vacante vacante = vacanteService.findByIdVacante(id);
	      if (vacante != null) {
	         return ResponseEntity.ok(vacanteMapper.mapToDto(vacante));
	      }
	      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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
	   public ResponseEntity<VacanteDto> crearVacante(@RequestBody VacanteDto vacanteDto) {
	      vacanteDto.setEstatus(Estatus.CREADA.name());
	      Vacante vacante = vacanteMapper.mapToEntity(vacanteDto);
	      int result = vacanteService.save(vacante);
	      return result == 1 ? ResponseEntity.status(HttpStatus.CREATED).body(vacanteDto)
	                         : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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
	   public ResponseEntity<VacanteDto> updateVacante(@RequestBody VacanteDto vacanteDto) {
	      Vacante vacante = vacanteMapper.mapToEntity(vacanteDto);
	      int result = vacanteService.update(vacante);
	      return result == 1 ? ResponseEntity.ok(vacanteDto)
	                         : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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
	   public ResponseEntity<VacanteDto> deleteVacante(@RequestBody VacanteDto vacanteDto) {
	      Vacante vacante = vacanteMapper.mapToEntity(vacanteDto);
	      int result = vacanteService.delete(vacante);
	      return result == 1 ? ResponseEntity.ok(vacanteDto)
	                         : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	   }

	   
	   @Operation(
			      summary = "Cancelar vacante.",
			      description = "Devuelve la vacante cancelada."
			   )
			   @ApiResponses(value = {
			      @ApiResponse(responseCode = "200", description = "Vacante cancelada"),
			      @ApiResponse(responseCode = "400", description = "Error al cancelar la vacante")
			   })
	   @PutMapping("/cancelar")
	   public ResponseEntity<VacanteDto> cancelarVacante(@RequestBody VacanteDto vacanteDto) {
	      vacanteDto.setEstatus(Estatus.CANCELADA.name());
	      Vacante vacante = vacanteMapper.mapToEntity(vacanteDto);
	      int result = vacanteService.update(vacante);
	      return result == 1 ? ResponseEntity.ok(vacanteDto)
	                         : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	   }

	   
	   @Operation(
			      summary = "Asignar vacante.",
			      description = "Devuelve la vacante asignada."
			   )
			   @ApiResponses(value = {
			      @ApiResponse(responseCode = "200", description = "Vacante asignada"),
			      @ApiResponse(responseCode = "400", description = "Error al asignar la vacante")
			   })
			   @Parameter(name = "emailUsuario", description = "Email del usuario que solicita la vacante")
	   @PutMapping("/asignar/{emailUsuario}")
	   public ResponseEntity<VacanteDto> asignarVacante(@PathVariable String emailUsuario, @RequestBody VacanteDto vacanteDto) {
	      Usuario usuario = usuarioService.findByEmail(emailUsuario);
	      if (usuario == null) {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	      }

	      Vacante vacante = vacanteMapper.mapToEntity(vacanteDto);
	      vacante.setEstatus(Estatus.CUBIERTA);
	      int result = vacanteService.update(vacante);

	      if (result == 1) {
	         Solicitud solicitud = solicitudService.findByVacanteAndUsuario(vacante, usuario);
	         if (solicitud != null) {
	            solicitud.setEstado(1);
	            result = solicitudService.update(solicitud);
	            if (result == 0) {
	               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	            }
	         }
	         return ResponseEntity.ok(vacanteMapper.mapToDto(vacante));
	      }
	      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	   }

	   @Operation(
			      summary = "Obtener las vacantes de una empresa.",
			      description = "Devuelve todas las vacantes de una empresa."
			   )
			   @ApiResponses(value = {
			      @ApiResponse(responseCode = "200", description = "Vacantes obtenidas")
			   })
	   @GetMapping("/empresa/{idEmpresa}")
	   public ResponseEntity<List<VacanteDto>> getVacantesByEmpresa(@PathVariable int idEmpresa) {
	      Empresa empresa = empresaService.findByidEmpresa(idEmpresa);
	      if (empresa == null) {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	      }

	      List<VacanteDto> vacantes = vacanteService.findByEmpresa(empresa).stream()
	            .map(vacanteMapper::mapToDto)
	            .collect(Collectors.toList());
	      return ResponseEntity.ok(vacantes);
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
	            .map(vacanteMapper::mapToDto)
	            .collect(Collectors.toList());
	      return ResponseEntity.ok(vacantes);
	   }
	   
	   @Operation(
			      summary = "Obtener todas las vacantes creadas.",
			      description = "Devuelve todas las vacantes creadas."
			   )
			   @ApiResponses(value = {
			      @ApiResponse(responseCode = "200", description = "Vacantes creadas obtenidas")
			   })
	   @GetMapping("/creadas")
	   public ResponseEntity<List<VacanteDto>> getAllCreadas() {
	      List<VacanteDto> vacantes = vacanteService.findByEstatus(Estatus.CREADA).stream()
	            .map(vacanteMapper::mapToDto)
	            .collect(Collectors.toList());
	      return ResponseEntity.ok(vacantes);
	   }
	   
	 //añadimos rest para poder hacer los filtros en las vacantes creadas
	   
	   

	   @GetMapping("/filtrar")
	   public ResponseEntity<List<VacanteDto>> filtrarVacantes(
	       @RequestParam(required = false) String nombre,
	       @RequestParam(required = false) String descripcion,
	       @RequestParam(required = false) Integer salarioMin,
	       @RequestParam(required = false) Integer salarioMax
	       
	   ) {
	       List<Vacante> vacantes = vacanteService.findByEstatus(Estatus.CREADA);

	       if (nombre != null && !nombre.isEmpty()) {
	           vacantes = vacantes.stream()
	               .filter(v -> v.getNombre() != null && v.getNombre().toLowerCase().contains(nombre.toLowerCase()))
	               .collect(Collectors.toList());
	       }

	       if (descripcion != null && !descripcion.isEmpty()) {
	           vacantes = vacantes.stream()
	               .filter(v -> v.getDescripcion() != null && v.getDescripcion().toLowerCase().contains(descripcion.toLowerCase()))
	               .collect(Collectors.toList());
	       }

	       
	       if (salarioMin != null) {
	           vacantes = vacantes.stream()
	        		   .filter(v -> v.getSalario() >= salarioMin)
	               .collect(Collectors.toList());
	       }

	       if (salarioMax != null) {
	           vacantes = vacantes.stream()
	               .filter(v -> v.getSalario() <= salarioMax)
	               .collect(Collectors.toList());
	       }

	       List<VacanteDto> vacantesDto = vacantes.stream()
	           .map(vacanteMapper::mapToDto)
	           .collect(Collectors.toList());

	       return ResponseEntity.ok(vacantesDto);
	   }

	   
	   
	}
