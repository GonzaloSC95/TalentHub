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
import { Usuario } from '../../interfaces/usuario';



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
  usuario: Usuario | null = null;

  formSolicitud!: FormGroup;
  formUsuario!: FormGroup;

  tipo: string = '';
  id: string = '';

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
    this.id = this.route.snapshot.params['id'];

    if (this.tipo === 'usuario') {
      this.cargarUsuario(decodeURIComponent(this.id));
    } else {
      // Para vacante o solicitud convertir a número
      const idNum = Number(this.id);
      if (isNaN(idNum)) {
        alert('ID inválido');
        this.volver();
        return;
      }

      this.crearFormularioSolicitud();

      if (this.tipo === 'vacante' || this.tipo === 'buscar ofertas') {
        this.cargarVacante(idNum);
      } else if (this.tipo === 'solicitud' || this.tipo === 'mis solicitudes') {
        this.cargarSolicitud(idNum);
      } else {
        alert('Tipo no soportado');
        this.volver();
      }
    }
  }

  // ---------------------------------------------------
  // FORMULARIO Y FUNCIONES PARA SOLICITUD / VACANTE
  // ---------------------------------------------------
  crearFormularioSolicitud() {
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

        const usuarioLogueado = this.usuarioService.getUsuario();

        this.formSolicitud.patchValue({
          idVacante: vacante.idVacante,
          emailUsuario: usuarioLogueado ? usuarioLogueado.email : '',
          estado: '0',
          fecha: this.formatDateForInput(new Date()),
          archivo: '',
          curriculum: '',
          comentarios: '',
        });

        this.formSolicitud.get('estado')?.disable();
        this.formSolicitud.get('fecha')?.disable();
        this.formSolicitud.get('archivo')?.enable();
        this.formSolicitud.get('curriculum')?.enable();
        this.formSolicitud.get('comentarios')?.enable();

        if (usuarioLogueado) {
          this.formSolicitud.get('emailUsuario')?.disable();
        } else {
          this.formSolicitud.get('emailUsuario')?.enable();
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

    // Volver a deshabilitar si es edición (opcional)
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

  formatDateForInput(date: Date): string {
    const d = new Date(date);
    const month = ('0' + (d.getMonth() + 1)).slice(-2);
    const day = ('0' + d.getDate()).slice(-2);
    return `${d.getFullYear()}-${month}-${day}`;
  }

  volver() {
    this.location.back();
  }

  // ---------------------------------------------------
  // FORMULARIO Y FUNCIONES PARA USUARIO
  // ---------------------------------------------------
  crearFormularioUsuario() {
    this.formUsuario = this.fb.group({
      email: [{ value: '', disabled: true }, [Validators.required, Validators.email]],
      nombre: ['', Validators.required],
      apellidos: ['', Validators.required],
      password: ['', Validators.required],  // obligatorio para actualizar usuario
    });
  }

  cargarUsuario(email: string) {
    this.crearFormularioUsuario();

    this.usuarioService.getUsuarioByEmail(email).subscribe({
      next: (usuario) => {
        this.usuario = usuario;

        // para no hacer nada de modifcar back, quitamos el {noop} que son siempre 6 digitos
      const passwordSinNoop = usuario.password?.startsWith('{noop}')
      ? usuario.password.substring(6)
      : usuario.password;



        this.formUsuario.patchValue({
          email: usuario.email,
          nombre: usuario.nombre,
          apellidos: usuario.apellidos,
          password: passwordSinNoop //aplicamos sin {noop}
        });
      },
      error: (err) => {
        console.error('Error cargando usuario:', err);
        alert('No se pudo cargar el usuario');
        this.volver();
      },
    });
  }
  async getDataUsuario() {
    if (this.formUsuario.invalid) {
      this.formUsuario.markAllAsTouched();
      return;
    }
  
    const usuarioOriginal = this.usuario!;
  
      //para pasarlo lo volvemos a poner, un poco guarrete pero funciona sin cambiar más
    const password = this.formUsuario.value.password?.trim() || '';
    const passwordConNoop = password.startsWith('{noop}') ? password : `{noop}${password}`;
  
    const usuarioActualizado: Usuario = {
      email: usuarioOriginal.email,
      nombre: this.formUsuario.value.nombre,
      apellidos: this.formUsuario.value.apellidos,
      password: passwordConNoop,
      enabled: 1,
      rol: 'CLIENTE',
      fechaRegistro: usuarioOriginal.fechaRegistro,
    };
  
    try {
      const resultado = await this.usuarioService.actualizarUsuario(usuarioActualizado);
      if (resultado) {
        alert('Usuario actualizado con éxito');
        this.volver();
      } else {
        alert('No se pudo actualizar el usuario');
      }
    } catch (error) {
      alert('Error al actualizar usuario: ' + error);
    }
  }
  
  
}
