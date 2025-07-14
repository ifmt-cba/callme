import { User } from './usuarios.models'; // Importa o modelo de usuário para o técnico

// Define a estrutura de um Chamado Externo
export interface ChamadoExterno {
  id: number;
  assunto: string;
  descricao: string;
  remetente: string;
  dataHora: string;
  status: 'ABERTO' | 'EM_ANDAMENTO' | 'FECHADO' | 'NOVO'; // Usa os mesmos valores do seu Enum no Java
  tokenEmail: string;
  tecnico?: User | null;
  dataFinalizacao?: string;
}
