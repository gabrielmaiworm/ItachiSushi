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
import { IYakisoba } from 'app/shared/model/yakisoba.model';
import { getEntity, updateEntity, createEntity, reset } from './yakisoba.reducer';

export const YakisobaUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const cardapios = useAppSelector(state => state.cardapio.entities);
  const yakisobaEntity = useAppSelector(state => state.yakisoba.entity);
  const loading = useAppSelector(state => state.yakisoba.loading);
  const updating = useAppSelector(state => state.yakisoba.updating);
  const updateSuccess = useAppSelector(state => state.yakisoba.updateSuccess);
  const handleClose = () => {
    props.history.push('/yakisoba');
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
      ...yakisobaEntity,
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
          ...yakisobaEntity,
          cardapio: yakisobaEntity?.cardapio?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="itachiSushiApp.yakisoba.home.createOrEditLabel" data-cy="YakisobaCreateUpdateHeading">
            Create or edit a Yakisoba
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="yakisoba-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Nome" id="yakisoba-nome" name="nome" data-cy="nome" type="text" />
              <ValidatedField label="Descricao" id="yakisoba-descricao" name="descricao" data-cy="descricao" type="text" />
              <ValidatedBlobField label="Imagem" id="yakisoba-imagem" name="imagem" data-cy="imagem" isImage accept="image/*" />
              <ValidatedField label="Preco" id="yakisoba-preco" name="preco" data-cy="preco" type="text" />
              <ValidatedField label="Promocao" id="yakisoba-promocao" name="promocao" data-cy="promocao" check type="checkbox" />
              <ValidatedField label="Ativo" id="yakisoba-ativo" name="ativo" data-cy="ativo" check type="checkbox" />
              <ValidatedField id="yakisoba-cardapio" name="cardapio" data-cy="cardapio" label="Cardapio" type="select">
                <option value="" key="0" />
                {cardapios
                  ? cardapios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/yakisoba" replace color="info">
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

export default YakisobaUpdate;
