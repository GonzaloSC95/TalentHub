import { CommonModule, Location } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Empresa } from '../../interfaces/empresa';
import { Solicitud } from '../../interfaces/solicitud';
import { Usuario } from '../../interfaces/usuario';
import { Vacante } from '../../interfaces/vacante';
import { EmpresaService } from '../../service/empresa.service';
import { SolicitudService } from '../../service/solicitud.service';
import { UsuarioService } from '../../service/usuario.service';
import { VacanteService } from '../../service/vacante.service';



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
  empresa !:Empresa 

  formSolicitud!: FormGroup;
  formUsuario!: FormGroup;

  tipo: string = '';
  id: string = '';
  formEmpresa !: FormGroup;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private vacanteService: VacanteService,
    private solicitudService: SolicitudService,
    private location: Location,
    private usuarioService: UsuarioService,
    private empresaService : EmpresaService
  ) {}

  ngOnInit(): void {
    this.tipo = this.route.snapshot.params['tipo'];
    this.id = this.route.snapshot.params['id'];

    if (this.tipo === 'usuario') {
      this.cargarUsuario(decodeURIComponent(this.id));
    } else if (this.tipo === 'empresa') {

      const idNum = Number(this.id);
      if (isNaN(idNum) || idNum <= 0) {
        alert('ID de empresa inválido');
        this.volver();
        return;
      }
    this.cargarEmpresa(idNum); 
  }else {
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
    // ---------------------------------------------------
  // FORMULARIO Y FUNCIONES PARA EMPRESA (¡NUEVO!)
  // ---------------------------------------------------

  /** Crea el FormGroup para editar la empresa */
  crearFormularioEmpresa(): void {
    this.formEmpresa = this.fb.group({
      // El ID de la empresa no se edita, pero puede ser útil tenerlo
      idEmpresa: [{ value: null, disabled: true }],
      // El CIF podría ser o no editable, lo dejo editable por ahora
      cif: ['', [Validators.required, Validators.pattern(/^\w{1}\d{7}\w{1}$/)]], // Ejemplo de patrón CIF español simple, ajusta!
      nombreEmpresa: ['', Validators.required],
      direccionFiscal: ['', Validators.required],
      pais: ['', Validators.required],
      // El email del usuario asociado (contacto)
      email: [{ value: '', disabled: true }, [Validators.required, Validators.email]],
       // Podrías añadir aquí campos del USUARIO asociado si quieres editarlos a la vez
       // nombreContacto: [''],
       // apellidosContacto: [''],
       // passwordContacto: [''] // ¡CUIDADO con editar passwords aquí!
    });
  }

  /** Carga los datos de la empresa y su usuario asociado */
  cargarEmpresa(idEmpresa: number): void {
    this.crearFormularioEmpresa(); // Crea el formulario antes de cargar datos

    this.empresaService.getEmpresaById(idEmpresa).subscribe({ // Asume que tienes este método en EmpresaService
      next: (empresaData) => {
        this.empresa = empresaData;

        // Obtenemos el usuario asociado (si la relación está bien configurada y viene en la respuesta)
        // Asumiendo que 'empresaData' tiene una propiedad 'usuario' o necesitas buscarlo por email
        const usuarioAsociado = empresaData.email; // Ajusta esto según tu modelo Empresa

        this.formEmpresa.patchValue({
          idEmpresa: empresaData.idEmpresa,
          cif: empresaData.cif,
          nombreEmpresa: empresaData.nombreEmpresa,
          direccionFiscal: empresaData.direccionFiscal,
          pais: empresaData.pais,
          email: usuarioAsociado ? usuarioAsociado : empresaData.email, // Usa el email del usuario o el de la empresa si no hay usuario
          // Si añades campos de contacto al form:
          // nombreContacto: usuarioAsociado ? usuarioAsociado.nombre : '',
          // apellidosContacto: usuarioAsociado ? usuarioAsociado.apellidos : '',
        });

        // Deshabilitar campos que no se deben editar (como el ID, quizás el email de contacto)
        this.formEmpresa.get('idEmpresa')?.disable();
        

      },
      error: (err) => {
        console.error('Error cargando empresa:', err);
        alert('No se pudo cargar la empresa');
        this.volver();
      },
    });
  }

 /** Procesa y guarda los datos actualizados de la empresa */
 async getDataEmpresa() { // Marcado como async por si llamas a servicios async
    if (this.formEmpresa.invalid) {
      this.formEmpresa.markAllAsTouched();
      alert('Por favor, corrija los errores del formulario.');
      return;
    }
    const nuevoEmail = this.formEmpresa.value.email?.trim(); // Normalmente el email de contacto no se cambia fácilmente
    // Re-habilitar campos si es necesario obtener su valor (aunque patchValue funciona con disabled)
    // this.formEmpresa.get('idEmpresa')?.enable();
    // this.formEmpresa.get('email')?.enable();

    // Construye el objeto Empresa actualizado desde el formulario
    const empresaActualizada: Empresa = {
      // Mantenemos el ID original
      idEmpresa: this.empresa!.idEmpresa, // Usamos el ID cargado inicialmente
      // Tomamos los valores del formulario
      cif: this.formEmpresa.value.cif,
      nombreEmpresa: this.formEmpresa.value.nombreEmpresa,
      direccionFiscal: this.formEmpresa.value.direccionFiscal,
      pais: this.formEmpresa.value.pais,
      // El email de contacto (usamos el original ya que está deshabilitado)
      email: nuevoEmail // O this.empresa!.email si prefieres
      // --- IMPORTANTE: Manejo del Usuario Asociado ---
      // Aquí hay dos opciones:
      // 1. NO actualizar el usuario aquí: Se asume que la relación no cambia o se actualiza por separado.
      //    En este caso, no necesitas incluir 'usuario' en el objeto enviado.
      // 2. Actualizar TAMBIÉN el usuario aquí (si editaste campos de contacto):
      //    Necesitarías construir el objeto Usuario también y tu backend debería manejarlo.
      //    Por simplicidad, asumiremos la opción 1 por ahora.
      //    Si necesitas la opción 2, tendrías que construir 'usuarioActualizado' aquí.
      // usuario: this.empresa!.email // Mantenemos la referencia al usuario original cargado
                                       // O null si no quieres enviar el objeto usuario completo
    };

    // --- Llamada al Servicio de Actualización ---
    try {
        // Asume que tienes un método 'actualizarEmpresa' en tu servicio
        const resultado = await this.empresaService.actualizarEmpresa(empresaActualizada); // Usa await si el método devuelve Promise
        // O usa subscribe si devuelve Observable:
        // this.empresaService.actualizarEmpresa(empresaActualizada).subscribe({
        //    next: (empresaGuardada) => {
        //        alert('Empresa actualizada con éxito');
        //        this.volver();
        //    },
        //    error: (err) => alert('Error al actualizar empresa: ' + (err.error?.message || err.message || 'Error desconocido'))
        // });

        // Si usaste await:
         if (resultado) { // Asumiendo que actualizarEmpresa devuelve algo útil (o true/false)
             alert('Empresa actualizada con éxito');
             this.volver();
         } else {
              alert('No se pudo actualizar la empresa (respuesta inesperada)');
         }

    } catch (error: any) {
        console.error("Error en getDataEmpresa:", error);
        alert('Error al actualizar empresa: ' + (error.error?.message || error.message || 'Error desconocido'));
    } finally {
         // Volver a deshabilitar campos si los habilitaste (opcional)
         // this.formEmpresa.get('idEmpresa')?.disable();
         // this.formEmpresa.get('email')?.disable();
    }
  }
  
  
}
