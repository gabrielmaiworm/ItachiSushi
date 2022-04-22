import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICardapio } from 'app/shared/model/cardapio.model';
import { getEntities as getCardapios } from 'app/entities/cardapio/cardapio.reducer';
import { ITemaki } from 'app/shared/model/temaki.model';
import { getEntity, updateEntity, createEntity, reset } from './temaki.reducer';

export const TemakiUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const cardapios = useAppSelector(state => state.cardapio.entities);
  const temakiEntity = useAppSelector(state => state.temaki.entity);
  const loading = useAppSelector(state => state.temaki.loading);
  const updating = useAppSelector(state => state.temaki.updating);
  const updateSuccess = useAppSelector(state => state.temaki.updateSuccess);
  const handleClose = () => {
    props.history.push('/temaki');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCardapios({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...temakiEntity,
      ...values,
      cardapio: cardapios.find(it => it.id.toString() === values.cardapio.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...temakiEntity,
          cardapio: temakiEntity?.cardapio?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="itachiSushiApp.temaki.home.createOrEditLabel" data-cy="TemakiCreateUpdateHeading">
            Create or edit a Temaki
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="temaki-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Nome" id="temaki-nome" name="nome" data-cy="nome" type="text" />
              <ValidatedField label="Descricao" id="temaki-descricao" name="descricao" data-cy="descricao" type="text" />
              <ValidatedBlobField label="Imagem" id="temaki-imagem" name="imagem" data-cy="imagem" isImage accept="image/*" />
              <ValidatedField label="Preco" id="temaki-preco" name="preco" data-cy="preco" type="text" />
              <ValidatedField label="Promocao" id="temaki-promocao" name="promocao" data-cy="promocao" check type="checkbox" />
              <ValidatedField label="Ativo" id="temaki-ativo" name="ativo" data-cy="ativo" check type="checkbox" />
              <ValidatedField id="temaki-cardapio" name="cardapio" data-cy="cardapio" label="Cardapio" type="select">
                <option value="" key="0" />
                {cardapios
                  ? cardapios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/temaki" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TemakiUpdate;
