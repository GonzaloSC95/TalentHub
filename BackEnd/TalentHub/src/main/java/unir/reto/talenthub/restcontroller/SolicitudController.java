package unir.reto.talenthub.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import unir.reto.talenthub.service.SolicitudService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("talenthub/api/solicitud")
public class SolicitudController {

   @Autowired
   private SolicitudService solicitudService;
}
