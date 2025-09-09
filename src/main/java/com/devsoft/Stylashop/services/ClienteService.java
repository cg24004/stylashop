package com.devsoft.Stylashop.services;

import com.devsoft.Stylashop.dto.ClienteDTO;
import com.devsoft.Stylashop.entities.Cliente;
import com.devsoft.Stylashop.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    private ClienteDTO toDTO(Cliente c){
        return ClienteDTO.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .email(c.getEmail())
                .telefono(c.getTelefono())
                .tipoCliente(c.getTipoCliente())
                .build();
    }

    private Cliente toEntity(ClienteDTO dto){
        Cliente c = new Cliente();
        c.setId(dto.getId());
        c.setNombre(dto.getNombre());
        c.setEmail(dto.getEmail());
        c.setTelefono(dto.getTelefono());
        c.setTipoCliente(dto.getTipoCliente());
        return c;
    }

    public List<ClienteDTO> listar(){
        return clienteRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ClienteDTO obtenerPorId(Long id){
        return clienteRepository.findById(id).map(this::toDTO).orElse(null);
    }

    public ClienteDTO crear(ClienteDTO dto){
        Cliente guardado = clienteRepository.save(toEntity(dto));
        return toDTO(guardado);
    }

    public ClienteDTO actualizar(Long id, ClienteDTO dto){
        return clienteRepository.findById(id).map(c -> {
            c.setNombre(dto.getNombre());
            c.setEmail(dto.getEmail());
            c.setTelefono(dto.getTelefono());
            c.setTipoCliente(dto.getTipoCliente());
            return toDTO(clienteRepository.save(c));
        }).orElse(null);
    }

    public void eliminar(Long id){
        clienteRepository.deleteById(id);
    }
}
