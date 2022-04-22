import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Especiais from './especiais';
import EspeciaisDetail from './especiais-detail';
import EspeciaisUpdate from './especiais-update';
import EspeciaisDeleteDialog from './especiais-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EspeciaisUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EspeciaisUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EspeciaisDetail} />
      <ErrorBoundaryRoute path={match.url} component={Especiais} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={EspeciaisDeleteDialog} />
  </>
);

export default Routes;
