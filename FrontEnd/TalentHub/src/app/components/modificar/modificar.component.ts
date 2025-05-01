
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Vacante } from '../../interfaces/vacante';
import { Solicitud } from '../../interfaces/solicitud';
import { VacanteService } from '../../service/vacante.service';
import { SolicitudService } from '../../service/solicitud.service';
import { CommonModule } from '@angular/common';
import { Location } from '@angular/common';
import { UsuarioService } from '../../service/usuario.service';


@Component({
  selector: 'app-modificar',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './modificar.component.html',
  styleUrls: ['./modificar.component.css'],
})
export class ModificarComponent {
  vacante: Vacante | null = null;
  solicitud: Solicitud | null = null;
  formSolicitud!: FormGroup;
  tipo: string = '';
  id: number = 0;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private vacanteService: VacanteService,
    private solicitudService: SolicitudService,
    private location: Location,
    private usuarioService: UsuarioService,
  ) {}

  ngOnInit(): void {
    this.tipo = this.route.snapshot.params['tipo'];
    this.id = +this.route.snapshot.params['id'];

    this.crearFormulario();

    if (this.tipo === 'vacante' || this.tipo === 'buscar ofertas') {
      this.cargarVacante(this.id);
    } else if (this.tipo === 'solicitud' || this.tipo === 'mis solicitudes') {
      this.cargarSolicitud(this.id);
    }
  }

  crearFormulario() {
    this.formSolicitud = this.fb.group({
      fecha: ['', Validators.required],
      estado: ['', Validators.required],
      archivo: ['', Validators.required],
      curriculum: ['', Validators.required],
      comentarios: [''],
      emailUsuario: ['', [Validators.required, Validators.email]],
      idVacante: ['', Validators.required],
    });
  }

  cargarVacante(idVacante: number) {
    this.vacanteService.getVacanteById(idVacante).subscribe({
      next: (vacante) => {
        this.vacante = vacante;

        const usuarioLogueado = this.usuarioService.getUsuario();//nos traemos el usuario con el que estamos logados

        this.formSolicitud.patchValue({
          idVacante: vacante.idVacante,
          emailUsuario: usuarioLogueado ? usuarioLogueado.email : '',  // email del usuario logueado o vacío
          estado: '0',      // Estado inicial en creación x defecto presentada
          fecha: this.formatDateForInput(new Date()),
          archivo: '',
          curriculum: '',
          comentarios: '',
        });

        // En creación: todos editables excepto estado (readonly)
        this.formSolicitud.get('estado')?.disable();

        this.formSolicitud.get('fecha')?.disable();
        this.formSolicitud.get('archivo')?.enable();
        this.formSolicitud.get('curriculum')?.enable();
        this.formSolicitud.get('comentarios')?.enable();
        if (usuarioLogueado) {
          this.formSolicitud.get('emailUsuario')?.disable(); // para que no se pueda modificar el email
        } else {
          this.formSolicitud.get('emailUsuario')?.enable(); // por si no hay usuario logueado, que puedan ponerlo por si en algun momeno se permite
        }
      },
      error: (err) => {
        console.error('Error cargando vacante:', err);
        alert('No se pudo cargar la vacante');
        this.volver();
      },
    });
  }

  cargarSolicitud(idSolicitud: number) {
    this.solicitudService.getSolicitudById(idSolicitud).subscribe({
      next: (solicitud) => {
        this.solicitud = solicitud;
        this.vacanteService.getVacanteById(Number(solicitud.vacante)).subscribe({
          next: (vacante) => (this.vacante = vacante),
          error: (err) => console.error('Error cargando vacante:', err),
        });

        this.formSolicitud.patchValue({
          fecha: this.formatDateForInput(new Date(solicitud.fecha)),
          estado: solicitud.estado,
          archivo: solicitud.archivo,
          curriculum: solicitud.curriculum,
          comentarios: solicitud.comentarios,
          emailUsuario: solicitud.candidato,
          idVacante: solicitud.vacante,
        });

        // En modificación: solo archivo, curriculum y comentarios habilitados para modificar la solicitud
        this.formSolicitud.get('fecha')?.disable();
        this.formSolicitud.get('estado')?.disable();
        this.formSolicitud.get('emailUsuario')?.disable();

        this.formSolicitud.get('archivo')?.enable();
        this.formSolicitud.get('curriculum')?.enable();
        this.formSolicitud.get('comentarios')?.enable();
      },
      error: (err) => {
        console.error('Error cargando solicitud:', err);
        alert('No se pudo cargar la solicitud');
        this.volver();
      },
    });
  }

  formatDateForInput(date: Date): string {
    const d = new Date(date);
    const month = ('0' + (d.getMonth() + 1)).slice(-2);
    const day = ('0' + d.getDate()).slice(-2);
    return `${d.getFullYear()}-${month}-${day}`;
  }

  getDataSolicitud() {
    if (this.formSolicitud.invalid) {
      this.formSolicitud.markAllAsTouched();
      return;
    }

    // Habilitar campos deshabilitados para obtener valor:
    this.formSolicitud.get('estado')?.enable();
    this.formSolicitud.get('fecha')?.enable();
    this.formSolicitud.get('emailUsuario')?.enable();

    const solicitud: Solicitud = {
      idSolicitud: this.solicitud?.idSolicitud || 0,
      fecha: this.formSolicitud.value.fecha,
      estado: this.formSolicitud.value.estado,
      archivo: this.formSolicitud.value.archivo,
      curriculum: this.formSolicitud.value.curriculum,
      comentarios: this.formSolicitud.value.comentarios,
      vacante: this.formSolicitud.value.idVacante.toString(),
      candidato: this.formSolicitud.value.emailUsuario,
    };

    // Volver a deshabilitar si es edición (esto es de forma opcional)
    if (this.tipo === 'solicitud' || this.tipo === 'mis solicitudes') {
      this.formSolicitud.get('fecha')?.disable();
      this.formSolicitud.get('estado')?.disable();
      this.formSolicitud.get('emailUsuario')?.disable();
    } else if (this.tipo === 'vacante' || this.tipo === 'buscar ofertas') {
      this.formSolicitud.get('estado')?.disable();
    }

    if (this.tipo === 'solicitud' || this.tipo === 'mis solicitudes') {
      this.solicitudService.actualizarSolicitud(solicitud).subscribe({
        next: () => {
          alert('Solicitud actualizada con éxito');
          this.volver();
        },
        error: (err) => alert('Error al actualizar solicitud: ' + err),
      });
    } else if (this.tipo === 'vacante' || this.tipo === 'buscar ofertas') {
      this.solicitudService.crearSolicitud(solicitud).subscribe({
        next: () => {
          alert('Solicitud creada con éxito');
          this.volver();
        },
        error: (err) => alert('Error al crear solicitud: ' + err),
      });
    }
  }

  
  volver() {
    this.location.back();
  }
}
