import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISashimi } from 'app/shared/model/sashimi.model';
import { getEntities } from './sashimi.reducer';

export const Sashimi = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const sashimiList = useAppSelector(state => state.sashimi.entities);
  const loading = useAppSelector(state => state.sashimi.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="sashimi-heading" data-cy="SashimiHeading">
        Sashimis
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/sashimi/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Sashimi
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {sashimiList && sashimiList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Descricao</th>
                <th>Imagem</th>
                <th>Preco</th>
                <th>Promocao</th>
                <th>Cardapio</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {sashimiList.map((sashimi, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/sashimi/${sashimi.id}`} color="link" size="sm">
                      {sashimi.id}
                    </Button>
                  </td>
                  <td>{sashimi.nome}</td>
                  <td>{sashimi.descricao}</td>
                  <td>
                    {sashimi.imagem ? (
                      <div>
                        {sashimi.imagemContentType ? (
                          <a onClick={openFile(sashimi.imagemContentType, sashimi.imagem)}>
                            <img src={`data:${sashimi.imagemContentType};base64,${sashimi.imagem}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {sashimi.imagemContentType}, {byteSize(sashimi.imagem)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{sashimi.preco}</td>
                  <td>{sashimi.promocao ? 'true' : 'false'}</td>
                  <td>{sashimi.cardapio ? <Link to={`/cardapio/${sashimi.cardapio.id}`}>{sashimi.cardapio.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/sashimi/${sashimi.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/sashimi/${sashimi.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/sashimi/${sashimi.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Sashimis found</div>
        )}
      </div>
    </div>
  );
};

export default Sashimi;
