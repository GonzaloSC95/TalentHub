export interface Solicitud {
  idSolicitud: number;
  fecha: Date;
  archivo: string;
  comentarios: string;
  estado: string;
  curriculum: string;
  vacante: string; // seria el idVacante
  candidato: string; // es el mail del usuario
}
