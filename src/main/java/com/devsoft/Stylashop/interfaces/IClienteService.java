package com.devsoft.Stylashop.interfaces;

import com.devsoft.Stylashop.dto.ClienteDTO;
import java.util.List;

public interface IClienteService {
    List<ClienteDTO> findAll();
    ClienteDTO findById (Long id);
    ClienteDTO findByNombre (String nombre);
}
