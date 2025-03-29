package unir.reto.talenthub.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import unir.reto.talenthub.entity.Categoria;
import unir.reto.talenthub.service.CategoriaService;

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
   public ResponseEntity <Categoria> getCategoriaById(@PathVariable int id) {
      Categoria categoria = categoriaService.findByIdCategoria(id);
      if (categoria != null) {
         return ResponseEntity.status(HttpStatus.OK).body(categoria);
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
   public ResponseEntity <List<Categoria>> getAllCategorias() {
      return ResponseEntity.ok(categoriaService.findAll());
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
   public ResponseEntity <Categoria> crearCategoria(@RequestBody Categoria categoria) {
      int result = categoriaService.save(categoria);
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
   @PostMapping("/actualizar")
   public ResponseEntity <Categoria> actualizarCategoria(@RequestBody Categoria categoria) {
      int result = categoriaService.update(categoria);
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
   @PostMapping("/eliminar")
   public ResponseEntity <Categoria> eliminarCategoria(@RequestBody Categoria categoria) {
      int result = categoriaService.delete(categoria);
      if (result == 0) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
      return ResponseEntity.status(HttpStatus.OK).body(categoria);
   }

}
