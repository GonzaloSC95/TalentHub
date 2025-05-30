import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import {
  BehaviorSubject,
  catchError,
  lastValueFrom,
  map,
  Observable,
  of,
} from 'rxjs';
import { environment } from '../../environments/environment';
import { Empresa } from '../interfaces/empresa';
import { Usuario } from '../interfaces/usuario';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {
  // BehaviorSubject para mantener usuario actual
  private usuarioSubject = new BehaviorSubject<Usuario | null>(null);
  usuario$ = this.usuarioSubject.asObservable(); // observable para componentes
  //Inyección de dependencias
  httpClient = inject(HttpClient);
  // URL de la API
  private apiUrl: string = environment.baseUrl + 'talenthub/api/usuario';
  constructor() {}

  getUsuarioByEmail(email: string):Observable<Usuario>{
    const url = `${this.apiUrl}/${email}`;
    return this.httpClient.get<Usuario>(url).pipe(
      catchError(this.handleError<Usuario>('getAllUsuarios')) // Manejo de errores
    );
  }

  getAllUsuarios(): Observable<Usuario[]> {
    const url = `${this.apiUrl}/all`;
    // Llama al endpoint GET /all que devuelve List<UsuarioDto>

    return this.httpClient.get<Usuario[]>(url).pipe(
      catchError(this.handleError<Usuario[]>('getAllUsuarios', [])) // Manejo de errores
    );
  }

  // Guarda usuario logueado
  setUsuario(usuario: Usuario): void {
    this.usuarioSubject.next(usuario);
  }

  // Borra usuario logueado (logout)
  clearUsuario(): void {
    this.usuarioSubject.next(null);
  }

  // Getter para usuario actual (ej: desde navbar)
  getUsuario(): Usuario | null {
    return this.usuarioSubject.value;
  }

  //para actulizar el usuario

  async actualizarUsuario(usuario: Usuario): Promise<Usuario | undefined> {
    return lastValueFrom(
      this.httpClient.put<Usuario>(`${this.apiUrl}/actualizar`, usuario).pipe(
        catchError((error) => {
          console.error('Error al actualizar el usuario:', error);
          return of(undefined);
        })
      )
    );
  }
  
  


  // async deleteUsuario(usuario: Usuario): Promise<boolean> {
  //     const url = `${this.apiUrl}/disable`;
  //   return lastValueFrom(
  //     this.httpClient
  //     .put<Usuario>(url, usuario)
  //       .pipe(
  //         catchError((error) => {
  //           console.error('Error al eliminar el usuario:', error);
  //           return of(false); // error
  //         })
  //       )
  //   );
  // }

  async getUsuarioByLogin(usuario: Usuario): Promise<Usuario | undefined> {
    const params = {
      email: usuario.email,
      password: usuario.password,
    };
    return lastValueFrom(
      this.httpClient.get<Usuario>(`${this.apiUrl}/login`, { params }).pipe(
        catchError((error) => {
          console.error('Error al obtener el usuario:', error);
          return of(undefined);
        })
      )
    );
  }

  async registrarUsuario(usuario: Usuario): Promise<Usuario | undefined> {
    
    return lastValueFrom(
      this.httpClient.post<Usuario>(`${this.apiUrl}/registrar`, usuario).pipe(
        catchError((error) => {
          console.error('Error al registrar el usuario:', error);
          return of(undefined);
        })
      )
    );
  }

  async registrarUsuarioEmpresa(
    usuario: Usuario,
    empresa: Empresa
  ): Promise<Usuario | undefined> {
    const body = { usuario:usuario, empresa:empresa };
    return lastValueFrom(
      this.httpClient
        .post<Usuario>(`${this.apiUrl}/registrar/empresa`, body)
        .pipe(
          catchError((error) => {
            console.error('Error al registrar el usuario:', error);
            return of(undefined);
          })
        )
    );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} falló: ${error.message}`, error);
      return of(result as T);
    };
  }
  



  async deleteUsuario(usuario: Usuario): Promise<boolean> {
    //modificamos a PUT, por necesidad de cliente

    const url = `${this.apiUrl}/disable`;

    return lastValueFrom(
      this.httpClient
        .put<Usuario>(url, usuario)
        .pipe(
          map(() => true), // Si la petición PUT tiene éxito (2xx), considerarlo true
          catchError((error) => {
            console.error('Error al deshabilitar (soft delete) el usuario:', error);
            return of(false); 
          })
        )
    );
  }

}

