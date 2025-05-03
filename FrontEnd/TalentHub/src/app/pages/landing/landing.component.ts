import { CommonModule } from '@angular/common';
import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { map, Observable, of, Subscription } from 'rxjs';
import { BotoneraComponent } from '../../components/botonera/botonera.component';
import { Usuario } from './../../interfaces/usuario';
import { SolicitudService } from './../../service/solicitud.service';

import { FormsModule } from '@angular/forms';
import { EmpresaService } from '../../service/empresa.service';
import { UsuarioService } from '../../service/usuario.service';
import { VacanteService } from '../../service/vacante.service';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, BotoneraComponent, FormsModule],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.css',
})
export class LandingComponent implements OnInit, OnDestroy {

  // Inyección de dependencias
  route = inject(ActivatedRoute);
  router = inject(Router);
  usuarioService = inject(UsuarioService);
  empresaService = inject(EmpresaService);
  vacanteService = inject(VacanteService);
  solicitudService = inject(SolicitudService); 

  // Propiedades
  usuarioLogueado: Usuario | null = null;
  pageTitle!: string;
  listType: string | null = null;
  config = { columns: [] as { key: string; label: string }[] };
  data: any[] = [];
  typeForBotonera: string = ''; // para los diferentes type
  estadoFiltro = '';//filtro usuarios admin
  todosLosUsuarios: Usuario[] = [];

  // Nueva propiedad para solicitudes adjudicadas
  solicitudesAdjudicadas: any[] = [];

  // Variables para manejar suscripciones
  private routeSubscription: Subscription | null = null;
  private dataSubscription: Subscription | null = null;
  private adjudicadasSubscription: Subscription | null = null;

  // NUEVAS PROPIEDADES PARA FILTRAR VACANTES CREADAS
  filtroNombre: string = '';
  filtroDescripcion: string = '';
  filtroSalarioMin: number | null = null;
  filtroSalarioMax: number | null = null;

  ngOnInit(): void {
    this.usuarioLogueado = this.usuarioService.getUsuario();
    this.filtrar(this.estadoFiltro);

    this.routeSubscription = this.route.paramMap.subscribe((params) => {
      this.dataSubscription?.unsubscribe();
      this.adjudicadasSubscription?.unsubscribe();

      this.data = [];
      this.solicitudesAdjudicadas = [];
      this.config.columns = [];
      this.listType = null;
      this.typeForBotonera = '';

      const typeParam = params.get('type');
      const emailParam = params.get('email');

      const urlSegments = this.route.snapshot.url.map(segment => segment.path);
      const urlPath = urlSegments.join('/');

      if (typeParam && urlPath.startsWith('admin/list')) {
        this.listType = typeParam;
        this.typeForBotonera = this.mapListTypeToBotoneraType(typeParam);
        this.configureAdminListView(typeParam);
      } else if (typeParam && urlPath.startsWith('user/list')) {
        this.listType = typeParam;
        this.typeForBotonera = this.mapListTypeToBotoneraType(typeParam);
        this.configureUserListView(typeParam);
      } else if (emailParam && this.usuarioLogueado) {
        this.listType = this.usuarioLogueado.rol ?? null;
        this.typeForBotonera = this.mapListTypeToBotoneraType(this.listType);

        if (this.listType !== null) {
          this.configurePostLoginView(this.usuarioLogueado);
        } else {
          console.warn('Usuario logueado no tiene un rol definido.');
          this.pageTitle = `${this.getPageTitle(this.usuarioLogueado.nombre, 'USER')} (Rol no definido)`;
          this.config.columns = [
            { key: 'nombre', label: 'Nombre' },
            { key: 'email', label: 'Email' },
          ];
          this.data = [this.usuarioLogueado];
        }
      } else {
        console.error('Parámetros de ruta inválidos o usuario no encontrado.');
        this.pageTitle = this.getPageTitle(undefined, 'ERROR');
      }
    });
  }

  // NUEVO MÉTODO: Aplicar filtros y recargar las vacantes filtradas en estado CREADA
  aplicarFiltrosVacantes(): void {
    // Construimos el objeto filtros para el servicio
    const filtros: {
      nombre?: string,
      descripcion?: string,
      salarioMin?: number,
      salarioMax?: number
    } = {};

    if (this.filtroNombre.trim() !== '') {
      filtros.nombre = this.filtroNombre.trim();
    }

    if (this.filtroDescripcion.trim() !== '') {
      filtros.descripcion = this.filtroDescripcion.trim();
    }

    if (this.filtroSalarioMin != null && !isNaN(this.filtroSalarioMin)) {
      filtros.salarioMin = this.filtroSalarioMin;
    }

    if (this.filtroSalarioMax != null && !isNaN(this.filtroSalarioMax)) {
      filtros.salarioMax = this.filtroSalarioMax;
    }

    this.dataSubscription?.unsubscribe();

    // Usar el método que hay en el servicio para obtener vacantes filtradas
    this.dataSubscription = this.vacanteService.getVacantesFiltradas(filtros).subscribe({
      next: (vacantes) => {
        this.data = vacantes;

        // Limpiar filtros después de mostrar resultados
      this.filtroNombre = '';
      this.filtroDescripcion = '';
      this.filtroSalarioMin = null;
      this.filtroSalarioMax = null;

      },
      error: (error) => {
        console.error('Error al cargar vacantes filtradas:', error);
        this.data = [];
      },
    });
  }

  quitarDeLista(itemEliminado: any) {
    if (this.typeForBotonera === 'vacante') {
      this.data = this.data.filter(item => item.idVacante !== itemEliminado.idVacante);
    } else if (this.typeForBotonera === 'usuario') {
      this.data = this.data.filter(item => item.email !== itemEliminado.email);
    } else if (this.typeForBotonera === 'empresa') {
      this.data = this.data.filter(item => item.idEmpresa !== itemEliminado.idEmpresa);
    } else if (this.typeForBotonera === 'solicitud') {
      this.solicitudesAdjudicadas = this.solicitudesAdjudicadas.filter(item => item.idSolicitud !== itemEliminado.idSolicitud);
    } else {
      console.warn('Tipo desconocido al intentar quitar de lista:', this.typeForBotonera);
    }
  }

  private getPageTitle(name: string | undefined, type: string): string {
    switch (type.toUpperCase()) {
      case 'USUARIOS':
        return `Gestión de Usuarios`;
      case 'EMPRESAS':
        return `Gestión de Empresas`;
      case 'VACANTES':
        return `Gestión de Vacantes`;
      case 'USER':
        return `Bienvenido/a ${name} ?`;
      case 'MI PERFIL':
        return `Mi Perfil`;
      case 'MIS SOLICITUDES':
        return `Mis Solicitudes`;
      case 'BUSCAR OFERTAS':
        return `Buscar Ofertas`;
      case 'UNDEFINED':
        return `Error`;
      default:
        return `Error`;
    }
  }
  //en la lista de adjudicadas habia problemas con el tipo, asignamos y se pueden poner todos
  private mapListTypeToBotoneraType(type: string | null): string {
    if (!type) return '';

    const lowerType = type.toLowerCase();

    switch (lowerType) {
      case 'usuarios':
        return 'usuario';
      case 'empresas':
        return 'empresa';
      case 'vacantes':
        return 'vacante';
      case 'mis solicitudes':
        return 'solicitud'; // por si acaso hay problema con el espacio
      case 'mi perfil':
        return 'usuario';
      case 'buscar ofertas':
        return 'vacante';
      default:
        return lowerType.replace(/\s+/g, ''); // para los espacios
    }
  }

  filtrar(estado: string) {
    this.estadoFiltro = estado;
    this.data = this.todosLosUsuarios.filter(c => c.rol === estado);
  }
  clearFiltrar() {
    this.estadoFiltro = '';
    this.data = this.todosLosUsuarios
  }
  // Método para hacer logout
  alta(): void {
    // Redirigimos a la página de inicio o login con parametros de vuelta
    this.router.navigate(['/registro'], {
      state: { returnUrl: '/admin/list/usuarios' }
    });
  }
  // --- Lógica para la Vista de Lista de Admin ---
  configureAdminListView(type: string): void {
    let serviceCall: Observable<any[]>;

    switch (type) {
      case 'usuarios':
        this.pageTitle = this.getPageTitle(undefined, type);
        this.config.columns = [
          { key: 'nombre', label: 'Nombre' },
          { key: 'apellidos', label: 'Apellidos' },
          { key: 'email', label: 'Email' },
          { key: 'rol', label: 'Rol' },
          { key: 'enabled', label: 'Estado'}
          //{ key: 'nombreEmpresa', label: 'Empresa' },
        ];
        serviceCall = this.usuarioService.getAllUsuarios();
        break;
      case 'empresas':
        this.pageTitle = this.getPageTitle(undefined, type);
        this.config.columns = [
          { key: 'idEmpresa', label: 'ID' },
          { key: 'nombreEmpresa', label: 'Nombre Empresa' },
          { key: 'cif', label: 'CIF' },
          { key: 'direccionFiscal', label: 'Dirección' },
          { key: 'pais', label: 'País' },
          { key: 'email', label: 'Email Contacto' },
        ];
        serviceCall = this.empresaService.getAllEmpresas();
        break;
      case 'vacantes':
        this.pageTitle = this.getPageTitle(undefined, type);
        this.config.columns = [
          { key: 'idVacante', label: 'ID' },
          { key: 'nombre', label: 'Título' },
          { key: 'descripcion', label: 'Descripción Breve' },
          { key: 'idEmpresa', label: 'Empresa' },
          { key: 'nombreEmpresa', label: 'Empresa' },
          { key: 'estatus', label: 'Estado' },
          
        ];
        serviceCall = this.vacanteService.getAllVacantes();
        break;
      default:
        console.error(`Tipo de lista desconocido: ${type}`);
        this.pageTitle = this.getPageTitle(undefined, 'UNDEFINED');
        return;
    }

    serviceCall.subscribe({
      next: (response) => {
        this.todosLosUsuarios =response;
        this.data = response;
      },
      error: (err) => {
        console.error(`Error al cargar ${type}:`, err);
        this.data = [];
      },
    });
  }

  // --- Lógica para la Vista de Detalles Post-Login ---
  configurePostLoginView(usuario: Usuario): void {
    this.pageTitle = this.getPageTitle(usuario.nombre, 'USER');

    if (usuario.rol === 'CLIENTE') {
      this.config.columns = [
        { key: 'nombre', label: 'Nombre' },
        { key: 'apellidos', label: 'Apellidos' },
        { key: 'email', label: 'Email' },
      ];
    } else if (usuario.rol === 'EMPRESA') {
      this.config.columns = [
        { key: 'nombre', label: 'Nombre Contacto' },
        { key: 'apellidos', label: 'Apellidos Contacto' },
        { key: 'email', label: 'Email Contacto' },
        { key: 'nombreEmpresa', label: 'Nombre Empresa' },
      ];
    } else if (usuario.rol === 'ADMON') {
      this.config.columns = [
        { key: 'nombre', label: 'Nombre Admin' },
        { key: 'email', label: 'Email Admin' },
        { key: 'rol', label: 'Rol' },
      ];
    } else {
      this.config.columns = [{ key: 'info', label: 'Info Usuario' }];
    }

    this.data = [usuario];
  }

  // --- Lógica para la Vista de Lista de User ---
  configureUserListView(type: string): void {
    let serviceCall: Observable<any[]> | null = null;

    switch (type) {
      case 'mi perfil':
        this.pageTitle = this.getPageTitle(undefined, type);
        this.config.columns = [
          { key: 'nombre', label: 'Nombre' },
          { key: 'apellidos', label: 'Apellidos' },
          { key: 'email', label: 'Email' },
          { key: 'rol', label: 'Rol' },
          { key: 'nombreEmpresa', label: 'Empresa' },
        ];
        if (this.usuarioLogueado) {
          this.data = [this.usuarioLogueado];
        } else {
          this.data = [];
        }
        return;

      case 'mis solicitudes':
        this.pageTitle = this.getPageTitle(undefined, type);
        this.config.columns = [
          { key: 'idSolicitud', label: 'ID' },
          // { key: 'vacante', label: 'Título' },
          { key: 'comentarios', label: 'Comentarios' },
          { key: 'estado', label: 'Estado' },
          { key: 'fecha', label: 'Fecha' },
          // { key: 'candidato', label: 'Candidato' },
          { key: 'archivo', label: 'Archivo' },
          { key: 'curriculum', label: 'Currículum' }
        ];

        if (this.usuarioLogueado?.email) {
          // Cancelamos suscripciones previas
          this.dataSubscription?.unsubscribe();
          this.adjudicadasSubscription?.unsubscribe();

          // Cargamos solicitudes presentadas filtradas por email
          serviceCall = this.solicitudService.getSolicitudesPresentadas().pipe(
            map((solicitudes: any[]) =>
              solicitudes.filter((s: any) => s.emailUsuario === this.usuarioLogueado?.email)
            )
          );

          // Cargamos solicitudes adjudicadas filtradas por email igual que en presentadas pero en otro observable
          this.adjudicadasSubscription = this.solicitudService.getSolicitudesAdjudicadas().pipe(
            map((solicitudes: any[]) =>
              solicitudes.filter((s: any) => s.emailUsuario === this.usuarioLogueado?.email)
            )
          ).subscribe({
            next: (adjudicadas) => {
              this.solicitudesAdjudicadas = adjudicadas;
            },
            error: (error) => {
              console.error('Error cargando solicitudes adjudicadas:', error);
              this.solicitudesAdjudicadas = [];
            }
          });

        } else {
          console.warn('Usuario no logueado, no se puede filtrar solicitudes');
          serviceCall = of([]);
          this.solicitudesAdjudicadas = [];
        }
        break;

      case 'buscar ofertas':
        this.pageTitle = this.getPageTitle(undefined, type);
        this.config.columns = [
          { key: 'idVacante', label: 'ID' },
          { key: 'nombre', label: 'Título' },
          { key: 'descripcion', label: 'Descripción Breve' },
          { key: 'idEmpresa', label: 'Empresa' },
          { key: 'nombreEmpresa', label: 'Empresa' },
          { key: 'estatus', label: 'Estado' },
          { key: 'salario', label: 'Salario' },
        ];

        // En vez de usar getVacantesCreadas, llamamos a aplicarFiltrosVacantes()
        this.aplicarFiltrosVacantes();

        return; // Salimos para prevenir carga doble

      default:
        this.pageTitle = this.getPageTitle(undefined, 'UNDEFINED');
        this.config.columns = [];
        this.data = [];
        return;
    }

    if (serviceCall) {
      this.dataSubscription?.unsubscribe();
      this.dataSubscription = serviceCall.subscribe({
        next: (response) => {
          this.data = response;
        },
        error: (error) => {
          console.error(`Error cargando datos para ${type}:`, error);
          this.data = [];
        }
      });
    }
  }

  cargarTodasVacantes(): void {
    this.dataSubscription?.unsubscribe();
  
    this.vacanteService.getVacantesCreadas().subscribe({
      next: (vacantes) => {
        this.data = vacantes;
  
        // Limpiar filtros para que se refleje vacío
        this.filtroNombre = '';
        this.filtroDescripcion = '';
        this.filtroSalarioMin = null;
        this.filtroSalarioMax = null;
      },
      error: (error) => {
        console.error('Error cargando todas las vacantes:', error);
        this.data = [];
      }
    });
  }

  ngOnDestroy(): void {
    this.routeSubscription?.unsubscribe();
    this.dataSubscription?.unsubscribe();
    this.adjudicadasSubscription?.unsubscribe();
  }
}
