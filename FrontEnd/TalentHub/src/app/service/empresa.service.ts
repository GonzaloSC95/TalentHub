import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, Observable, of } from 'rxjs';
import { environment } from '../../environments/environment';
import { Empresa } from '../interfaces/empresa';

@Injectable({
  providedIn: 'root',
})
export class EmpresaService {

  httpClient = inject(HttpClient)
  private apiUrl: string = environment.baseUrl + 'talenthub/api/empresa';

  constructor() {}


    getAllEmpresas():Observable<Empresa[]>{
      const url = `${this.apiUrl}/all`;
          // Llama al endpoint GET /all que devuelve List<UsuarioDto>
      
      return this.httpClient.get<Empresa[]>(url).pipe(
        catchError(this.handleError<Empresa[]>('getAllEmpresas', [])) // Manejo de errores
      );
    }
      private handleError<T> (operation= 'operation', result?:T){
        return (error: any): Observable<T> =>{
          console.error(`${operation} falló: ${error.message}`, error)
          return of (result as T);
        }
      }
}
