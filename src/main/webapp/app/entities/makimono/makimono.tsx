import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMakimono } from 'app/shared/model/makimono.model';
import { getEntities } from './makimono.reducer';

export const Makimono = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const makimonoList = useAppSelector(state => state.makimono.entities);
  const loading = useAppSelector(state => state.makimono.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="makimono-heading" data-cy="MakimonoHeading">
        Makimonos
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/makimono/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Makimono
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {makimonoList && makimonoList.length > 0 ? (
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
              {makimonoList.map((makimono, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/makimono/${makimono.id}`} color="link" size="sm">
                      {makimono.id}
                    </Button>
                  </td>
                  <td>{makimono.nome}</td>
                  <td>{makimono.descricao}</td>
                  <td>
                    {makimono.imagem ? (
                      <div>
                        {makimono.imagemContentType ? (
                          <a onClick={openFile(makimono.imagemContentType, makimono.imagem)}>
                            <img src={`data:${makimono.imagemContentType};base64,${makimono.imagem}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {makimono.imagemContentType}, {byteSize(makimono.imagem)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{makimono.preco}</td>
                  <td>{makimono.promocao ? 'true' : 'false'}</td>
                  <td>{makimono.ativo ? 'true' : 'false'}</td>
                  <td>{makimono.cardapio ? <Link to={`/cardapio/${makimono.cardapio.id}`}>{makimono.cardapio.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/makimono/${makimono.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/makimono/${makimono.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/makimono/${makimono.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Makimonos found</div>
        )}
      </div>
    </div>
  );
};

export default Makimono;
