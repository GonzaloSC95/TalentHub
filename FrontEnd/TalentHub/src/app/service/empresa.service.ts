import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, catchError, lastValueFrom, Observable, of } from 'rxjs';
import { environment } from '../../environments/environment';
import { Empresa } from '../interfaces/empresa';

@Injectable({
  providedIn: 'root',
})
export class EmpresaService {

  httpClient = inject(HttpClient)
  private apiUrl: string = environment.baseUrl + 'talenthub/api/empresa';
  private empresaSubject = new BehaviorSubject<Empresa | null>(null);
  empresa$ = this.empresaSubject.asObservable(); // observable para componentes

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

        async registrarEmpresa(empresa: Empresa): Promise<Empresa | undefined> {
          const body = { empresa };
          return lastValueFrom(
            this.httpClient.post<Empresa>(`${this.apiUrl}/registrar`, body).pipe(
              catchError((error) => {
                console.error('Error al registrar la empresa:', error);
                return of(undefined);
              })
            )
          );
        }

        
  // Guarda usuario logueado
  setUsuario(empresa: Empresa): void {
    this.empresaSubject.next(empresa);
  }
        
  // Borra usuario logueado (logout)
  clearUsuario(): void {
    this.empresaSubject.next(null);
  }
  getEmpresaById(id: number):Observable<Empresa>{
    const url = `${this.apiUrl}/${id}`;
                
    return this.httpClient.get<Empresa>(url).pipe(
      catchError(this.handleError<Empresa>('getAllEmpresas')) // Manejo de errores
    );
  }       
    // Getter para usuario actual (ej: desde navbar)
  getEmpresa(): Empresa | null {
    return this.empresaSubject.value;
    }
        
  async deleteEmpresa(empresa: Empresa): Promise<boolean> {
    const body = empresa;
    return lastValueFrom(
      this.httpClient
        .delete<boolean>(`${this.apiUrl}/eliminar`, {
          body, // Enviamos el usuario completo en el cuerpo
        })
        .pipe(
          catchError((error) => {
            console.error('Error al eliminar la empresa:', error);
            return of(false); // error
          })
        )
    );
  }
}
