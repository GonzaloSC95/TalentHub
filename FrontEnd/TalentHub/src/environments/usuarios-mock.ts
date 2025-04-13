import { Usuario } from "../app/interfaces/usuario";

export const USUARIOS_DATA: Usuario[] = [
    {email: 'elprimer@hotmail.com', password: 'alcalacrece', nombre: 'string', apellidos: 'string',enabled: 0, rol: 'empresa',empresa: 'tocoto',},
    {email: 'elsegundo@hotmail.com', password: 'alcalacrece', nombre: 'string', apellidos: 'string',enabled: 0, rol: 'candidato',empresa: '',},
    {email: 'elprimer@hotmail.com', password: 'alcalacrece', nombre: 'string', apellidos: 'string',enabled: 0, rol: 'empresa',empresa: 'bichos',},
    {email: 'elprimer@hotmail.com', password: 'alcalacrece', nombre: 'string', apellidos: 'string',enabled: 0, rol: 'candidato',empresa: '',},
    {email: 'elprimer@hotmail.com', password: 'alcalacrece', nombre: 'string', apellidos: 'string',enabled: 0, rol: 'candidato',empresa: '',},
    {
        email: 'empresa@dominio.com',
        password: 'empresa123',
        nombre: 'Empresa SA',
        apellidos: '',
        enabled: 1,
        fechaRegistro: new Date('2023-01-02'),
        rol: 'empresa',
        empresa: 'Empresa SA',
      },
      {
        email: 'admin@admin.com',
        password: 'admin123',
        nombre: 'Administrador',
        apellidos: 'Admin',
        enabled: 1,
        fechaRegistro: new Date('2023-01-03'),
        rol: 'admin',
        empresa: '',
      },
]  