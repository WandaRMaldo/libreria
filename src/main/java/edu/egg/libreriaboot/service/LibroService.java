package edu.egg.libreriaboot.service;

import edu.egg.libreriaboot.entity.Libro;
import edu.egg.libreriaboot.excepcion.MiExcepcion;
import edu.egg.libreriaboot.repository.AutorRepository;
import edu.egg.libreriaboot.repository.EditorialRepository;
import edu.egg.libreriaboot.repository.LibroRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroService {
    
    @Autowired
    private LibroRepository libroRepository;
    
    @Autowired
    private AutorRepository autorRepository;
    
    @Autowired
    private EditorialRepository editorialRepository;
    
    @Transactional
    public void crear(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, String idAutor, String idEditorial){
        Libro libro = new Libro();
        
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(restantes(ejemplares, ejemplaresPrestados));
        libro.setAutor(autorRepository.findById(idAutor).orElse(null));
        libro.setEditorial(editorialRepository.findById(idEditorial).orElse(null));
    
        libroRepository.save(libro);
    }
    
    @Transactional
    public void modificar(String id, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados){
        Integer ejemplaresRestantes = restantes(ejemplares, ejemplaresPrestados);
        libroRepository.modificar(id, isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes);
    }
    
    @Transactional(readOnly = true)
    public List<Libro> buscarTodas(){
        return libroRepository.findAll();
    }
    
    @Transactional
    public void darBaja(String id) throws MiExcepcion{
        Optional<Libro> libroOptional = libroRepository.findById(id) ; /*veamos si funciona*/
        if (libroOptional.isPresent()) {
            Libro libro = libroOptional.get();
            libro.setAlta(false);
            libroRepository.save(libro);
        }else{
            throw new MiExcepcion("No se encontro el libro solicitado");
        }
    }
    
    @Transactional(readOnly = true)
    public Libro buscarPorId(String id) {
        Optional<Libro> libroOptional = libroRepository.findById(id);
        return libroOptional.orElse(null);
    }
    
    public Integer restantes(Integer ejemplares, Integer ejemplaresPrestados){
        Integer ejemplaresRestantes;
        ejemplaresRestantes = ejemplares - ejemplaresPrestados;
        return ejemplaresRestantes;
    }
}
