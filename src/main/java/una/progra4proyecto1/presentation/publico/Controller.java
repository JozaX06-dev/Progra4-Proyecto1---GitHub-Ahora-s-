package una.progra4proyecto1.presentation.publico;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import una.progra4proyecto1.logic.Nacionalidad;
import una.progra4proyecto1.logic.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("usuarios")
public class Controller {
    @Autowired
    private Service service;

    @GetMapping("/")
    public String show(Model model) {
        model.addAttribute("usuarios", service.usuarioFindAll());
        model.addAttribute("puestos", service.find5PuestosPublicosActivos());
        return "presentation/publico/MenuPartePublica";
    }

    @GetMapping("/registroEmpresa")
    public String mostrarRegistroEmpresa(){
        return "presentation/publico/RegistroEmpresa";
    }
    @GetMapping("/registroOferente")
    public String mostrarRegistroOferente(Model model){
        model.addAttribute("nacionalidades", service.nacionalidadFindAll());
        return "presentation/publico/RegistroOferente";
    }
    @PostMapping("/registroEmpresa")
    public String procesarRegistroEmpresa(
            @RequestParam String nombre,
            @RequestParam String correo,
            @RequestParam String localizacion,
            @RequestParam String telefono,
            @RequestParam String descripcion){
        service.registrarEmpresa(nombre, correo, localizacion, telefono, descripcion);
        return "redirect:/";
    }
    @PostMapping("/registroOferente")
    public String procesarRegistroOferente(
            @RequestParam String correo,
            @RequestParam String identificacion,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String nacionalidadIso,
            @RequestParam String telefono,
            @RequestParam String lugarResidencia){
            Nacionalidad nacionalidad = service.nacionalidadFindById(nacionalidadIso);
            service.registrarOferente(correo,identificacion,nombre,apellido,nacionalidad,telefono,lugarResidencia);
     return "redirect:/";
    }

}
