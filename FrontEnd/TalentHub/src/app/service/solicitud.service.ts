
import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, map, of } from 'rxjs';
import { Solicitud } from '../interfaces/solicitud';
import { SolicitudBackend } from '../interfaces/solicitud-backend';
import { environment } from '../../environments/environment';
import { Usuario } from '../interfaces/usuario';

@Injectable({
  providedIn: 'root'
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

  getSolicitudById(id: number): Observable<Solicitud> {
    return this.httpClient.get<SolicitudBackend>(`${this.apiUrl}/${id}`).pipe(
      map(dto => this.mapDtoToSolicitud(dto
        
      ))
    );
  }

  crearSolicitud(solicitud: Solicitud): Observable<any> {
    return this.httpClient.post(`${this.apiUrl}/crear`, this.mapSolicitudToDto(solicitud));
  }
  
  actualizarSolicitud(solicitud: Solicitud): Observable<any> {
    return this.httpClient.put(`${this.apiUrl}/actualizar`, this.mapSolicitudToDto(solicitud));
  }


//como no coincidian los interfaces he solicitudBackend y hecho el map


  private mapDtoToSolicitud(dto: SolicitudBackend): Solicitud {
    return {
      idSolicitud: dto.idSolicitud,
      fecha: new Date (dto.fecha),
      archivo: dto.archivo,
      comentarios: dto.comentarios,
      estado: dto.estado,
      curriculum: dto.curriculum,
      vacante: dto.idVacante.toString(), //esto cambiaba
      candidato: dto.emailUsuario //esto tb cambiaba con el original que tenemos
    };
  }

  private mapSolicitudToDto(solicitud: Solicitud): SolicitudBackend {
    return {
      idSolicitud: solicitud.idSolicitud,
      fecha: solicitud.fecha,
      archivo: solicitud.archivo,
      comentarios: solicitud.comentarios,
      estado: solicitud.estado,
      curriculum: solicitud.curriculum,
      idVacante: Number(solicitud.vacante),
      emailUsuario: solicitud.candidato
    };
  }

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
  }