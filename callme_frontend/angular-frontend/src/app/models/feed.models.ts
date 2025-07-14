export interface FeedItem{

  chamadoId: string;
  content: string;
  username: string;
  descricao: string;


}

export interface FeedResponse{

  feedItens: FeedItem[];
  page: number;
  pageSize: number;
  totalPages: number;
  totalElements: number;

}
