import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Entrada from './entrada';
import EntradaDetail from './entrada-detail';
import EntradaUpdate from './entrada-update';
import EntradaDeleteDialog from './entrada-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EntradaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EntradaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EntradaDetail} />
      <ErrorBoundaryRoute path={match.url} component={Entrada} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={EntradaDeleteDialog} />
  </>
);

export default Routes;
