import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Temaki from './temaki';
import TemakiDetail from './temaki-detail';
import TemakiUpdate from './temaki-update';
import TemakiDeleteDialog from './temaki-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TemakiUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TemakiUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TemakiDetail} />
      <ErrorBoundaryRoute path={match.url} component={Temaki} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TemakiDeleteDialog} />
  </>
);

export default Routes;
