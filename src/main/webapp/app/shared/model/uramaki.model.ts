import { ICardapio } from 'app/shared/model/cardapio.model';

export interface IUramaki {
  id?: number;
  nome?: string | null;
  descricao?: string | null;
  imagemContentType?: string | null;
  imagem?: string | null;
  preco?: number | null;
  promocao?: boolean | null;
  cardapio?: ICardapio | null;
}

export const defaultValue: Readonly<IUramaki> = {
  promocao: false,
};