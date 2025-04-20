package unir.reto.talenthub.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import unir.reto.talenthub.configuration.UsuarioMapper;
import unir.reto.talenthub.dto.EmpresaDto;
import unir.reto.talenthub.dto.UsuarioDto;
import unir.reto.talenthub.entity.Empresa;
import unir.reto.talenthub.entity.Usuario;
import unir.reto.talenthub.service.EmpresaService;
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

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Operation(summary = "Obtener usuario por login", description = "Devuelve un usuario por email y contraseña.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario obtenido"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @Parameters({
        @Parameter(name = "email", description = "Email del usuario"),
        @Parameter(name = "password", description = "Password del usuario")
    })
    @GetMapping("/login")
    public ResponseEntity<UsuarioDto> getUsuarioByLogin(@RequestParam String email, @RequestParam String password) {
        Usuario usuario = usuarioService.findByEmailAndPassword(email, password);
        if (usuario != null) {
            UsuarioDto dto = usuarioMapper.mapWithEmpresa(usuario);
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(
        summary = "Registrar usuario.",
        description = "registra un usuario nuevo."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado."),
        @ApiResponse(responseCode = "400", description = "Error al crear el usuario.")
    })
    @Transactional
    @PostMapping("/registrar")
    public ResponseEntity<UsuarioDto> registrarUsuario(@RequestBody UsuarioDto usuarioDto) {
        //Mapeamos el usuario y la empresa desde el DTO a la entidad
        System.out.println(usuarioDto);
        Usuario usuario = usuarioDto.mapToEntity(usuarioDto);
        System.out.println(usuario);
        //Guardamos el usuario la base de datos
        int result = usuarioService.save(usuario);
        System.out.println(result);
        if (result == 1) {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDto);
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(
        summary = "Registrar usuario y empresa.",
        description = "registra un usuario y una empresa nuevos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario y empresa creados"),
        @ApiResponse(responseCode = "400", description = "Error al crear el usuario o la empresa")
    })
    @Transactional
    @PostMapping("/registrar/empresa")
    public ResponseEntity<UsuarioDto> registrarUsuarioEmpresa(@RequestBody UsuarioDto usuarioDto,@RequestBody EmpresaDto empresaDto) {
        //Mapeamos el usuario y la empresa desde el DTO a la entidad
        Usuario usuario = usuarioDto.mapToEntity(usuarioDto);
        Empresa empresa = empresaDto.mapToEntity(empresaDto);
        //Guardamos el usuario la base de datos
        int result = usuarioService.save(usuario);
        if (result == 1) {
            //Guardamos la empresa en la base de datos
            empresa.setUsuario(usuario);
            result = empresaService.save(empresa);
            if (result == 1) {
                //Si se ha guardado correctamente, devolvemos el usuario
                return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDto);
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado"),
        @ApiResponse(responseCode = "400", description = "Error al crear el usuario")
    })
    @Transactional
    @PostMapping("/crear")
    public ResponseEntity<UsuarioDto> createUsuario(@RequestBody UsuarioDto usuarioDto) {
        Usuario usuario = usuarioMapper.mapToEntity(usuarioDto);
        int result = usuarioService.save(usuario);

        if (result == 1) {
            if (usuarioDto.getEmpresaId() != null) {
                Empresa empresa = empresaService.findByidEmpresa(usuarioDto.getEmpresaId());
                if (empresa != null) {
                    empresa.setUsuario(usuario);
                    empresaService.update(empresa);
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza un usuario existente.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @Transactional
    @PutMapping("/actualizar")
    public ResponseEntity<UsuarioDto> updateUsuario(@RequestBody UsuarioDto usuarioDto) {
        Usuario usuario = usuarioMapper.mapToEntity(usuarioDto);
        int result = usuarioService.update(usuario);
        return result == 1 ? ResponseEntity.ok(usuarioDto) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario existente.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario eliminado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/eliminar")
    public ResponseEntity<UsuarioDto> deleteUsuario(@RequestBody UsuarioDto usuarioDto) {
        Usuario usuario = usuarioMapper.mapToEntity(usuarioDto);
        int result = usuarioService.delete(usuario);
        return result == 1 ? ResponseEntity.ok(usuarioDto) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve todos los usuarios.")
    @ApiResponse(responseCode = "200", description = "Usuarios obtenidos")
    @GetMapping("/all")
    public ResponseEntity<List<UsuarioDto>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();
        List<UsuarioDto> usuariosDto = usuarios.stream()
                .map(usuarioMapper::mapWithEmpresa)
                .toList();
        return ResponseEntity.ok(usuariosDto);
    }
}
