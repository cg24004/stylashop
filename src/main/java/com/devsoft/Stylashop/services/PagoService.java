package com.devsoft.Stylashop.services;

import com.devsoft.Stylashop.entities.Pago;
import com.devsoft.Stylashop.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagoService {
    @Autowired
    private PagoRepository pagoRepository;

    public Pago registrarPago(Pago pago){
        return pagoRepository.save(pago);
    }
    public List<Pago> getAll(){
        return pagoRepository.findAll();
    }
    public Pago findById(Long id){
        return pagoRepository.findById(id).orElse(null);
    }
}
