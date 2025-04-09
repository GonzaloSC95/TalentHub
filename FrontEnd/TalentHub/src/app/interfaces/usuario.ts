export interface Usuario {
  email: string;
  nombre: string;
  apellidos: string;
  password: string;
  enabled: number;
  fechaRegistro: Date;
  rol: string;
  empresa?: string;
}
