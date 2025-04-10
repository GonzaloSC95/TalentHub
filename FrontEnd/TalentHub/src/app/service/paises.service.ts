import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, lastValueFrom, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PaisesService {
  //Inyección de dependencias
  httpClient = inject(HttpClient);
  // URL de la API de países (https://gitlab.com/restcountries)
  private apiUrl = 'https://restcountries.com/v3.1/all'; //'/api/v3.1/all';
  // Lista de países
  nombres: any[] = [];

  constructor() {
    this.getPaises().then((paises) => {
      paises.forEach((pais) => {
        this.nombres.push(pais.translations.spa.common);
      });
    });
  }

  private async getPaises(): Promise<any[]> {
    return lastValueFrom(
      this.httpClient.get<any[]>(this.apiUrl).pipe(
        catchError((error) => {
          console.error('Error al obtener los países:', error);
          return of([]);
        })
      )
    );
  }
}
