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
  

  editar() {
    this.router.navigate([`/${this.type}/editar`, this.item?.id || this.item?.email]);
  }

  verDetalle() {
    this.router.navigate([`/${this.type}/detalle`, this.item?.id || this.item?.email]);
  }
}
