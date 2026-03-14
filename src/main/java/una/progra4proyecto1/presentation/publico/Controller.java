package una.progra4proyecto1.presentation.publico;
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
        model.addAttribute("puestos", service.puestoFindAll());
        return "presentation/publico/MenuPartePublica";
    }
}
