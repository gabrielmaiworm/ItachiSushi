import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sushi.reducer';

export const SushiDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const sushiEntity = useAppSelector(state => state.sushi.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sushiDetailsHeading">Sushi</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{sushiEntity.id}</dd>
          <dt>
            <span id="nome">Nome</span>
          </dt>
          <dd>{sushiEntity.nome}</dd>
          <dt>
            <span id="descricao">Descricao</span>
          </dt>
          <dd>{sushiEntity.descricao}</dd>
          <dt>
            <span id="imagem">Imagem</span>
          </dt>
          <dd>
            {sushiEntity.imagem ? (
              <div>
                {sushiEntity.imagemContentType ? (
                  <a onClick={openFile(sushiEntity.imagemContentType, sushiEntity.imagem)}>
                    <img src={`data:${sushiEntity.imagemContentType};base64,${sushiEntity.imagem}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {sushiEntity.imagemContentType}, {byteSize(sushiEntity.imagem)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="preco">Preco</span>
          </dt>
          <dd>{sushiEntity.preco}</dd>
          <dt>
            <span id="promocao">Promocao</span>
          </dt>
          <dd>{sushiEntity.promocao ? 'true' : 'false'}</dd>
          <dt>
            <span id="ativo">Ativo</span>
          </dt>
          <dd>{sushiEntity.ativo ? 'true' : 'false'}</dd>
          <dt>Cardapio</dt>
          <dd>{sushiEntity.cardapio ? sushiEntity.cardapio.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/sushi" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sushi/${sushiEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SushiDetail;
