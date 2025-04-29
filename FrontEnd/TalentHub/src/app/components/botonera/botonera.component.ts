import { Component, inject, Input } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { UsuarioService } from '../../service/usuario.service';

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

  usuarioService = inject(UsuarioService);

  constructor(private router: Router) {}

  toggleMenu() {
    this.menuOpen = !this.menuOpen;
  }

  async eliminar() {
    const result = await Swal.fire({
      title: '¿Estás seguro?',
      text: `Esta acción eliminará el ${this.type}`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    });
  
    if (result.isConfirmed) {
      let eliminado = false;
  
      if (this.type === 'usuario') {
        const usuario = this.item;  // Obtenemos el objeto completo del usuario
        if (usuario) {
          eliminado = await this.usuarioService.deleteUsuario(usuario);  // Enviamos el usuario completo
        }
      }
  
      if (eliminado) {
        Swal.fire(
          'Eliminado!',
          `${this.type} eliminado correctamente.`,
          'success'
        );
        
      } else {
        Swal.fire('Error', `No se pudo eliminar el ${this.type}.`, 'error');
      }
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
  
    this.router.navigate(['modificar', tipo, id]);
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

