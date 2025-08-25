package com.devsoft.Stylashop.services;

import com.devsoft.Stylashop.dto.MarcaDTO;
import com.devsoft.Stylashop.entities.Marca;
import com.devsoft.Stylashop.interfaces.IMarcaService;
import com.devsoft.Stylashop.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public class MarcaService implements IMarcaService{
    @Autowired //Inyectar por dependencia el repositorio
    private MarcaRepository marcaRepository; //Esto es un repositorio que maneja las operaciones CRUD de la entidad Marca

    @Override
    @Transactional(readOnly = true)
    public List<MarcaDTO> findAll() { // Este método obtiene todas las marcas y las convierte a MarcaDTO
        return marcaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MarcaDTO findById(Long id) {
        Marca marca = marcaRepository.findById(id).orElse(null);
        if (marca == null) return null;
        return convertToDTO(marca);
    }

    @Override
    public MarcaRepository save(MarcaDTO dto) { // Este método guarda una marca y devuelve un MarcaDTO
        Marca marcaNueva = new Marca();
        if(dto.getId() == null) {
            //nuevo registro
            marcaNueva.setNombre(dto.getNombre());
        }else{
            //actualizar registro
            marcaNueva.setId(dto.getId());
            marcaNueva.setNombre(dto.getNombre());
        }
        return convertToDTO(marcaRepository.save(marcaNueva));
    }

    // Este método busca una categoría por su nombre y devuelve un CategoriaDTO
    @Override
    public MarcaDTO findByNombre(String nombre) {
        Marca marca = marcaRepository.findByNombre(nombre);
        if (marca == null) return null;
        return convertToDTO(marca);
    }

    @Override
    public void delete(Long id) { // Este método elimina una marca por su ID
        marcaRepository.deleteById(id);
    }

    // Método privado para convertir una entidad Categoria a CategoriaDTO
    private MarcaDTO convertToDTO(Marca marca) {
        return new MarcaDTO(marca.getId(),marca.getNombre());
    }
}
