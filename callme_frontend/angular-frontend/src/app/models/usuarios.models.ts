export interface User {
  id: string;
  username: string;
  email: string;
  roles: string[];
  password?: string;  // Campo opcional para atualizações
}


export interface UsuariosResponse{

  UsuariosItems : User[];

}
