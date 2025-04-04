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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import unir.reto.talenthub.entity.Usuario;
import unir.reto.talenthub.service.UsuarioService;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * Controlador REST para gestionar los usuarios.
 * http://localhost:8085/swagger-ui/index.html.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("talenthub/api/usuario")
public class UsuarioController {

   @Autowired
   private UsuarioService usuarioService;

   @Operation(
      summary = "Obtener usuario por ID.",
      description = "Devuelve un usuario por su ID."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuario obtenido"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
   })
   @GetMapping("/{id}/{password}")
   public ResponseEntity<Usuario> getUsuarioById(@PathVariable String email, @PathVariable String password) {
      Usuario usuario = usuarioService.findByEmailAndPassword(email, password);
      if (usuario != null) {
         return ResponseEntity.status(HttpStatus.OK).body(usuario);
      } else {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @Operation(
      summary = "Obtener todos los usuarios.",
      description = "Devuelve todos los usuarios."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuarios obtenidos")
   })
   @GetMapping("/all")
   public ResponseEntity<List<Usuario>> getAllUsuarios() {
      return ResponseEntity.ok(usuarioService.findAll());
   }

   @Operation(
      summary = "Crear usurio.",
      description = "Crea un nuevo usuario."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Usuario creado"),
      @ApiResponse(responseCode = "400", description = "Error al crear el usuario")
   })
   @PostMapping("/crear")
   public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
      int result = usuarioService.save(usuario);
      if (result == 1) {
         return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
      } else {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
   }

   @Operation(
      summary = "Actualizar usuario.",
      description = "Actualiza un usuario existente."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
      @ApiResponse(responseCode = "400", description = "Usuario no encontrado")
   })
   @PutMapping("/actualizar")
   public ResponseEntity<Usuario> updateUsuario(@RequestBody Usuario usuario) {
      int result = usuarioService.update(usuario);
      if (result == 1) {
         return ResponseEntity.status(HttpStatus.OK).body(usuario);
      } else {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }

   @Operation(
      summary = "Eliminar usuario.",
      description = "Elimina un usuario existente."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuario eliminado"),
      @ApiResponse(responseCode = "400", description = "Usuario no encontrado")
   })
   @DeleteMapping("/eliminar")
   public ResponseEntity<Usuario> deleteUsuario(@RequestBody Usuario usuario) {
      int result = usuarioService.delete(usuario);
      if (result == 1) {
         return ResponseEntity.status(HttpStatus.OK).body(usuario);
      } else {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
   }
}
