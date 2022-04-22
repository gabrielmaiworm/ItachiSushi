import { IEspeciais } from 'app/shared/model/especiais.model';
import { IEntrada } from 'app/shared/model/entrada.model';
import { ISushi } from 'app/shared/model/sushi.model';
import { ISashimi } from 'app/shared/model/sashimi.model';
import { IMakimono } from 'app/shared/model/makimono.model';
import { IHot } from 'app/shared/model/hot.model';
import { IHarumaki } from 'app/shared/model/harumaki.model';
import { ITemaki } from 'app/shared/model/temaki.model';
import { IYakisoba } from 'app/shared/model/yakisoba.model';
import { IUramaki } from 'app/shared/model/uramaki.model';

export interface ICardapio {
  id?: number;
  nome?: string | null;
  especiais?: IEspeciais[] | null;
  entradas?: IEntrada[] | null;
  sushis?: ISushi[] | null;
  sashimis?: ISashimi[] | null;
  makimonos?: IMakimono[] | null;
  hots?: IHot[] | null;
  harumakis?: IHarumaki[] | null;
  temakis?: ITemaki[] | null;
  yakisobas?: IYakisoba[] | null;
  uramakis?: IUramaki[] | null;
}

export const defaultValue: Readonly<ICardapio> = {};
