package unir.reto.talenthub.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import unir.reto.talenthub.configuration.UsuarioMapper;
import unir.reto.talenthub.dto.EmpresaDto;
import unir.reto.talenthub.dto.RegistroEmpresaRequestDto;
import unir.reto.talenthub.dto.UsuarioDto;
import unir.reto.talenthub.entity.Empresa;
import unir.reto.talenthub.entity.Usuario;
import unir.reto.talenthub.service.EmpresaService;
import unir.reto.talenthub.service.UsuarioService;

/**
 * Controlador REST para gestionar los usuarios.
 * http://localhost:8085/swagger-ui/index.html.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/talenthub/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private UsuarioMapper usuarioMapper;
    
    @Operation(
    	      summary = "Obtener una usuario por su email.",
    	      description = "Devuelve usuario por su email."
    	   )
    @ApiResponses(value = {
    	      @ApiResponse(responseCode = "200", description = "Usuario encontrada."),
    	      @ApiResponse(responseCode = "404", description = "Usuario no encontrada.")
    	   })
    @GetMapping("/{email}")
    	   public ResponseEntity<UsuarioDto> getUsuario(@PathVariable String email) {
    		Usuario usuario = usuarioService.findByEmail(email);

    	      if (usuario == null) {
    	         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    	      }

    	      UsuarioDto dto = usuarioMapper.mapWithEmpresa(usuario);
    	      return ResponseEntity.ok(dto);
    	   }

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
        Usuario usuario = usuarioService.findByEmailAndPassword(email, "{noop}" + password);
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
        // Acepta UN RequestBody del tipo contenedor
        public ResponseEntity<?> registrarUsuarioEmpresa(@RequestBody /*@Valid*/ RegistroEmpresaRequestDto requestDto) {
            try {
                // Accede a los DTOs individuales desde el contenedor
                UsuarioDto usuarioDto = requestDto.getUsuario();
                EmpresaDto empresaDto = requestDto.getEmpresa();

                // --- Validación básica (o usa @Valid) ---
                if (usuarioDto == null || empresaDto == null) {
                    return ResponseEntity.badRequest().body("Faltan datos de usuario o empresa.");
                }
                // Puedes añadir más validaciones aquí si no usas @Valid

                // Mapeamos el usuario y la empresa desde el DTO a la entidad
                // Asumiendo que los mappers/DTOs tienen métodos mapToEntity
                Usuario usuario = usuarioMapper.mapToEntity(usuarioDto); // O usa un constructor/builder
                Empresa empresa = empresaDto.mapToEntity(empresaDto); // O usa un constructor/builder

                // --- Lógica de guardado ---
                // Guarda el usuario PRIMERO (si empresa depende de él)
                 usuarioService.save(usuario); // Habria que verificar con try/catch
                 Usuario savedUsuario = usuarioService.findByEmail(usuario.getEmail());
                // Asigna el usuario guardado (con ID) a la empresa y guarda la empresa
                empresa.setUsuario(savedUsuario);
                empresaService.save(empresa); // Asume que save maneja la creación

                // Devuelve el DTO del usuario creado (o un mensaje de éxito)
                UsuarioDto responseDto = usuarioMapper.mapWithEmpresa(savedUsuario); // Mapea la entidad guardada a DTO
                return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

            } catch (DataIntegrityViolationException e) { // Ejemplo: Captura errores de constraint
                // Log e
                 return ResponseEntity.badRequest().body("Error de datos: " + e.getMessage());
            } catch (Exception e) { // Captura genérica
                // Log e
                 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al registrar.");
            }
        }

/*    @Operation(
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
    }*/

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

    @Operation(summary = "Eliminar/inactivar usuario", description = "Elimina un usuario existente.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario "),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/disable")//antes delete
    public ResponseEntity<UsuarioDto> deleteUsuario(@RequestBody UsuarioDto usuarioDto) {
        Usuario usuario = usuarioMapper.mapToEntity(usuarioDto);
       /* int result = usuarioService.delete(usuario);
        return result == 1 ? ResponseEntity.ok(usuarioDto) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();*/
        usuario.setEnabled(0);
        int result = usuarioService.save(usuario);
        UsuarioDto dto = usuarioMapper.mapWithEmpresa(usuario);
        return result == 1 ? ResponseEntity.ok(dto) : ResponseEntity.badRequest().build();
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
