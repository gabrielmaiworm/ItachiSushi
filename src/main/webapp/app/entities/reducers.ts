import cardapio from 'app/entities/cardapio/cardapio.reducer';
import especiais from 'app/entities/especiais/especiais.reducer';
import entrada from 'app/entities/entrada/entrada.reducer';
import sushi from 'app/entities/sushi/sushi.reducer';
import sashimi from 'app/entities/sashimi/sashimi.reducer';
import makimono from 'app/entities/makimono/makimono.reducer';
import hot from 'app/entities/hot/hot.reducer';
import harumaki from 'app/entities/harumaki/harumaki.reducer';
import temaki from 'app/entities/temaki/temaki.reducer';
import yakisoba from 'app/entities/yakisoba/yakisoba.reducer';
import uramaki from 'app/entities/uramaki/uramaki.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  cardapio,
  especiais,
  entrada,
  sushi,
  sashimi,
  makimono,
  hot,
  harumaki,
  temaki,
  yakisoba,
  uramaki,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
