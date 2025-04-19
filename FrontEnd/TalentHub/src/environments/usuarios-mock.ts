import { Usuario } from "../app/interfaces/usuario";

export const USUARIOS_DATA: Usuario[] = [
    {email: 'elprimer@hotmail.com', password: 'alcalacrece', nombre: 'string', apellidos: 'string',enabled: 0, rol: 'EMPRESA',empresa: 'tocoto',},
    {email: 'elsegundo@hotmail.com', password: 'alcalacrece', nombre: 'string', apellidos: 'string',enabled: 0, rol: 'CLIENTE',empresa: '',},
    {email: 'elprimer@hotmail.com', password: 'alcalacrece', nombre: 'string', apellidos: 'string',enabled: 0, rol: 'EMPRESA',empresa: 'bichos',},
    {email: 'elprimer@hotmail.com', password: 'alcalacrece', nombre: 'string', apellidos: 'string',enabled: 0, rol: 'CLIENTE',empresa: '',},
    {email: 'elprimer@hotmail.com', password: 'alcalacrece', nombre: 'string', apellidos: 'string',enabled: 0, rol: 'CLIENTE',empresa: '',},
    {
        email: 'empresa@dominio.com',
        password: 'empresa123',
        nombre: 'Empresa SA',
        apellidos: '',
        enabled: 1,
        fechaRegistro: new Date('2023-01-02'),
        rol: 'EMPRESA',
        empresa: '',
      },
      {
        email: 'admin@admin.com',
        password: 'admin123',
        nombre: 'Administrador',
        apellidos: 'Admin',
        enabled: 1,
        fechaRegistro: new Date('2023-01-03'),
        rol: 'ADMON',
        empresa: '',
      },
]  