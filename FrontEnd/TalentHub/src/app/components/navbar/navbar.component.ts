import { Component, inject } from '@angular/core';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { filter } from 'rxjs';
import { Usuario } from '../../interfaces/usuario';
import { UsuarioService } from '../../service/usuario.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {
  //Router para la navegación
  router = inject(Router);
  usuarioService= inject (UsuarioService);
  usuario :Usuario | null = null;

  //Arrays dinámicos para los botones de la barra de navegación
  arrNavBarButtons: { text: string; route: string }[] = [];
  arrNavBarOptions: { text: string; route: string }[] = [];

  //Botones de la barra de navegación
  loginBtn = {
    text: 'Login',
    route: '/login',
  };
  registerBtn = {
    text: 'Registro',
    route: '/registro',
  };
  logOutBtn = {
    text: 'Cerrar sesión',
    route: '/home',
  };

  //Opciones de la barra de navegación
  //TODO: ¿?

  ngOnInit(): void {
    //Rellenamos el array de botones de la barra de navegación
    this.usuario = this.usuarioService.getUsuario();
    this.usuarioService.usuario$.subscribe(usuario => {
      this.usuario = usuario!;
    });
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        switch (event.urlAfterRedirects) {
          case '/login':
            this.arrNavBarButtons = [this.registerBtn];
            break;
          case '/registro':
            this.arrNavBarButtons = [this.loginBtn];
            break;
          case '/home':
            this.arrNavBarButtons = [this.loginBtn, this.registerBtn];
            break;
          default:
            this.arrNavBarButtons = [this.logOutBtn];
            break;
        }
      });
  }
}
