package una.progra4proyecto1.presentation.oferente;
import una.progra4proyecto1.logic.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("oferentes")
public class Controller {
    @Autowired
    private Service service;

    public String show (Model model){
        model.addAttribute("oferentes", service.oferenteFindAll());
        return "presentation/oferente/MenuOferente";
    }
}
