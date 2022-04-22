import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Sashimi from './sashimi';
import SashimiDetail from './sashimi-detail';
import SashimiUpdate from './sashimi-update';
import SashimiDeleteDialog from './sashimi-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SashimiUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SashimiUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SashimiDetail} />
      <ErrorBoundaryRoute path={match.url} component={Sashimi} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SashimiDeleteDialog} />
  </>
);

export default Routes;
