import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { VacanteService } from '../../service/vacante.service';
import { SolicitudService } from '../../service/solicitud.service';
import { Vacante } from '../../interfaces/vacante';
import { Solicitud } from '../../interfaces/solicitud';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { Location } from '@angular/common';

@Component({
  selector: 'app-vacante-modificar',
  standalone: true,
  templateUrl: './vacante-modificar.component.html',
  styleUrls: ['./vacante-modificar.component.css'],
  imports: [CommonModule, ReactiveFormsModule],
})
export class VacanteModificarComponent implements OnInit {
  id!: number;
  tipo!: string;
  formSolicitud: FormGroup;

  vacante!: Vacante;
  solicitud!: Solicitud;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private vacanteService: VacanteService,
    private solicitudService: SolicitudService,
    private location: Location,
  ) {
    // Inicializo formSolicitud con todos los controles para evitar errores en el template
    this.formSolicitud = this.fb.group({
      // Campos para vacante
      idVacante: [''],
      nombre: ['', Validators.required],
      descripcion: ['', Validators.required],
      fecha: ['', Validators.required],
      salario: ['', [Validators.required, Validators.min(0)]],
      estatus: ['', Validators.required],
      destacado: [''],
      imagen: [''],
      detalles: [''],
      categoria: [''],
      idEmpresa: ['', Validators.required],
      nombreEmpresa: ['', Validators.required],

      // Campos para solicitud
      idSolicitud: [''],
      archivo: [''],
      comentarios: [''],
      estado: ['', Validators.required],
      curriculum: [''],
      vacante: ['', Validators.required],
      candidato: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.tipo = this.route.snapshot.paramMap.get('tipo') || '';

    console.log('Parametros recibidos en modificar:', { id: this.id, tipo: this.tipo }); //verificacion de id

    if (this.tipo === 'vacante') {
      this.cargarVacante();
    } else if (this.tipo === 'solicitud') {
      this.cargarSolicitud();
    } else {
      console.error('Tipo no soportado:', this.tipo);
    }
  }

  volver() {
    this.location.back();
  }

  cargarVacante() {
    this.vacanteService.getVacanteById(this.id).subscribe({
      next: (vacante) => {
        this.vacante = vacante;

        this.formSolicitud.patchValue({
          idVacante: vacante.idVacante,
          nombre: vacante.nombre,
          descripcion: vacante.descripcion,
          fecha: this.formatDateInput(vacante.fecha),
          salario: vacante.salario,
          estatus: vacante.estatus,
          destacado: vacante.destacado,
          imagen: vacante.imagen,
          detalles: vacante.detalles,
          categoria: vacante.categoria,
          idEmpresa: vacante.idEmpresa,
          nombreEmpresa: vacante.nombreEmpresa,
        });
      },
      error: (err) => {
        console.error('Error al cargar vacante:', err);
        // Opcionalmente resetear o limpiar formulario
        this.formSolicitud.reset();
      }
    });
  }

  cargarSolicitud() {
    this.solicitudService.getSolicitudById(this.id).subscribe({
      next: (solicitud) => {
        this.solicitud = solicitud;

        this.formSolicitud.patchValue({
          idSolicitud: solicitud.idSolicitud,
          fecha: this.formatDateInput(solicitud.fecha),
          archivo: solicitud.archivo,
          comentarios: solicitud.comentarios,
          estado: solicitud.estado,
          curriculum: solicitud.curriculum,
          vacante: solicitud.vacante,
          candidato: solicitud.candidato,
        });
      },
      error: (err) => {
        console.error('Error al cargar solicitud:', err);
        this.formSolicitud.reset();
      }
    });
  }

  formatDateInput(date: Date | string | undefined): string {
    if (!date) return '';
    const d = new Date(date);
    const month = (d.getMonth() + 1).toString().padStart(2, '0');
    const day = d.getDate().toString().padStart(2, '0');
    return `${d.getFullYear()}-${month}-${day}`;
  }

  actualizar() {
    if (this.formSolicitud.invalid) {
      this.formSolicitud.markAllAsTouched();
      return;
    }

    if (this.tipo === 'vacante') {
      const vacanteActualizada: Vacante = {
        ...this.vacante,
        ...this.formSolicitud.value,
        fecha: new Date(this.formSolicitud.value.fecha)
      };
      this.vacanteService.actualizarVacante(vacanteActualizada).subscribe({
        next: () => {
          console.log('Vacante actualizada');
          this.volver();
        },
        error: (err) => console.error('Error actualizando vacante', err)
      });
    } else if (this.tipo === 'solicitud') {
      const solicitudActualizada: Solicitud = {
        ...this.solicitud,
        ...this.formSolicitud.value,
        fecha: new Date(this.formSolicitud.value.fecha)
      };
      this.solicitudService.actualizarSolicitud(solicitudActualizada).subscribe({
        next: () => {
          console.log('Solicitud actualizada');
          this.volver();
        },
        error: (err) => console.error('Error actualizando solicitud', err)
      });
    }
  }
}
