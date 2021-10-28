package edu.egg.libreriaboot.controller;

import edu.egg.libreriaboot.entity.Editorial;
import edu.egg.libreriaboot.excepcion.MiExcepcion;
import edu.egg.libreriaboot.service.EditorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/editoriales")
public class EditorialController {
    
    @Autowired
    private EditorialService editorialService; 
    
    @GetMapping
    public ModelAndView mostrarTodos() {
        ModelAndView mav = new ModelAndView("editoriales");
        mav.addObject("editoriales", editorialService.buscarTodas());
        return mav;
    }

    @GetMapping("/crear")
    public ModelAndView crearUsuario() {
        ModelAndView mav = new ModelAndView("editorial-formulario");
        mav.addObject("editorial", new Editorial());
        mav.addObject("title", "Crear Editorial");
        mav.addObject("action", "guardar");
        return mav;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarUsuario(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("editorial-formulario");
        mav.addObject("editorial", editorialService.buscarPorId(id));
        mav.addObject("title", "Editar Editorial");
        mav.addObject("action", "modificar");
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String nombre) throws MiExcepcion {
        editorialService.crear(nombre);
        return new RedirectView("/editoriales");
    }

    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam String id, @RequestParam String nombre) {
        editorialService.modificar(id, nombre);
        return new RedirectView("/editoriales");
    }

    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable String id) throws MiExcepcion {
        editorialService.darBaja(id);
        return new RedirectView("/editoriales");
    }
}
