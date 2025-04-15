import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BotoneraComponent } from "../../components/botonera/botonera.component";
import { Usuario } from '../../interfaces/usuario';
import { UsuarioService } from '../../service/usuario.service';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, BotoneraComponent],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.css'
})
export class LandingComponent {

  route = inject(ActivatedRoute);
  usuarioService = inject(UsuarioService);

  usuario: Usuario | null = null;
  email: string | null = null;

  config = {
    columns: [] as { key: string; label: string }[]
  };

  data: any[] = [];

  ngOnInit(): void {
    this.email = this.route.snapshot.paramMap.get('email');
    this.usuario = this.usuarioService.getUsuario();

    if (this.usuario) {
      if (this.usuario.rol === 'CLIENTE') {
        this.config.columns = [
          { key: 'nombre', label: 'Nombre' },
          { key: 'apellidos', label: 'Apellidos' },
          { key: 'email', label: 'Email' },
          { key: 'fechaRegistro', label: 'Fecha de registro' },
        ];
      } else if (this.usuario.rol === 'EMPRESA') {
        this.config.columns = [
          { key: 'nombre', label: 'Nombre' },
          { key: 'apellidos', label: 'Apellidos' },
          { key: 'email', label: 'Email' },
          { key: 'empresa', label: 'Empresa' },

        ];
      } else if (this.usuario.rol ==='ADMON'){
        this.config.columns = [
          { key: 'nombre', label: 'Nombre' },
          { key: 'apellidos', label: 'Apellidos' },
          { key: 'email', label: 'Email' },
        ];
      }

      this.data = [this.usuario];
    }
  }

}
