export interface ComentarioDTO {
  texto: string;
  dataCriacao: string;
  autorUsername: string;
}

// 2. Adicione a lista de comentários à sua interface principal
export interface Acompanhamento {
  status: string;
  dataAbertura: string;
  comentarios?: ComentarioDTO[]; // Propriedade opcional para a lista de comentários
}
