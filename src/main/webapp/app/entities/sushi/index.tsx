import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Sushi from './sushi';
import SushiDetail from './sushi-detail';
import SushiUpdate from './sushi-update';
import SushiDeleteDialog from './sushi-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SushiUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SushiUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SushiDetail} />
      <ErrorBoundaryRoute path={match.url} component={Sushi} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SushiDeleteDialog} />
  </>
);

export default Routes;
