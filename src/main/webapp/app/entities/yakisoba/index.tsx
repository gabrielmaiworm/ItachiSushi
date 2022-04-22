import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Yakisoba from './yakisoba';
import YakisobaDetail from './yakisoba-detail';
import YakisobaUpdate from './yakisoba-update';
import YakisobaDeleteDialog from './yakisoba-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={YakisobaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={YakisobaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={YakisobaDetail} />
      <ErrorBoundaryRoute path={match.url} component={Yakisoba} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={YakisobaDeleteDialog} />
  </>
);

export default Routes;
