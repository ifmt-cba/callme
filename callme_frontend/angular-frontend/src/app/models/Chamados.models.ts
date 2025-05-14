export interface ChamadosItem {

  remetente: string;
  destinatario: string | null;
  assunto: string;
  corpoSimples: string;
  dataHora: string;
  spf: string | null;
  dkim: string | null;
  dmarc: string | null;
  comprovante: string;

}

export interface ChamadoResponse {
  ChamadoItems: ChamadosItem[];
}
