package una.progra4proyecto1.presentation.publico;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import una.progra4proyecto1.logic.Nacionalidad;
import una.progra4proyecto1.logic.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import una.progra4proyecto1.logic.Usuario;

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
    @GetMapping("/buscarPuestos")
    public String mostrarPuestos(){
        return "presentation/publico/BuscarPuestos";
    }
    @GetMapping("/login")
    public String mostrarLogin(){
        return "presentation/publico/Login";
    }
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo, @RequestParam String clave, HttpSession session) {
        Usuario usuario = service.login(correo, clave);
        if (usuario == null) {
            return "redirect:/login?error";
        }
        session.setAttribute("usuario", usuario);
        String usuarioLoggear;
        usuarioLoggear = service.getTipoUsuario(usuario.getId());
        if (usuarioLoggear.equals("admin")) return "redirect:/admin/AdminDashboard";
        if (usuarioLoggear.equals("oferente")) return "redirect:/oferente/MenuOferente";
        if (usuarioLoggear.equals("empresa")) return "redirect:/empresa/EmpresaDashboard";
        return "redirect:/";
    }
    @PostMapping("/registroEmpresa")
    public String procesarRegistroEmpresa(
            @RequestParam String nombre,
            @RequestParam String correo,
            @RequestParam String localizacion,
            @RequestParam String telefono,
            @RequestParam String descripcion,
            @RequestParam String clave,
            @RequestParam String claveConfirmar){
        if (!clave.equals(claveConfirmar)) {
            return "redirect:/registroEmpresa?error";
        }
        service.registrarEmpresa(nombre, correo, localizacion, telefono, descripcion, clave);
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
            @RequestParam String lugarResidencia,
            @RequestParam String clave,
            @RequestParam String claveConfirmar){
        if (!clave.equals(claveConfirmar)) {
            return "redirect:/registroOferente?error";
        }
            Nacionalidad nacionalidad = service.nacionalidadFindById(nacionalidadIso);
            service.registrarOferente(correo,identificacion,nombre,apellido,nacionalidad,telefono,lugarResidencia, clave);
     return "redirect:/";
    }
}
