import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEntrada } from 'app/shared/model/entrada.model';
import { getEntities } from './entrada.reducer';

export const Entrada = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const entradaList = useAppSelector(state => state.entrada.entities);
  const loading = useAppSelector(state => state.entrada.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="entrada-heading" data-cy="EntradaHeading">
        Entradas
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/entrada/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Entrada
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {entradaList && entradaList.length > 0 ? (
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
              {entradaList.map((entrada, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/entrada/${entrada.id}`} color="link" size="sm">
                      {entrada.id}
                    </Button>
                  </td>
                  <td>{entrada.nome}</td>
                  <td>{entrada.descricao}</td>
                  <td>
                    {entrada.imagem ? (
                      <div>
                        {entrada.imagemContentType ? (
                          <a onClick={openFile(entrada.imagemContentType, entrada.imagem)}>
                            <img src={`data:${entrada.imagemContentType};base64,${entrada.imagem}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {entrada.imagemContentType}, {byteSize(entrada.imagem)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{entrada.preco}</td>
                  <td>{entrada.promocao ? 'true' : 'false'}</td>
                  <td>{entrada.ativo ? 'true' : 'false'}</td>
                  <td>{entrada.cardapio ? <Link to={`/cardapio/${entrada.cardapio.id}`}>{entrada.cardapio.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/entrada/${entrada.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/entrada/${entrada.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/entrada/${entrada.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Entradas found</div>
        )}
      </div>
    </div>
  );
};

export default Entrada;
