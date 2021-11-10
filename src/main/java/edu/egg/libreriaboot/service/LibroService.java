package edu.egg.libreriaboot.service;

import edu.egg.libreriaboot.entity.Autor;
import edu.egg.libreriaboot.entity.Editorial;
import edu.egg.libreriaboot.entity.Libro;
import edu.egg.libreriaboot.excepcion.MiExcepcion;
import edu.egg.libreriaboot.repository.AutorRepository;
import edu.egg.libreriaboot.repository.EditorialRepository;
import edu.egg.libreriaboot.repository.LibroRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public void crear(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, String idAutor, String idEditorial) throws MiExcepcion{
        validarIsbn(isbn);
        validarTitulo(titulo);
        validarAnio(anio);
        validarEjemplares(ejemplares);
        validarEjemplaresPrestados(ejemplaresPrestados, ejemplares);
        
        Libro libro = new Libro();
        
        libro.setIsbn(isbn);
        libro.setTitulo(titulo.toUpperCase());
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(restantes(ejemplares, ejemplaresPrestados));
        libro.setAutor(autorRepository.findById(idAutor).orElse(null));
        libro.setEditorial(editorialRepository.findById(idEditorial).orElse(null));
    
        libroRepository.save(libro);
    }
    
    @Transactional
    public void modificar(String id, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Autor autor, Editorial editorial) throws MiExcepcion{
        //validarIsbn(isbn);
        //validarTitulo(titulo);
        validarAnio(anio);
        validarEjemplares(ejemplares);
        validarEjemplaresPrestados(ejemplaresPrestados, ejemplares);
        
        Integer ejemplaresRestantes = restantes(ejemplares, ejemplaresPrestados);
        libroRepository.modificar(id, isbn, titulo.toUpperCase(), anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, autor, editorial);
    }
    
    @Transactional(readOnly = true)
    public List<Libro> buscarTodas(){
        return libroRepository.findAll();
    }
    
    @Transactional
    public void darBaja(String id) throws MiExcepcion{
        Optional<Libro> libroOptional = libroRepository.findById(id) ;
        if (libroOptional.isPresent()) {
            Libro libro = libroOptional.get();
            libro.setAlta(!libro.getAlta());
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
    
    public void validarTitulo(String titulo) throws MiExcepcion {

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new MiExcepcion("EL NOMBRE DEL LIBRO ES OBLIGATORIO");
        }

        if (libroRepository.buscarPorTitulo(titulo) != null) {
            throw new MiExcepcion("YA EXISTE UN LIBRO CON ESE NOMBRE");
        }
    }

    public void validarEjemplares(Integer ejemplares) throws MiExcepcion {
        if (ejemplares < 0 || ejemplares == null) {
            throw new MiExcepcion("LA CANTIDAD DE EJEMPLARES NO PUEDE SER MENOR A 0");
        }
    }

    public void validarEjemplaresPrestados(Integer ejemplaresPrestados, Integer ejemplares) throws MiExcepcion {

        if (ejemplaresPrestados < 0 || ejemplaresPrestados == null) {
            throw new MiExcepcion("LA CANTIDAD DE EJEMPLARES PRESTADOS NO PUEDE SER MENOR A 0");
        }
        if (ejemplaresPrestados > ejemplares) {
            throw new MiExcepcion("LA CANTIDAD DE LIBROS PRESTADOS NO PUEDE SUPERAR LA CANTIDAD DE EJEMPLARES");
        }
    }

    public void validarAnio(Integer anio) throws MiExcepcion {

        Date date = new Date();

        SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");
        String anioString = getYearFormat.format(date);
        Integer anioActual = Integer.parseInt(anioString);

        if (anio > anioActual) {
            throw new MiExcepcion("EL AÑO NO PUEDE SER MAYOR AL AÑO ACTUAL");
        }
        
        if (!anio.toString().matches("\\d{4}")) {
                throw new MiExcepcion("DEBE INGRESAR 4 DÍGITOS");
            }
    }

    public void validarIsbn(Long isbn) throws MiExcepcion {

        if (isbn.toString().trim().isEmpty()) {
                throw new MiExcepcion("CAMPO OBLIGATORIO");
            }

            if (!isbn.toString().matches("^[0-9]{13}$")) {
                throw new MiExcepcion("DEBE INGRESAR 13 DÍGITOS");
            }

        if (libroRepository.buscarLibroPorIsbn(isbn) != null) {
            throw new MiExcepcion("YA EXISTE UN LIBRO CON ESE ISBN.");
        }

    }
}
