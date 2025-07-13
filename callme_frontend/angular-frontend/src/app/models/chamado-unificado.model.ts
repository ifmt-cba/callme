export interface ChamadoUnificado {
  id: number;
  tipo: 'INTERNO' | 'EXTERNO';
  assunto: string;
  descricao: string;
  solicitante: string;
  data: string;
}
