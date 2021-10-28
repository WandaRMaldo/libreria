/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.egg.libreriaboot.service;

import edu.egg.libreriaboot.entity.Editorial;
import edu.egg.libreriaboot.excepcion.MiExcepcion;
import edu.egg.libreriaboot.repository.EditorialRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialService {
    
    @Autowired
    private EditorialRepository editorialRepository;
    
    @Transactional
    public void crear(String nombre) throws MiExcepcion{
        
        if (nombre == null || nombre.isEmpty()) {
            throw new MiExcepcion("El nombre de la editorial no puede ser nulo");
        }
        
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        
        editorialRepository.save(editorial);
    }
    
    @Transactional
    public void modificar(String id, String nombre){
        editorialRepository.modificar(id, nombre);
    }
    
    @Transactional(readOnly = true)
    public List<Editorial> buscarTodas(){
        return editorialRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Editorial buscarPorId(String id) {
        Optional<Editorial> editorialOptional = editorialRepository.findById(id);
        return editorialOptional.orElse(null);
    }
    
    @Transactional
    public void darBaja(String id) throws MiExcepcion{
        Optional<Editorial> editorialOptional = editorialRepository.findById(id); /*veamos si funciona*/
        if (editorialOptional.isPresent()) {
            Editorial editorial = editorialOptional.get();
            editorial.setAlta(false);
            editorialRepository.save(editorial);
        }else{
            throw new MiExcepcion("No se encontro la editorial solicitado");
        }
    }
}
