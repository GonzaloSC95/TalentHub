import { Component, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { Usuario } from '../../interfaces/usuario';
import { UsuarioService } from '../../service/usuario.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  router = inject(Router)
  //Inyección de dependencias
  usuarioService = inject(UsuarioService);
  // Propiedades
  usuario!: Usuario;
  reactiveForm!: FormGroup;

  constructor() {}

  ngOnInit(): void {
    this.reactiveForm = new FormGroup({
      email: new FormControl(null, [
        Validators.required,
        Validators.pattern(/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/),
      ]),
      password: new FormControl(null, [Validators.required]),
    });
  }

  checkControl(
    formControlName: string,
    validador: string
  ): boolean | undefined {
    return (
      this.reactiveForm.get(formControlName)?.hasError(validador) &&
      this.reactiveForm.get(formControlName)?.touched
    );
  }

  async onSubmit(): Promise<void> {
    if (this.reactiveForm.valid) {
      console.log('Formulario enviado:', this.reactiveForm.value);
      // Aquí puedes enviar los datos al backend
      this.usuario = {
        email: this.reactiveForm.get('email')?.value,
        password: this.reactiveForm.get('password')?.value,
      };
      const response = await this.usuarioService.getUsuarioByLogin(this.usuario)//ana.martinez@example.com passana3 

      console.log('Respuesta del backend:', response);
      if (response) {
        this.usuario = response as Usuario;
        this.usuarioService.setUsuario(this.usuario);
        Swal.fire({
          title: '¡Bienvenid@!😊',
          text: `Has iniciado sesión correctamente: ${this.usuario.nombre} ${this.usuario.apellidos}`,
          icon: 'success',
          showCancelButton: false,
          showCloseButton: true,
          confirmButtonColor: getComputedStyle(document.documentElement)
            .getPropertyValue('--primary-color')
            .trim(),
          confirmButtonText: 'Continuar',
        }).then((result) => {
          if (result.isConfirmed) {
            //TODO: redirigir al usuario a otra página o realizar otras acciones
            this.reactiveForm.reset();
            this.router.navigate(['/home'])
          }
        });
      } else {
        Swal.fire({
          title: 'Error al iniciar sesión',
          text: 'Usuario o contraseña incorrectos.',
          icon: 'error',
          showCancelButton: false,
          showCloseButton: true,
          confirmButtonColor: getComputedStyle(document.documentElement)
            .getPropertyValue('--primary-color')
            .trim(),
          confirmButtonText: 'Continuar',
        }).then((result) => {
          if (result.isConfirmed) {
            this.reactiveForm.reset();
          }
        });
      }
    } else {
      //Marcan todos los controles del formulario como "tocados" (touched), para que aparezcan los mensajes de validación.
      this.reactiveForm.markAllAsTouched();
    }
  }
}
