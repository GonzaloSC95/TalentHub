import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { EmpresaService } from '../../service/empresa.service';
import { UsuarioService } from '../../service/usuario.service';
import { VacanteService } from '../../service/vacante.service';

@Component({
  selector: 'app-botonera',
  standalone: true,
  imports: [],
  templateUrl: './botonera.component.html',
  styleUrl: './botonera.component.css'
})
export class BotoneraComponent {

  menuOpen = false;

  @Input() item: any;           // El objeto actual 
  @Input() type: string = '';  // Tipo de entidad
  @Output() eliminado = new EventEmitter<any>();//para recergar pagina 

  usuarioService = inject(UsuarioService);
  empresaService = inject(EmpresaService);
  vacanteService = inject(VacanteService);


  constructor(private router: Router) {}

  toggleMenu() {
    this.menuOpen = !this.menuOpen;
  }

  async eliminar() {
    const usuario = this.usuarioService.getUsuario();
    

    //si no lo compruebo el usuario podría ser null y no deja hacer usuario.rol
    if (!usuario) {
      await Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'No hay usuario logueado.',
        confirmButtonText: 'Aceptar'
      });
      return;
    }
  
    const rol = (usuario.rol ?? '').toLowerCase();
  
    if (this.type === 'vacante' && rol === 'cliente') {
      await Swal.fire({
        icon: 'warning',
        title: 'Acción no permitida',
        text: 'Como eres un usuario no puedes eliminar vacante, solo tu solicitud!!!.',
        confirmButtonText: 'Aceptar'
      });
      return;
    }


    const result = await Swal.fire({
      title: '¿Estás seguro?',
      text: `Esta acción eliminará su ${this.type}`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    });
  
    if (!result.isConfirmed)return;
      let eliminado = false;
      switch (this.type) {
        case 'usuario':
          if (this.item?.email) {
            eliminado = await this.usuarioService.deleteUsuario(this.item);
          }
          break;
    
        case 'empresa':
          if (this.item?.idEmpresa) {
            eliminado = await this.empresaService.deleteEmpresa(this.item);
          }
          break;
    
        case 'vacante':
          if (this.item?.idVacante) {
            eliminado = await this.vacanteService.deleteVacante(this.item);
          }
          break;
    
        default:
          console.warn('Tipo no soportado:', this.type);
      }
    
      if (eliminado) {
        Swal.fire('Eliminado!', `${this.type} eliminada correctamente.`, 'success');
        // aquí puedes emitir un evento o refrescar una lista si es necesario
        this.eliminado.emit(this.item);  // <<< Notifica al padre
      } else {
        Swal.fire('Error', `No se pudo eliminar el ${this.type}.`, 'error');
      }
    
  }
  

  modificar() {
    
    const tipo = this.type.toLowerCase().replace(/\s+/g, '');
    let id = this.getId();
  
    switch (this.type) {
      case 'vacante':
        id = this.item?.idVacante;
        break;
      case 'usuario':
        id = this.item?.email;
        break;
      case 'solicitud':
        id = this.item?.idSolicitud;
        break;
      default:
        id = this.item?.id || this.item?.email; // fallback
    }
  
    if (!id) {
      console.error('No se pudo obtener el id para modificar:', this.item);
      return;
    }
  
    this.router.navigate(['modificar2', tipo, id]);
  }
  
  
  // el ver detalles evalua id y se queda con la primera y toma siempre ese
  //método para cada cosa y su id, se puede meter más

  private getId(): number | string | null {
    if (!this.item) return null;
  
    switch (this.type.toLowerCase()) {
      case 'solicitud':
        return this.item.idSolicitud ?? null;
      case 'vacante':
        return this.item.idVacante ?? null;
      case 'usuario':
        return this.item.id ?? this.item.email ?? null;
      case 'empresa':
        return this.item.idEmpresa ?? null;
      default:
        return this.item.id ?? null;
    }
  }
  

  //verDetalle() {
   // this.router.navigate([`vacante/detalle`, this.item?.id || this.item?.idVacante]);}
   verDetalle() {
  const tipo = this.type.toLowerCase().replace(/\s+/g, ''); //para espacios
  const id = this.getId();

  if (!id) {
    console.error('No se pudo obtener el id para el item:', this.item);
    return;
  }
  const param = encodeURIComponent(id); 
  this.router.navigate(['detalle', tipo, id]);
}

//modificar() {
//  const tipo = this.type.toLowerCase().replace(/\s+/g, '');
//const id = this.getId();

// if (!id) {
//    console.error('No se pudo obtener el id para modificar:', this.item);
//    return;
//  }

//  this.router.navigate(['modificar', tipo, id]);
}

