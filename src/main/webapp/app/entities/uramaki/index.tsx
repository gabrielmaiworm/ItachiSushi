import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Uramaki from './uramaki';
import UramakiDetail from './uramaki-detail';
import UramakiUpdate from './uramaki-update';
import UramakiDeleteDialog from './uramaki-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UramakiUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UramakiUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UramakiDetail} />
      <ErrorBoundaryRoute path={match.url} component={Uramaki} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UramakiDeleteDialog} />
  </>
);

export default Routes;
