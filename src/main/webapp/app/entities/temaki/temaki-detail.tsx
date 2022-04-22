import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './temaki.reducer';

export const TemakiDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const temakiEntity = useAppSelector(state => state.temaki.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="temakiDetailsHeading">Temaki</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{temakiEntity.id}</dd>
          <dt>
            <span id="nome">Nome</span>
          </dt>
          <dd>{temakiEntity.nome}</dd>
          <dt>
            <span id="descricao">Descricao</span>
          </dt>
          <dd>{temakiEntity.descricao}</dd>
          <dt>
            <span id="imagem">Imagem</span>
          </dt>
          <dd>
            {temakiEntity.imagem ? (
              <div>
                {temakiEntity.imagemContentType ? (
                  <a onClick={openFile(temakiEntity.imagemContentType, temakiEntity.imagem)}>
                    <img src={`data:${temakiEntity.imagemContentType};base64,${temakiEntity.imagem}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {temakiEntity.imagemContentType}, {byteSize(temakiEntity.imagem)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="preco">Preco</span>
          </dt>
          <dd>{temakiEntity.preco}</dd>
          <dt>
            <span id="promocao">Promocao</span>
          </dt>
          <dd>{temakiEntity.promocao ? 'true' : 'false'}</dd>
          <dt>Cardapio</dt>
          <dd>{temakiEntity.cardapio ? temakiEntity.cardapio.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/temaki" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/temaki/${temakiEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TemakiDetail;
