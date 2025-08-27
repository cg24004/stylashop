package com.devsoft.Stylashop.services;

import com.devsoft.Stylashop.dto.MarcaDTO;
import com.devsoft.Stylashop.entities.Marca;
import com.devsoft.Stylashop.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarcaService {
    @Autowired
    private MarcaRepository marcaRepository;

    @Transactional(readOnly = true)
    public List<MarcaDTO> findAll() {
        return marcaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MarcaDTO findById(Long id) {
        Marca marca = marcaRepository.findById(id).orElse(null);
        if (marca == null) return null;
        return convertToDTO(marca);
    }

    @Transactional(readOnly = true)
    public MarcaDTO findByNombre(String nombre) {
        Marca marca = marcaRepository.findByNombre(nombre);
        if (marca == null) return null;
        return convertToDTO(marca);
    }

    @Transactional
    public MarcaDTO save(MarcaDTO dto) {
        Marca marca = new Marca();
        if (dto.getId() != null) marca.setId(dto.getId());
        marca.setNombre(dto.getNombre());
        return convertToDTO(marcaRepository.save(marca));
    }

    @Transactional
    public void delete(Long id) {
        marcaRepository.deleteById(id);
    }

    private MarcaDTO convertToDTO(Marca marca) {
        return new MarcaDTO(marca.getId(), marca.getNombre());
    }
}

