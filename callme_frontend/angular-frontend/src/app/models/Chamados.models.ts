export interface ChamadosItem {

  remetente: string;
  assunto: string;
  descricao: string;
  dataHora: string;
  tokenEmail: string;
  status: string;
}

export interface ChamadoResponse {
  ChamadoItems: ChamadosItem[];
}
