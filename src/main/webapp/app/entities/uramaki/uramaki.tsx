import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUramaki } from 'app/shared/model/uramaki.model';
import { getEntities } from './uramaki.reducer';

export const Uramaki = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const uramakiList = useAppSelector(state => state.uramaki.entities);
  const loading = useAppSelector(state => state.uramaki.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="uramaki-heading" data-cy="UramakiHeading">
        Uramakis
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/uramaki/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Uramaki
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {uramakiList && uramakiList.length > 0 ? (
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
              {uramakiList.map((uramaki, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/uramaki/${uramaki.id}`} color="link" size="sm">
                      {uramaki.id}
                    </Button>
                  </td>
                  <td>{uramaki.nome}</td>
                  <td>{uramaki.descricao}</td>
                  <td>
                    {uramaki.imagem ? (
                      <div>
                        {uramaki.imagemContentType ? (
                          <a onClick={openFile(uramaki.imagemContentType, uramaki.imagem)}>
                            <img src={`data:${uramaki.imagemContentType};base64,${uramaki.imagem}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {uramaki.imagemContentType}, {byteSize(uramaki.imagem)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{uramaki.preco}</td>
                  <td>{uramaki.promocao ? 'true' : 'false'}</td>
                  <td>{uramaki.ativo ? 'true' : 'false'}</td>
                  <td>{uramaki.cardapio ? <Link to={`/cardapio/${uramaki.cardapio.id}`}>{uramaki.cardapio.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/uramaki/${uramaki.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/uramaki/${uramaki.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/uramaki/${uramaki.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Uramakis found</div>
        )}
      </div>
    </div>
  );
};

export default Uramaki;
