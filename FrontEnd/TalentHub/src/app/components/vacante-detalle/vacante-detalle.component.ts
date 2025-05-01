import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EmpresaService } from '../../service/empresa.service';
import { SolicitudService } from '../../service/solicitud.service';
import { UsuarioService } from '../../service/usuario.service';
import { VacanteService } from '../../service/vacante.service';

@Component({
  selector: 'app-vacante-detalle',
  standalone: true,
  templateUrl: './vacante-detalle.component.html',
  styleUrls: ['./vacante-detalle.component.css']
})
export class VacanteDetalleComponent {
  tipo!: string;
  id!: number;
  email!:string;

  vacante: any = null;
  solicitud: any = null;
  empresa : any = null;
  usuario: any = null;
   

  constructor(
    private route: ActivatedRoute,
    private vacanteService: VacanteService,
    private solicitudService: SolicitudService,
    private location: Location,
    private empresaService: EmpresaService,
    private usuarioService: UsuarioService
    
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.tipo = params.get('type') || '';
      const param = params.get('param') || '';
  
      if (this.tipo === 'usuario') {
        this.email = decodeURIComponent(param); // ← decodifica el email
        this.cargarUsuario();
      } else {
        this.id = Number(param);
        if (!this.id) {
          console.error('ID inválido');
          return;
        }
      else if (this.tipo === 'vacante' || this.tipo === 'buscar ofertas' ) {
        this.cargarVacante();
      } else if (this.tipo === 'solicitud' || this.tipo === 'mis solicitudes') {
        this.cargarSolicitud();
      } else if (this.tipo === 'empresa') {
        this.cargarEmpresa();
      } else {
        console.warn('Tipo no reconocido:', this.tipo);
      }
    }
    });
  }
  cargarUsuario() {
    this.usuarioService.getUsuarioByEmail(this.email).subscribe({
      next: usuario => {this.usuario = usuario,
        console.log('Usuario cargada:', usuario);},

      error: err => console.error('Error cargando empresa:', err)
    });
  }
  cargarEmpresa() {
    this.empresaService.getEmpresaById(this.id).subscribe({
      next: empresa => {this.empresa = empresa,
        console.log('Empresa cargada:', empresa);},

      error: err => console.error('Error cargando empresa:', err)
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
