import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, catchError, lastValueFrom, Observable, of } from 'rxjs';
import { environment } from '../../environments/environment';
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

  getAllUsuarios():Observable<Usuario[]>{
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

async deleteUsuario(usuario: Usuario): Promise<boolean> { 
  const body = { usuario }; 
  return lastValueFrom(
    this.httpClient.delete<boolean>(`${this.apiUrl}/eliminar`, {
      body,  // Enviamos el usuario completo en el cuerpo
    }).pipe(
      catchError(error => {
        console.error('Error al eliminar el usuario:', error);
        return of(false);  // error
      })
    )
  );
}
    
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
  private handleError<T> (operation= 'operation', result?:T){
    return (error: any): Observable<T> =>{
      console.error(`${operation} falló: ${error.message}`, error)
      return of (result as T);
    }
  }

  //     // Obtener usuario con mock
  // async getUsuarioByLogin(usuario: Usuario): Promise<Usuario | null> {
  //   // Simula la validación del login con el mock de usuarios
  //   const usuarioEncontrado = USUARIOS_DATA.find(
  //     (u) => u.email === usuario.email && u.password === usuario.password
  //   );

  //   // Si no se encuentra el usuario o las credenciales no coinciden, devuelve null
  //   if (!usuarioEncontrado) {
  //     return null;
  //   }

  //   // Devolvemos el usuario encontrado
  //   return usuarioEncontrado;
  // }


}
