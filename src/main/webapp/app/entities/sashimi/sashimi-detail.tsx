import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sashimi.reducer';

export const SashimiDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const sashimiEntity = useAppSelector(state => state.sashimi.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sashimiDetailsHeading">Sashimi</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{sashimiEntity.id}</dd>
          <dt>
            <span id="nome">Nome</span>
          </dt>
          <dd>{sashimiEntity.nome}</dd>
          <dt>
            <span id="descricao">Descricao</span>
          </dt>
          <dd>{sashimiEntity.descricao}</dd>
          <dt>
            <span id="imagem">Imagem</span>
          </dt>
          <dd>
            {sashimiEntity.imagem ? (
              <div>
                {sashimiEntity.imagemContentType ? (
                  <a onClick={openFile(sashimiEntity.imagemContentType, sashimiEntity.imagem)}>
                    <img src={`data:${sashimiEntity.imagemContentType};base64,${sashimiEntity.imagem}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {sashimiEntity.imagemContentType}, {byteSize(sashimiEntity.imagem)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="preco">Preco</span>
          </dt>
          <dd>{sashimiEntity.preco}</dd>
          <dt>
            <span id="promocao">Promocao</span>
          </dt>
          <dd>{sashimiEntity.promocao ? 'true' : 'false'}</dd>
          <dt>Cardapio</dt>
          <dd>{sashimiEntity.cardapio ? sashimiEntity.cardapio.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/sashimi" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sashimi/${sashimiEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SashimiDetail;
