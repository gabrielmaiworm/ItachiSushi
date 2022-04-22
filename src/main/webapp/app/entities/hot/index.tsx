import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Hot from './hot';
import HotDetail from './hot-detail';
import HotUpdate from './hot-update';
import HotDeleteDialog from './hot-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HotUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HotUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HotDetail} />
      <ErrorBoundaryRoute path={match.url} component={Hot} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HotDeleteDialog} />
  </>
);

export default Routes;
