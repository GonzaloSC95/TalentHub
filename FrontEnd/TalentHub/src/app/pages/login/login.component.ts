import { Component, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
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
      const response = await this.usuarioService.getUsuarioByLogin(
        this.usuario //ana.martinez@example.com passana3
      );
      console.log(response);
    } else {
      this.reactiveForm.markAllAsTouched();
    }
  }
}
