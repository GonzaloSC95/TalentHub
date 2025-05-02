import { Component, inject } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import Swal from 'sweetalert2';
import { Empresa } from '../../interfaces/empresa';
import { Usuario } from '../../interfaces/usuario';
import { PaisesService } from '../../service/paises.service';
import { UsuarioService } from '../../service/usuario.service';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './registro.component.html',
  styleUrl: './registro.component.css',
})
export class RegistroComponent {

  //Inyección de dependencias
  usuarioService = inject(UsuarioService);
  paisesService = inject(PaisesService);
  router = inject(Router);

  // Propiedades
  usuario!: Usuario;
  empresa!: Empresa;
  reactiveForm!: FormGroup;
  esRegistroPublico = true;




  constructor() {}

  ngOnInit(): void {
    
    const usuarioActual = this.usuarioService.getUsuario();
    this.esRegistroPublico = !usuarioActual;
  
    // Inicializamos el formulario reactivo primero
    this.reactiveForm = new FormGroup(
      {
        email: new FormControl(null, [
          Validators.required,
          Validators.pattern(/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/),
        ]),
        password: new FormControl(null, [
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(15),
        ]),
        nombre: new FormControl(null, [Validators.required]),
        apellidos: new FormControl(null, [Validators.required]),
        empresa: new FormControl(null, []),
        cif: new FormControl(null, []),
        direccion: new FormControl(null, []),
        pais: new FormControl(null, []),
        rol: new FormControl('CLIENTE', [Validators.required]),
        lopd: new FormControl(false, [Validators.requiredTrue]),
      },
      [this.empresaValidator]
    );
        // --- DESHABILITAR LOPD SI ES ADMIN ---
        if (!this.esRegistroPublico) {
          // Si no es registro público (es admin), deshabilita el control LOPD
      //  this.reactiveForm.get('lopd')?.disable();
          //si es admin
           this.reactiveForm.get('lopd')?.setValue(true);
        }
  
    // Luego registramos el valueChanges
    this.reactiveForm.get('rol')?.valueChanges.subscribe(() => {
      this.reactiveForm.updateValueAndValidity();
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
      } else {
        empresa?.setErrors(null);
      }
  
      if (!cif?.value || cif.value.trim() === '') {
        cif?.setErrors({ required: true });
      } else {
        cif?.setErrors(null);
      }
  
      if (!direccion?.value || direccion.value.trim() === '') {
        direccion?.setErrors({ required: true });
      } else {
        direccion?.setErrors(null);
      }
  
      if (!pais?.value || pais.value.trim() === '') {
        pais?.setErrors({ required: true });
      } else {
        pais?.setErrors(null);
      }
    } else {
      empresa?.setErrors(null);
      cif?.setErrors(null);
      direccion?.setErrors(null);
      pais?.setErrors(null);
    }
  
    return null;
  }
  
  volver() {
    this.router.navigate(['/admin/list/usuarios']);
    }
  async onSubmit(): Promise<void> {
    console.log(this.reactiveForm.value)
    console.log(this.reactiveForm.status)
    console.log(this.reactiveForm.errors)
    
    if (this.reactiveForm.valid) {
      let response = undefined;
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
          email: this.reactiveForm.get('email')?.value,
        };
        //Enviamos los datos al backend
        response = await this.usuarioService.registrarUsuarioEmpresa(
          this.usuario,
          this.empresa
        );
      } else {
        this.usuario = {
          email: this.reactiveForm.get('email')?.value,
          nombre: this.reactiveForm.get('nombre')?.value,
          apellidos: this.reactiveForm.get('apellidos')?.value,
          password: this.reactiveForm.get('password')?.value,
          enabled: 1,
          fechaRegistro: new Date(),
          rol: this.reactiveForm.get('rol')?.value,
        };
        //Enviamos los datos al backend
        response = await this.usuarioService.registrarUsuario(this.usuario);
      }
      //Comprobamos la respuesta del backend
      console.log('Respuesta del backend:', response);
      if (response) {
        this.usuario = response as Usuario;
        this.usuarioService.setUsuario(this.usuario);
        Swal.fire({
          title: '¡Bienvenid@!😊',
          text: `Te has registrado correctamente: ${this.usuario.nombre} ${this.usuario.apellidos}`,
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
            if(!this.esRegistroPublico){
              this.router.navigate(['/admin/list/usuarios']);
            } else {
              this.router.navigate([`/landing/${this.usuario.email}`]);
            }
          }
        });
      }
      //Si no se ha podido registrar el usuario, mostramos un mensaje de error.
      else {
        Swal.fire({
          title: 'Error en el registro',
          text: 'No se ha podido registrar el usuario.',
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
