import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Harumaki from './harumaki';
import HarumakiDetail from './harumaki-detail';
import HarumakiUpdate from './harumaki-update';
import HarumakiDeleteDialog from './harumaki-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HarumakiUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HarumakiUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HarumakiDetail} />
      <ErrorBoundaryRoute path={match.url} component={Harumaki} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HarumakiDeleteDialog} />
  </>
);

export default Routes;
