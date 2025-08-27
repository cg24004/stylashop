package com.devsoft.Stylashop.interfaces;

import com.devsoft.Stylashop.dto.RoleDTO;
import java.util.List;

public interface IRoleService {
    List<RoleDTO> findAll();
    RoleDTO findById(Long id);
    RoleDTO findByNombre(String nombre);
    RoleDTO save(RoleDTO dto);
    void delete(Long id);
}

