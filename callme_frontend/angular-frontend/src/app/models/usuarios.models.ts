export interface User {
  id: string;
  username: string;
  email: string;
  roles: string[];
}


export interface UsuariosResponse{

  UsuariosItems : User[];


}
