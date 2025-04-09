import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './registro.component.html',
  styleUrl: './registro.component.css',
})
export class RegistroComponent {
  reactiveForm!: FormGroup;

  constructor() {
    this.reactiveForm = new FormGroup({
      empresa: new FormControl(null, [Validators.required]),
      email: new FormControl(null, [Validators.required]),
      cif: new FormControl(null, [Validators.required]),
      direccion: new FormControl(null, [Validators.required]),
      pais: new FormControl(null, [Validators.required]),
      politica: new FormControl(null, [Validators.required]),
    });
  }

  ngOnInit(): void {
    this.reactiveForm = new FormGroup({
      empresa: new FormControl(null, [Validators.required]),
      email: new FormControl(null, [Validators.required]),
      cif: new FormControl(null, [Validators.required]),
      direccion: new FormControl(null, [Validators.required]),
      pais: new FormControl(null, [Validators.required]),
      politica: new FormControl(null, [Validators.required]),
    });
  }

  campoInvalido(campo: string): boolean {
    const control = this.reactiveForm.get(campo);
    return !!(control && control.invalid && control.touched);
  }

  onSubmit(): void {
    if (this.reactiveForm.valid) {
      console.log('Formulario enviado:', this.reactiveForm.value);
      // Aquí puedes enviar los datos al backend
    } else {
      this.reactiveForm.markAllAsTouched();
    }
  }
}
