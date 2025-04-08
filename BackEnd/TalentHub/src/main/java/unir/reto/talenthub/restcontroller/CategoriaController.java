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
import unir.reto.talenthub.dto.CategoriaDto;
import unir.reto.talenthub.entity.Categoria;
import unir.reto.talenthub.service.CategoriaService;

/**
 * Controlador REST para gestionar las categorías.
 * http://localhost:8085/swagger-ui/index.html.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("talenthub/api/categoria")
public class CategoriaController {

   @Autowired
   private CategoriaService categoriaService;

   @Operation(
      summary = "Obtener categoría por ID.",
      description = "Devuelve una categoría por su ID."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categoría obtenida"),
      @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
   })
   @GetMapping("/{id}")
   public ResponseEntity <CategoriaDto> getCategoriaById(@PathVariable int id) {
      Categoria categoria = categoriaService.findByIdCategoria(id);
      CategoriaDto categoriaDto = new CategoriaDto();
      if (categoria != null) {
         return ResponseEntity.status(HttpStatus.OK).body(categoriaDto.mapFromEntity(categoria));
      } else {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
   }

   @Operation(
      summary = "Obtener categorías.",
      description = "Devuelve todas las categorías."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categorías obtenidas")
   })
   @GetMapping("/all")
   public ResponseEntity <List<CategoriaDto>> getAllCategorias() {
      List<CategoriaDto> categorias = categoriaService.findAll().stream()
            .map(categoria -> new CategoriaDto().mapFromEntity(categoria))
            .toList();
      return ResponseEntity.ok(categorias);
   }

   @Operation(
      summary = "Crear categoria.",
      description = "Devuelve la categoría creada."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categoría creada"),
      @ApiResponse(responseCode = "400", description = "Error al crear la categoría")
   })
   @PostMapping("/crear")
   public ResponseEntity <CategoriaDto> crearCategoria(@RequestBody CategoriaDto categoria) {
      int result = categoriaService.save(categoria.mapToEntity(categoria));
      if (result == 0) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
      return ResponseEntity.status(HttpStatus.OK).body(categoria);
   }

   @Operation(
      summary = "Actualizar categoría.",
      description = "Devuelve la categoría actualizada."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categoría actualizada"),
      @ApiResponse(responseCode = "400", description = "Error al actualizar la categoría")
   })
   @PutMapping("/actualizar")
   public ResponseEntity <CategoriaDto> actualizarCategoria(@RequestBody CategoriaDto categoria) {
      int result = categoriaService.update(categoria.mapToEntity(categoria));
      if (result == 0) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
      return ResponseEntity.status(HttpStatus.OK).body(categoria);
   }

   @Operation(
      summary = "Eliminar categoría.",
      description = "Devuelve la categoría eliminada."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categoría eliminada"),
      @ApiResponse(responseCode = "400", description = "Error al eliminar la categoría")
   })
   @DeleteMapping("/eliminar")
   public ResponseEntity <CategoriaDto> eliminarCategoria(@RequestBody CategoriaDto categoria) {
      int result = categoriaService.delete(categoria.mapToEntity(categoria));
      if (result == 0) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
      return ResponseEntity.status(HttpStatus.OK).body(categoria);
   }

}
