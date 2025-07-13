
export interface Tecnico {
  userid: string;
  username: string;
}

export interface Comentario {
  id: number;
  texto: string;
  dataCriacao: string;
  autor: { // Apenas os dados que queremos exibir
    username: string;
  };
}


export interface ChamadosItem {
  id: number;
  remetente: string;
  assunto: string;
  descricao: string;
  dataHora: string;
  tokenEmail: string;
  status: string;
  tecnico?: Tecnico | null
  comentarios?: Comentario[];

}

export interface ChamadoResponse {
  ChamadoItems: ChamadosItem[];
}
