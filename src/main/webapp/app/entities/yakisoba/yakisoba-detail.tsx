import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './yakisoba.reducer';

export const YakisobaDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const yakisobaEntity = useAppSelector(state => state.yakisoba.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="yakisobaDetailsHeading">Yakisoba</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{yakisobaEntity.id}</dd>
          <dt>
            <span id="nome">Nome</span>
          </dt>
          <dd>{yakisobaEntity.nome}</dd>
          <dt>
            <span id="descricao">Descricao</span>
          </dt>
          <dd>{yakisobaEntity.descricao}</dd>
          <dt>
            <span id="imagem">Imagem</span>
          </dt>
          <dd>
            {yakisobaEntity.imagem ? (
              <div>
                {yakisobaEntity.imagemContentType ? (
                  <a onClick={openFile(yakisobaEntity.imagemContentType, yakisobaEntity.imagem)}>
                    <img src={`data:${yakisobaEntity.imagemContentType};base64,${yakisobaEntity.imagem}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {yakisobaEntity.imagemContentType}, {byteSize(yakisobaEntity.imagem)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="preco">Preco</span>
          </dt>
          <dd>{yakisobaEntity.preco}</dd>
          <dt>
            <span id="promocao">Promocao</span>
          </dt>
          <dd>{yakisobaEntity.promocao ? 'true' : 'false'}</dd>
          <dt>
            <span id="ativo">Ativo</span>
          </dt>
          <dd>{yakisobaEntity.ativo ? 'true' : 'false'}</dd>
          <dt>Cardapio</dt>
          <dd>{yakisobaEntity.cardapio ? yakisobaEntity.cardapio.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/yakisoba" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/yakisoba/${yakisobaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default YakisobaDetail;
