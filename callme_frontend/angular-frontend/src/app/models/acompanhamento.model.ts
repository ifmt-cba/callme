export interface Comentario {
  id: number;
  texto: string;
  dataCriacao: string;
  autor: {
    username: string;
  };
}

// Interface principal para o acompanhamento
export interface Acompanhamento {
  status: string;
  dataAbertura: string;
  comentarios?: Comentario[];
}
