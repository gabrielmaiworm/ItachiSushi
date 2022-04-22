import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Cardapio from './cardapio';
import Especiais from './especiais';
import Entrada from './entrada';
import Sushi from './sushi';
import Sashimi from './sashimi';
import Makimono from './makimono';
import Hot from './hot';
import Harumaki from './harumaki';
import Temaki from './temaki';
import Yakisoba from './yakisoba';
import Uramaki from './uramaki';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}cardapio`} component={Cardapio} />
        <ErrorBoundaryRoute path={`${match.url}especiais`} component={Especiais} />
        <ErrorBoundaryRoute path={`${match.url}entrada`} component={Entrada} />
        <ErrorBoundaryRoute path={`${match.url}sushi`} component={Sushi} />
        <ErrorBoundaryRoute path={`${match.url}sashimi`} component={Sashimi} />
        <ErrorBoundaryRoute path={`${match.url}makimono`} component={Makimono} />
        <ErrorBoundaryRoute path={`${match.url}hot`} component={Hot} />
        <ErrorBoundaryRoute path={`${match.url}harumaki`} component={Harumaki} />
        <ErrorBoundaryRoute path={`${match.url}temaki`} component={Temaki} />
        <ErrorBoundaryRoute path={`${match.url}yakisoba`} component={Yakisoba} />
        <ErrorBoundaryRoute path={`${match.url}uramaki`} component={Uramaki} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
