package unir.reto.talenthub.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import unir.reto.talenthub.entity.Estatus;
import unir.reto.talenthub.entity.Vacante;
import unir.reto.talenthub.service.EmpresaService;
import unir.reto.talenthub.service.VacanteService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("talenthub/api/empresa")
public class EmpresaController {

   @Autowired
   private EmpresaService empresaService;

   @Autowired
   private VacanteService vacanteService;

   @Operation(
      summary = "Publicar una nueva vacante",
      description = "Crea una nueva vacante con estado 'CREADA'."
   )
   @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Vacante creada"),
      @ApiResponse(responseCode = "400", description = "Solicitud inválida")
   })
   @PostMapping("/publicar/vacante")
   public ResponseEntity<Vacante> publicarVacante(@RequestBody Vacante vacante) {
      vacante.setEstatus(Estatus.CREADA);
      int vacanteCreadaNm = vacanteService.save(vacante);
      if (vacanteCreadaNm == 0) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
      else{
         return ResponseEntity.status(HttpStatus.CREATED).body(vacante);
      }
   }

}
