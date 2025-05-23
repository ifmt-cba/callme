export interface UsuariosModels{

  id : string;
  nome: string;
  email: string;
  token : string;
  roles : string;


}

export interface UsuariosResponse{

  UsuariosItems : UsuariosModels[];


}
