import { Component } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  reactiveForm!: FormGroup;

  constructor() {
    this.reactiveForm = new FormGroup({
      email: new FormControl(null, [Validators.required]),
      password: new FormControl(null, [Validators.required]),
    });
  }

  ngOnInit(): void {
    this.reactiveForm = new FormGroup({
      email: new FormControl(null, [Validators.required]),
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

  onSubmit(): void {
    if (this.reactiveForm.valid) {
      console.log('Formulario enviado:', this.reactiveForm.value);
      // Aquí puedes enviar los datos al backend
    } else {
      this.reactiveForm.markAllAsTouched();
    }
  }
}
