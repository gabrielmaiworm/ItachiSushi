import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHot } from 'app/shared/model/hot.model';
import { getEntities } from './hot.reducer';

export const Hot = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const hotList = useAppSelector(state => state.hot.entities);
  const loading = useAppSelector(state => state.hot.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="hot-heading" data-cy="HotHeading">
        Hots
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/hot/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Hot
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {hotList && hotList.length > 0 ? (
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
              {hotList.map((hot, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/hot/${hot.id}`} color="link" size="sm">
                      {hot.id}
                    </Button>
                  </td>
                  <td>{hot.nome}</td>
                  <td>{hot.descricao}</td>
                  <td>
                    {hot.imagem ? (
                      <div>
                        {hot.imagemContentType ? (
                          <a onClick={openFile(hot.imagemContentType, hot.imagem)}>
                            <img src={`data:${hot.imagemContentType};base64,${hot.imagem}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {hot.imagemContentType}, {byteSize(hot.imagem)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{hot.preco}</td>
                  <td>{hot.promocao ? 'true' : 'false'}</td>
                  <td>{hot.cardapio ? <Link to={`/cardapio/${hot.cardapio.id}`}>{hot.cardapio.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/hot/${hot.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/hot/${hot.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/hot/${hot.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Hots found</div>
        )}
      </div>
    </div>
  );
};

export default Hot;
