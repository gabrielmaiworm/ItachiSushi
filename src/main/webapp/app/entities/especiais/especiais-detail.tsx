import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './especiais.reducer';

export const EspeciaisDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const especiaisEntity = useAppSelector(state => state.especiais.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="especiaisDetailsHeading">Especiais</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{especiaisEntity.id}</dd>
          <dt>
            <span id="nome">Nome</span>
          </dt>
          <dd>{especiaisEntity.nome}</dd>
          <dt>
            <span id="descricao">Descricao</span>
          </dt>
          <dd>{especiaisEntity.descricao}</dd>
          <dt>
            <span id="imagem">Imagem</span>
          </dt>
          <dd>
            {especiaisEntity.imagem ? (
              <div>
                {especiaisEntity.imagemContentType ? (
                  <a onClick={openFile(especiaisEntity.imagemContentType, especiaisEntity.imagem)}>
                    <img src={`data:${especiaisEntity.imagemContentType};base64,${especiaisEntity.imagem}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {especiaisEntity.imagemContentType}, {byteSize(especiaisEntity.imagem)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="preco">Preco</span>
          </dt>
          <dd>{especiaisEntity.preco}</dd>
          <dt>
            <span id="promocao">Promocao</span>
          </dt>
          <dd>{especiaisEntity.promocao ? 'true' : 'false'}</dd>
          <dt>
            <span id="ativo">Ativo</span>
          </dt>
          <dd>{especiaisEntity.ativo ? 'true' : 'false'}</dd>
          <dt>Cardapio</dt>
          <dd>{especiaisEntity.cardapio ? especiaisEntity.cardapio.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/especiais" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/especiais/${especiaisEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default EspeciaisDetail;
