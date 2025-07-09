// src/app/models/models.ts

export interface Tecnico {
  userid: string;
  username: string;
  // adicione outros campos do técnico se necessário
}

export interface ChamadosItem {
  remetente: string;
  assunto: string;
  descricao: string;
  dataHora: string;
  tokenEmail: string;
  status: string;
  tecnico?: Tecnico | null
}

export interface ChamadoResponse {
  ChamadoItems: ChamadosItem[];
}
