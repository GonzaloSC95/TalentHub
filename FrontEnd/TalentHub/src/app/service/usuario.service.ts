import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Usuario } from '../interfaces/usuario';
import { catchError, lastValueFrom, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {
  //Inyección de dependencias
  httpClient = inject(HttpClient);
  // URL de la API
  private apiUrl: string = environment.baseUrl + 'talenthub/api/usuario';
  constructor() {}

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
}
