import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, of } from 'rxjs';
import { Usuario } from '../interfaces/usuario';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Solicitud } from '../interfaces/solicitud';

@Injectable({
  providedIn: 'root',
})
export class SolicitudService {

  // BehaviorSubject para mantener usuario actual igual que en el service de usuario
  private usuarioSubject = new BehaviorSubject<Usuario | null>(null);
  usuario$ = this.usuarioSubject.asObservable(); // observable para componentes
  //Inyección de dependencias
  httpClient = inject(HttpClient);
  // URL de la API
  private apiUrl: string = environment.baseUrl + 'talenthub/api/solicitud';

  constructor() {}
  getSolicitudesPresentadas(): Observable<Solicitud[]> {
    return this.httpClient.get<Solicitud[]>(`${this.apiUrl}/presentadas`);
  }

  private handleError<T> (operation= 'operation', result?:T){
          return (error: any): Observable<T> =>{
            console.error(`${operation} falló: ${error.message}`, error)
            return of (result as T);
          }
        }

  getSolicitudesAdjudicadas(): Observable<Solicitud[]> {
    return this.httpClient.get<Solicitud[]>(`${this.apiUrl}/adjudicadas`);
  }

  getSolicitudesAll(): Observable<Solicitud[]> {
    return this.httpClient.get<Solicitud[]>(`${this.apiUrl}/all`);
    }

    getSolicitudById(id: number): Observable<Solicitud> {
      return this.httpClient.get<Solicitud>(`${this.apiUrl}/${id}`).pipe(
        map((solicitud) => {
          // Convertir fecha string a Date
          solicitud.fecha = solicitud.fecha ? new Date(solicitud.fecha) : new Date();
          return solicitud;
        })
      );
    }
  
    actualizarSolicitud(solicitud: Solicitud): Observable<Solicitud> {
      return this.httpClient.put<Solicitud>(`${this.apiUrl}/actualizar`, solicitud);
    }
  }
  



