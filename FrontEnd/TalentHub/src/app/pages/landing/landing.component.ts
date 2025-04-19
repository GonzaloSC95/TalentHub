import { CommonModule } from '@angular/common';
import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { BotoneraComponent } from '../../components/botonera/botonera.component';
import { Usuario } from '../../interfaces/usuario';
import { EmpresaService } from '../../service/empresa.service';
import { UsuarioService } from '../../service/usuario.service';
import { VacanteService } from '../../service/vacante.service';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, BotoneraComponent],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.css',
})
export class LandingComponent implements OnInit, OnDestroy {
  // Inyección de dependencias
  route = inject(ActivatedRoute);
  usuarioService = inject(UsuarioService);
  empresaService = inject(EmpresaService);
  vacanteService = inject(VacanteService);

  // Propiedades
  usuarioLogueado: Usuario | null = null;
  pageTitle!: string;
  listType: string | null = null;
  config = { columns: [] as { key: string; label: string }[] };
  data: any[] = [];

  // Variable para guardar la suscripción a la ruta
  private routeSubscription: Subscription | null = null;
  // Variable para guardar la suscripción a la carga de datos (opcional pero bueno)
  private dataSubscription: Subscription | null = null;

  private getPageTitle(name: string | undefined, type: string): string {
    // Devuelve el título de la página según el tipo de vista
    switch (type.toUpperCase()) {
      case 'USUARIOS':
        return `Gestión de Usuarios`;
      case 'EMPRESAS':
        return `Gestión de Empresas`;
      case 'VACANTES':
        return `Gestión de Vacantes`;
      case 'USER':
        return `Bienvenido/a ${name} 😊`;
      case 'UNDEFINED':
        return `Error`;
      default:
        return `Error`;
    }
  }

  ngOnInit(): void {
    // Obtenemos el usuario logueado (esto puede hacerse una vez)
    this.usuarioLogueado = this.usuarioService.getUsuario();

    // --- SUSCRIBIRSE A CAMBIOS EN PARÁMETROS ---
    this.routeSubscription = this.route.paramMap.subscribe((params) => {
      // ESTA LÓGICA SE EJECUTA CADA VEZ QUE CAMBIAN LOS PARÁMETROS

      // Cancelar carga de datos anterior si estaba en progreso
      this.dataSubscription?.unsubscribe();

      // Limpiamos estado previo simple ANTES de procesar los nuevos params
      this.data = [];
      this.config.columns = [];
      this.listType = null; // Reiniciar listType

      const typeParam = params.get('type'); // Leemos el parámetro 'type' ACTUAL
      const emailParam = params.get('email'); // Leemos el parámetro 'email' ACTUAL

      if (typeParam) {
        // --- CASO 1: Vista de Lista de Admin ---
        this.listType = typeParam;
        this.configureAdminListView(typeParam); // Llamamos a configurar con el NUEVO type
      } else if (emailParam && this.usuarioLogueado) {
        // --- CASO 2: Vista de Detalles Post-Login ---
        this.listType = this.usuarioLogueado.rol ?? null;
        // Solo configura si hay rol, si no, listType queda null
        if (this.listType !== null) {
          this.configurePostLoginView(this.usuarioLogueado);
        } else {
          console.warn('Usuario logueado no tiene un rol definido.');
          this.pageTitle = `${this.getPageTitle(
            this.usuarioLogueado.nombre,
            'USER'
          )} (Rol no definido)`;
          this.config.columns = [
            { key: 'nombre', label: 'Nombre' },
            { key: 'email', label: 'Email' },
          ];
          this.data = [this.usuarioLogueado];
        }
      } else {
        // --- CASO 3: Ruta inesperada o usuario no disponible ---
        console.error('Parámetros de ruta inválidos o usuario no encontrado.');
        this.pageTitle = this.getPageTitle(undefined, 'ERROR');
      }
    });
  }

  // --- Lógica para la Vista de Lista de Admin ---
  configureAdminListView(type: string): void {
    let serviceCall: Observable<any[]>; // Asume que los servicios devuelven Observable

    switch (type) {
      case 'usuarios':
        this.pageTitle = this.getPageTitle(undefined, type);
        this.config.columns = [
          /* ... Columnas Usuarios ... */
          //{ key: 'id', label: 'ID' },
          { key: 'nombre', label: 'Nombre' },
          { key: 'apellidos', label: 'Apellidos' },
          { key: 'email', label: 'Email' },
          { key: 'rol', label: 'Rol' },
          { key: 'nombreEmpresa', label: 'Empresa' },
        ];
        serviceCall = this.usuarioService.getAllUsuarios();
        break;
      case 'empresas':
        this.pageTitle = this.getPageTitle(undefined, type);
        this.config.columns = [
          /* ... Columnas Empresas ... */ { key: 'idEmpresa', label: 'ID' },
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
          /* ... Columnas Vacantes ... */ { key: 'idVacante', label: 'ID' },
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

    // Llamada directa al servicio y suscripción simple
    serviceCall.subscribe({
      next: (response) => {
        this.data = response; // Asigna los datos recibidos
      },
      error: (err) => {
        console.error(`Error al cargar ${type}:`, err);
        // No mostramos mensaje en UI, solo consola
        this.data = []; // Asegurar que data esté vacía en caso de error
      },
    });
  }

  // --- Lógica para la Vista de Detalles Post-Login ---
  configurePostLoginView(usuario: Usuario): void {
    this.pageTitle = this.getPageTitle(usuario.nombre, 'USER');

    // Configurar columnas según el rol (igual que antes)
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
        // Datos básicos del admin
        { key: 'nombre', label: 'Nombre Admin' },
        { key: 'email', label: 'Email Admin' },
        { key: 'rol', label: 'Rol' },
      ];
    } else {
      this.config.columns = [{ key: 'info', label: 'Info Usuario' }];
    }

    this.data = [usuario]; // Los datos son solo el usuario logueado
  }
  ngOnDestroy(): void {
    // Es MUY importante desuscribirse para evitar fugas de memoria
    this.routeSubscription?.unsubscribe();
    this.dataSubscription?.unsubscribe(); // También limpia la de datos
  }
}
