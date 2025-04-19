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
import unir.reto.talenthub.configuration.EmpresaMapper;
import unir.reto.talenthub.dto.EmpresaDto;

import unir.reto.talenthub.entity.Empresa;

import unir.reto.talenthub.service.EmpresaService;


/*
 * Controlador REST para gestionar las empresas y vacantes.
 * http://localhost:8085/swagger-ui/index.html.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("talenthub/api/empresa")
public class EmpresaController {

   @Autowired
   private EmpresaService empresaService;
   
   @Autowired
   private EmpresaMapper empresaMapper;
   

   @Operation(
	        summary = "Obtener una empresa por su id.",
	        description = "Devuelve la empresa por su id."
	    )
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Empresa encontrada."),
	        @ApiResponse(responseCode = "404", description = "Empresa no encontrada.")
	    })
	    @GetMapping("/{id}")
	    public ResponseEntity<EmpresaDto> getEmpresa(@PathVariable int id) {
	        Empresa empresa = empresaService.findByidEmpresa(id);
	        if (empresa == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }
	        return ResponseEntity.ok(empresaMapper.mapToDto(empresa));
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
	    public ResponseEntity<EmpresaDto> crearEmpresa(@RequestBody EmpresaDto empresaDto) {
	        Empresa empresa = empresaMapper.mapToEntity(empresaDto);
	        int result = empresaService.save(empresa);
	        if (result == 0) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	        }
	        return ResponseEntity.status(HttpStatus.CREATED).body(empresaDto);
	    }

	    @Operation(
	        summary = "Actualizar empresa.",
	        description = "Devuelve la empresa actualizada."
	    )
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Empresa actualizada."),
	        @ApiResponse(responseCode = "404", description = "Empresa no encontrada.")
	    })
	    @PutMapping("/actualizar")
	    public ResponseEntity<EmpresaDto> actualizarEmpresa(@RequestBody EmpresaDto empresaDto) {
	        Empresa empresaExistente = empresaService.findByidEmpresa(empresaDto.getIdEmpresa());
	        if (empresaExistente == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }
	        Empresa empresa = empresaMapper.mapToEntity(empresaDto);
	        int result = empresaService.update(empresa);
	        if (result == 0) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	        }
	        return ResponseEntity.ok(empresaDto);
	    }

	    @Operation(
	        summary = "Eliminar empresa.",
	        description = "Elimina la empresa."
	    )
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Empresa eliminada."),
	        @ApiResponse(responseCode = "404", description = "Empresa no encontrada.")
	    })
	    @DeleteMapping("/eliminar")
	    public ResponseEntity<EmpresaDto> eliminarEmpresa(@RequestBody EmpresaDto empresaDto) {
	        Empresa empresaExistente = empresaService.findByidEmpresa(empresaDto.getIdEmpresa());
	        if (empresaExistente == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }
	        Empresa empresa = empresaMapper.mapToEntity(empresaDto);
	        int result = empresaService.delete(empresa);
	        if (result == 0) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	        }
	        return ResponseEntity.ok(empresaDto);
	    }

	    @Operation(
	        summary = "Obtener empresas.",
	        description = "Devuelve todas las empresas."
	    )
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Lista de empresas obtenida.")
	    })
	    @GetMapping("/all")
	    public ResponseEntity<List<EmpresaDto>> getEmpresas() {
	        List<EmpresaDto> empresasDto = empresaService.findAll().stream()
	                .map(empresaMapper::mapToDto)
	                .toList();
	        return ResponseEntity.ok(empresasDto);
	    }
	

}
