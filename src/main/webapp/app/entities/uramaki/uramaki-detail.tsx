import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './uramaki.reducer';

export const UramakiDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const uramakiEntity = useAppSelector(state => state.uramaki.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="uramakiDetailsHeading">Uramaki</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{uramakiEntity.id}</dd>
          <dt>
            <span id="nome">Nome</span>
          </dt>
          <dd>{uramakiEntity.nome}</dd>
          <dt>
            <span id="descricao">Descricao</span>
          </dt>
          <dd>{uramakiEntity.descricao}</dd>
          <dt>
            <span id="imagem">Imagem</span>
          </dt>
          <dd>
            {uramakiEntity.imagem ? (
              <div>
                {uramakiEntity.imagemContentType ? (
                  <a onClick={openFile(uramakiEntity.imagemContentType, uramakiEntity.imagem)}>
                    <img src={`data:${uramakiEntity.imagemContentType};base64,${uramakiEntity.imagem}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {uramakiEntity.imagemContentType}, {byteSize(uramakiEntity.imagem)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="preco">Preco</span>
          </dt>
          <dd>{uramakiEntity.preco}</dd>
          <dt>
            <span id="promocao">Promocao</span>
          </dt>
          <dd>{uramakiEntity.promocao ? 'true' : 'false'}</dd>
          <dt>
            <span id="ativo">Ativo</span>
          </dt>
          <dd>{uramakiEntity.ativo ? 'true' : 'false'}</dd>
          <dt>Cardapio</dt>
          <dd>{uramakiEntity.cardapio ? uramakiEntity.cardapio.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/uramaki" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/uramaki/${uramakiEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UramakiDetail;
