package com.devsoft.Stylashop.interfaces;

import com.devsoft.Stylashop.dto.UsuarioDTO;
import java.util.List;

public interface IUsuarioService {
    List<UsuarioDTO> findAll();
    UsuarioDTO findById (Long id);
}
