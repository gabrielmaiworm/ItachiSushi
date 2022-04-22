import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHarumaki } from 'app/shared/model/harumaki.model';
import { getEntities } from './harumaki.reducer';

export const Harumaki = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const harumakiList = useAppSelector(state => state.harumaki.entities);
  const loading = useAppSelector(state => state.harumaki.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="harumaki-heading" data-cy="HarumakiHeading">
        Harumakis
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/harumaki/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Harumaki
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {harumakiList && harumakiList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Descricao</th>
                <th>Imagem</th>
                <th>Preco</th>
                <th>Promocao</th>
                <th>Ativo</th>
                <th>Cardapio</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {harumakiList.map((harumaki, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/harumaki/${harumaki.id}`} color="link" size="sm">
                      {harumaki.id}
                    </Button>
                  </td>
                  <td>{harumaki.nome}</td>
                  <td>{harumaki.descricao}</td>
                  <td>
                    {harumaki.imagem ? (
                      <div>
                        {harumaki.imagemContentType ? (
                          <a onClick={openFile(harumaki.imagemContentType, harumaki.imagem)}>
                            <img src={`data:${harumaki.imagemContentType};base64,${harumaki.imagem}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {harumaki.imagemContentType}, {byteSize(harumaki.imagem)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{harumaki.preco}</td>
                  <td>{harumaki.promocao ? 'true' : 'false'}</td>
                  <td>{harumaki.ativo ? 'true' : 'false'}</td>
                  <td>{harumaki.cardapio ? <Link to={`/cardapio/${harumaki.cardapio.id}`}>{harumaki.cardapio.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/harumaki/${harumaki.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/harumaki/${harumaki.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/harumaki/${harumaki.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Harumakis found</div>
        )}
      </div>
    </div>
  );
};

export default Harumaki;
