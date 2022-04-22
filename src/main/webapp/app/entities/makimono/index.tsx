import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Makimono from './makimono';
import MakimonoDetail from './makimono-detail';
import MakimonoUpdate from './makimono-update';
import MakimonoDeleteDialog from './makimono-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MakimonoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MakimonoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MakimonoDetail} />
      <ErrorBoundaryRoute path={match.url} component={Makimono} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MakimonoDeleteDialog} />
  </>
);

export default Routes;
