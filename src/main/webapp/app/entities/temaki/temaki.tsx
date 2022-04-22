import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITemaki } from 'app/shared/model/temaki.model';
import { getEntities } from './temaki.reducer';

export const Temaki = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const temakiList = useAppSelector(state => state.temaki.entities);
  const loading = useAppSelector(state => state.temaki.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="temaki-heading" data-cy="TemakiHeading">
        Temakis
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/temaki/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Temaki
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {temakiList && temakiList.length > 0 ? (
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
              {temakiList.map((temaki, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/temaki/${temaki.id}`} color="link" size="sm">
                      {temaki.id}
                    </Button>
                  </td>
                  <td>{temaki.nome}</td>
                  <td>{temaki.descricao}</td>
                  <td>
                    {temaki.imagem ? (
                      <div>
                        {temaki.imagemContentType ? (
                          <a onClick={openFile(temaki.imagemContentType, temaki.imagem)}>
                            <img src={`data:${temaki.imagemContentType};base64,${temaki.imagem}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {temaki.imagemContentType}, {byteSize(temaki.imagem)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{temaki.preco}</td>
                  <td>{temaki.promocao ? 'true' : 'false'}</td>
                  <td>{temaki.cardapio ? <Link to={`/cardapio/${temaki.cardapio.id}`}>{temaki.cardapio.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/temaki/${temaki.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/temaki/${temaki.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/temaki/${temaki.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Temakis found</div>
        )}
      </div>
    </div>
  );
};

export default Temaki;
