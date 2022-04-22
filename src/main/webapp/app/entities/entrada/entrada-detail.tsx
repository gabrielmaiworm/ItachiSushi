import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './entrada.reducer';

export const EntradaDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const entradaEntity = useAppSelector(state => state.entrada.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="entradaDetailsHeading">Entrada</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{entradaEntity.id}</dd>
          <dt>
            <span id="nome">Nome</span>
          </dt>
          <dd>{entradaEntity.nome}</dd>
          <dt>
            <span id="descricao">Descricao</span>
          </dt>
          <dd>{entradaEntity.descricao}</dd>
          <dt>
            <span id="imagem">Imagem</span>
          </dt>
          <dd>
            {entradaEntity.imagem ? (
              <div>
                {entradaEntity.imagemContentType ? (
                  <a onClick={openFile(entradaEntity.imagemContentType, entradaEntity.imagem)}>
                    <img src={`data:${entradaEntity.imagemContentType};base64,${entradaEntity.imagem}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {entradaEntity.imagemContentType}, {byteSize(entradaEntity.imagem)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="preco">Preco</span>
          </dt>
          <dd>{entradaEntity.preco}</dd>
          <dt>
            <span id="promocao">Promocao</span>
          </dt>
          <dd>{entradaEntity.promocao ? 'true' : 'false'}</dd>
          <dt>
            <span id="ativo">Ativo</span>
          </dt>
          <dd>{entradaEntity.ativo ? 'true' : 'false'}</dd>
          <dt>Cardapio</dt>
          <dd>{entradaEntity.cardapio ? entradaEntity.cardapio.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/entrada" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/entrada/${entradaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default EntradaDetail;
