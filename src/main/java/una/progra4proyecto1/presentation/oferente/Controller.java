package una.progra4proyecto1.presentation.oferente;
import jakarta.servlet.http.HttpSession;
import una.progra4proyecto1.logic.Oferente;
import una.progra4proyecto1.logic.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import una.progra4proyecto1.logic.Usuario;
import jakarta.servlet.http.HttpSession;

@org.springframework.stereotype.Controller("oferentes")
public class Controller {
    @Autowired
    private Service service;

    @GetMapping("/oferente/MenuOferente")
    public String show (Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Oferente oferente = service.oferenteFindById(usuario.getId());
        model.addAttribute("oferente", oferente);
        return "presentation/oferente/MenuOferente";
    }
}
