import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './harumaki.reducer';

export const HarumakiDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const harumakiEntity = useAppSelector(state => state.harumaki.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="harumakiDetailsHeading">Harumaki</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{harumakiEntity.id}</dd>
          <dt>
            <span id="nome">Nome</span>
          </dt>
          <dd>{harumakiEntity.nome}</dd>
          <dt>
            <span id="descricao">Descricao</span>
          </dt>
          <dd>{harumakiEntity.descricao}</dd>
          <dt>
            <span id="imagem">Imagem</span>
          </dt>
          <dd>
            {harumakiEntity.imagem ? (
              <div>
                {harumakiEntity.imagemContentType ? (
                  <a onClick={openFile(harumakiEntity.imagemContentType, harumakiEntity.imagem)}>
                    <img src={`data:${harumakiEntity.imagemContentType};base64,${harumakiEntity.imagem}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {harumakiEntity.imagemContentType}, {byteSize(harumakiEntity.imagem)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="preco">Preco</span>
          </dt>
          <dd>{harumakiEntity.preco}</dd>
          <dt>
            <span id="promocao">Promocao</span>
          </dt>
          <dd>{harumakiEntity.promocao ? 'true' : 'false'}</dd>
          <dt>Cardapio</dt>
          <dd>{harumakiEntity.cardapio ? harumakiEntity.cardapio.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/harumaki" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/harumaki/${harumakiEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default HarumakiDetail;
