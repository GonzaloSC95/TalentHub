import { CommonModule, Location } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Solicitud } from '../../interfaces/solicitud';
import { Vacante } from '../../interfaces/vacante';
import { SolicitudService } from '../../service/solicitud.service';
import { VacanteService } from '../../service/vacante.service';


@Component({
  selector: 'app-modificar',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './modificar.component.html',
  styleUrl: './modificar.component.css'
})
export class ModificarComponent {  

solictudService = inject (SolicitudService);

formSolicitud: FormGroup;
vacante!: Vacante;
solicitud!: Solicitud;


constructor(
  private fb: FormBuilder,
  private solicitudService: SolicitudService,
  private vacanteService: VacanteService,
  private route: ActivatedRoute,
  private location: Location
) {
  this.formSolicitud = this.fb.group({
    fecha: [''],
    archivo: [''],
    comentarios: [''],
    estado: [''],
    curriculum: [''],
    emailUsuario: [''],
    idVacante: ['']
  });
}

ngOnInit() {
  const idVacante = Number(this.route.snapshot.paramMap.get('id'));
  this.vacanteService.getVacanteById(idVacante).subscribe((v) => {
    this.vacante = v;
    this.formSolicitud.patchValue({ idVacante: v.idVacante });
   // this.formSolicitud.patchValue({fecha: v.fecha});
  });
}

getDataSolicitud() {

  if (this.formSolicitud.invalid) {
    this.formSolicitud.markAllAsTouched();
    return;
  }

  const solicitud: Solicitud = {
    ...this.formSolicitud.value,
   fecha: new Date(this.formSolicitud.value.fecha)
  };

  this.solicitudService.crearSolicitud(solicitud).subscribe({
    next: () => console.log('Solicitud enviada con éxito'),
    error: (err) => console.error('Error al enviar solicitud', err)
  });

}


volver() {
  this.location.back();
}


}