export interface Usuario {
  email: string;
  password: string;
  nombre?: string;
  apellidos?: string;
  enabled?: number;
  fechaRegistro?: Date;
  rol?: string;
  empresaId?: number;
  nombreEmpresa?: string;
}
