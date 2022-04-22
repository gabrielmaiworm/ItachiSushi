import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/cardapio">
        Cardapio
      </MenuItem>
      <MenuItem icon="asterisk" to="/especiais">
        Especiais
      </MenuItem>
      <MenuItem icon="asterisk" to="/entrada">
        Entrada
      </MenuItem>
      <MenuItem icon="asterisk" to="/sushi">
        Sushi
      </MenuItem>
      <MenuItem icon="asterisk" to="/sashimi">
        Sashimi
      </MenuItem>
      <MenuItem icon="asterisk" to="/makimono">
        Makimono
      </MenuItem>
      <MenuItem icon="asterisk" to="/hot">
        Hot
      </MenuItem>
      <MenuItem icon="asterisk" to="/harumaki">
        Harumaki
      </MenuItem>
      <MenuItem icon="asterisk" to="/temaki">
        Temaki
      </MenuItem>
      <MenuItem icon="asterisk" to="/yakisoba">
        Yakisoba
      </MenuItem>
      <MenuItem icon="asterisk" to="/uramaki">
        Uramaki
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
