import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { VacanteService } from '../../service/vacante.service';
import { SolicitudService } from '../../service/solicitud.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-vacante-detalle',
  standalone: true,
  templateUrl: './vacante-detalle.component.html',
  styleUrls: ['./vacante-detalle.component.css']
})
export class VacanteDetalleComponent implements OnInit {
  tipo!: string;
  id!: number;

  vacante: any = null;
  solicitud: any = null;

  constructor(
    private route: ActivatedRoute,
    private vacanteService: VacanteService,
    private solicitudService: SolicitudService,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.tipo = params.get('type') || '';
      this.id = Number(params.get('id'));

      if (!this.id || !this.tipo) {
        console.error('Faltan parámetros en la ruta');
        return;
      }

      if (this.tipo === 'vacante' || this.tipo === 'buscar ofertas' ) {
        this.cargarVacante();
      } else if (this.tipo === 'solicitud' || this.tipo === 'mis solicitudes') {
        this.cargarSolicitud();
      } else {
        console.warn('Tipo no reconocido:', this.tipo);
      }
    });
  }

  cargarVacante() {
    this.vacanteService.getVacanteById(this.id).subscribe({
      next: vacante => {this.vacante = vacante,
        console.log('Vacante cargada:', vacante);},

      error: err => console.error('Error cargando vacante:', err)
    });
  }

  cargarSolicitud() {
    this.solicitudService.getSolicitudById(this.id).subscribe({
      next: solicitud => {this.solicitud = solicitud,
      console.log('Solicitud cargada:', solicitud);},
      error: err => console.error('Error cargando solicitud:', err)
    });
  }

  volver() {
    this.location.back();
  }
}
