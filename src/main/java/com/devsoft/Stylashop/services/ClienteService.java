package com.devsoft.Stylashop.services;

import com.devsoft.Stylashop.dto.ClienteDTO;
import com.devsoft.Stylashop.entities.Cliente;
import com.devsoft.Stylashop.interfaces.IClienteService;
import com.devsoft.Stylashop.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService implements IClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDTO findById(Long id) {
        Cliente cl = clienteRepository.findById(id).orElse(null);
        if (cl == null) return null;
        return convertToDTO(cl);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDTO findByNombre(String nombre) {
        Cliente cl = clienteRepository.findByNombre(nombre);
        if (cl == null) return null;
        return convertToDTO(cl);
    }

    private ClienteDTO convertToDTO(Cliente cl) {
        return new ClienteDTO(cl.getId(),
                cl.getNombre(),
                cl.getEmail(),
                cl.getTelefono(),
                cl.getTipoCliente());
    }
}
