export interface ChamadoUnificado {
  id: string;
  tipo: 'INTERNO' | 'EXTERNO';
  assunto: string;
  descricao: string;
  solicitante: string;
  data: string;
  status: string;
}
