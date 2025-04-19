import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, Observable, of } from 'rxjs';
import { environment } from '../../environments/environment';
import { Vacante } from '../interfaces/vacante';

@Injectable({
  providedIn: 'root',
})
export class VacanteService {
  httpClient = inject(HttpClient)
  private apiUrl: string = environment.baseUrl + 'talenthub/api/vacante';

  constructor() {}


    getAllVacantes():Observable<Vacante[]>{
      const url = `${this.apiUrl}/all`;
          // Llama al endpoint GET /all que devuelve List<UsuarioDto>
      
      return this.httpClient.get<Vacante[]>(url).pipe(
        catchError(this.handleError<Vacante[]>('getAllVacantes', [])) // Manejo de errores
      );
    }
      private handleError<T> (operation= 'operation', result?:T){
        return (error: any): Observable<T> =>{
          console.error(`${operation} falló: ${error.message}`, error)
          return of (result as T);
        }
      }
}
