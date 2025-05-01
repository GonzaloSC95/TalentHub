import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, lastValueFrom, Observable, of } from 'rxjs';
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
      getVacantesCreadas():Observable<Vacante[]>{
        const url = `${this.apiUrl}/creadas`;
            // Llama al endpoint GET /creadas que devuelve una lista de vacantes con estatus CREADA
        
        return this.httpClient.get<Vacante[]>(url).pipe(
          catchError(this.handleError<Vacante[]>('getAllVacantes', [])) // Manejo de errores
        );
      }
      
      getVacanteById(id: number):Observable<Vacante>{
        const url = `${this.apiUrl}/${id}`;
               
        return this.httpClient.get<Vacante>(url).pipe(
          catchError(this.handleError<Vacante>('getAllVacantes')) // Manejo de errores
        );
      }

      actualizarVacante(vacante: any): Observable<any> {
        return this.httpClient.put(`${this.apiUrl}/actualizar`, vacante);
      }
  async deleteVacante(vacante: Vacante): Promise<boolean> {
    const body = vacante;
    return lastValueFrom(
      this.httpClient
        .delete<boolean>(`${this.apiUrl}/eliminar`, {
          body, // Enviamos la vacante completa en el cuerpo
        })
        .pipe(
          catchError((error) => {
            console.error('Error al eliminar la vacante:', error);
            return of(false); // error
          })
        )
    );
  }
}



