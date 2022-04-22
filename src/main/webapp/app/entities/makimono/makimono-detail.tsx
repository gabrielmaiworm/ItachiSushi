import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './makimono.reducer';

export const MakimonoDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const makimonoEntity = useAppSelector(state => state.makimono.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="makimonoDetailsHeading">Makimono</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{makimonoEntity.id}</dd>
          <dt>
            <span id="nome">Nome</span>
          </dt>
          <dd>{makimonoEntity.nome}</dd>
          <dt>
            <span id="descricao">Descricao</span>
          </dt>
          <dd>{makimonoEntity.descricao}</dd>
          <dt>
            <span id="imagem">Imagem</span>
          </dt>
          <dd>
            {makimonoEntity.imagem ? (
              <div>
                {makimonoEntity.imagemContentType ? (
                  <a onClick={openFile(makimonoEntity.imagemContentType, makimonoEntity.imagem)}>
                    <img src={`data:${makimonoEntity.imagemContentType};base64,${makimonoEntity.imagem}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {makimonoEntity.imagemContentType}, {byteSize(makimonoEntity.imagem)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="preco">Preco</span>
          </dt>
          <dd>{makimonoEntity.preco}</dd>
          <dt>
            <span id="promocao">Promocao</span>
          </dt>
          <dd>{makimonoEntity.promocao ? 'true' : 'false'}</dd>
          <dt>
            <span id="ativo">Ativo</span>
          </dt>
          <dd>{makimonoEntity.ativo ? 'true' : 'false'}</dd>
          <dt>Cardapio</dt>
          <dd>{makimonoEntity.cardapio ? makimonoEntity.cardapio.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/makimono" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/makimono/${makimonoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default MakimonoDetail;
