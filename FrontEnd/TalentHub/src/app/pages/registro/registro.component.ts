import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Usuario } from '../../interfaces/usuario';
import { Empresa } from '../../interfaces/empresa';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './registro.component.html',
  styleUrl: './registro.component.css',
})
export class RegistroComponent {
  usuario!: Usuario;
  empresa!: Empresa;
  reactiveForm!: FormGroup;

  constructor() {}

  ngOnInit(): void {
    this.reactiveForm = new FormGroup(
      {
        email: new FormControl(null, [Validators.required]),
        password: new FormControl(null, [Validators.required]),
        nombre: new FormControl(null, [Validators.required]),
        apellidos: new FormControl(null, [Validators.required]),
        empresa: new FormControl(null, []),
        cif: new FormControl(null, []),
        direccion: new FormControl(null, []),
        pais: new FormControl(null, []),
        rol: new FormControl('CLIENTE', [Validators.required]),
      },
      [this.empresaValidator]
    );
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

  isRolEmpresa(): boolean | undefined {
    return this.reactiveForm.get('rol')?.value === 'EMPRESA';
  }

  empresaValidator(formGroup: AbstractControl): any {
    const empresa = formGroup.get('empresa');
    const cif = formGroup.get('cif');
    const direccion = formGroup.get('direccion');
    const pais = formGroup.get('pais');
    const rol = formGroup.get('rol')?.value;

    if (rol === 'EMPRESA') {
      if (!empresa?.value || empresa.value.trim() === '') {
        empresa?.setErrors({ required: true });
      }
      if (!cif?.value || cif.value.trim() === '') {
        cif?.setErrors({ required: true });
      }
      if (!direccion?.value || direccion.value.trim() === '') {
        direccion?.setErrors({ required: true });
      }
      if (!pais?.value || pais.value.trim() === '') {
        pais?.setErrors({ required: true });
      }
    } else {
      empresa?.setErrors(null);
      cif?.setErrors(null);
      direccion?.setErrors(null);
      pais?.setErrors(null);
    }
    return null;
  }

  onSubmit(): void {
    if (this.reactiveForm.valid) {
      console.log('Formulario enviado:', this.reactiveForm.value);
      // Creamos las interfaces de usuario y empresa
      if (this.isRolEmpresa()) {
        this.usuario = {
          email: this.reactiveForm.get('email')?.value,
          nombre: this.reactiveForm.get('empresa')?.value,
          apellidos: this.reactiveForm.get('direccion')?.value,
          password: this.reactiveForm.get('cif')?.value,
          enabled: 1,
          fechaRegistro: new Date(),
          rol: this.reactiveForm.get('rol')?.value,
        };
        this.empresa = {
          cif: this.reactiveForm.get('cif')?.value,
          nombreEmpresa: this.reactiveForm.get('empresa')?.value,
          direccionFiscal: this.reactiveForm.get('direccion')?.value,
          pais: this.reactiveForm.get('pais')?.value,
          usuario: this.reactiveForm.get('email')?.value,
        };
      }
      // Aquí puedes enviar los datos al backend
    } else {
      this.reactiveForm.markAllAsTouched();
    }
  }
}
