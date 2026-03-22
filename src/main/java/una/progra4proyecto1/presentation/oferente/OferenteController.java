package una.progra4proyecto1.presentation.oferente;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import una.progra4proyecto1.logic.Caracteristica;
import una.progra4proyecto1.logic.Oferente;
import una.progra4proyecto1.logic.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import una.progra4proyecto1.logic.Usuario;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Controller("oferentes")
public class OferenteController {
    @Autowired
    private Service service;
    @Autowired
    private HttpSession session;

    @GetMapping("/oferente/MenuOferente")
    public String show(Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Oferente oferente = service.oferenteFindById(usuario.getId());
        model.addAttribute("oferente", oferente);
        return "presentation/oferente/MenuOferente";
    }

    @GetMapping("/oferente/MisHabilidades")
    public String showMisHabilidades(Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Oferente oferente = service.oferenteFindById(usuario.getId());
        List<Caracteristica> caracteristicas = service.caracteristicasRaiz();
        model.addAttribute("oferente", oferente);
        model.addAttribute("habilidades", service.habilidadesOferente(oferente));
        model.addAttribute("caracteristicas", service.caracteristicasRaiz());
        model.addAttribute("tieneHijos", service.mapTieneHijos(caracteristicas));
        model.addAttribute("nodosFinales", service.caracteristicasNodosFinales());
        return "presentation/oferente/MisHabilidades";
    }

    @GetMapping("/oferente/MisHabilidades/{id}")
    public String showHijosCaracteristica(@PathVariable Integer id, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Oferente oferente = service.oferenteFindById(usuario.getId());
        Caracteristica padre = service.caracteristicaFindById(id);
        List<Caracteristica> caracteristicas = service.caracteristicasHijos(padre);
        model.addAttribute("oferente", oferente);
        model.addAttribute("habilidades", service.habilidadesOferente(oferente));
        model.addAttribute("caracteristicas", caracteristicas);
        model.addAttribute("padre", padre);
        model.addAttribute("tieneHijos", service.mapTieneHijos(caracteristicas));
        model.addAttribute("nodosFinales", service.caracteristicasNodosFinales());
        return "presentation/oferente/MisHabilidades";
    }
    @PostMapping("/oferente/agregarHabilidad")
    public String agregarHabilidad(@RequestParam Integer caracteristicaId, @RequestParam  Integer nivel){
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Oferente oferente = service.oferenteFindById(usuario.getId());
        service.agregarHabilidad(oferente,service.caracteristicaFindById(caracteristicaId),nivel);
        return "redirect:/oferente/MisHabilidades";
    }
    @GetMapping("/oferente/MiCV")
    public String showMiCV(Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Oferente oferente = service.oferenteFindById(usuario.getId());
        model.addAttribute("oferente", oferente);
        return "presentation/oferente/MiCV";
    }
    @GetMapping("/oferente/revisarCV")
    public ResponseEntity<byte[]> revisarCV() throws IOException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Oferente oferente = service.oferenteFindById(usuario.getId());
        Path ruta = Paths.get(oferente.getCv());
        byte[] contenido = Files.readAllBytes(ruta);
        return ResponseEntity.ok().header("Content-Type","application/pdf").header("Content-Disposition","inline; filename=cv.pdf").body(contenido);
    }
    @PostMapping("/oferente/subirCV")
    public String subirCV(@RequestParam MultipartFile cv) throws IOException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Oferente oferente = service.oferenteFindById(usuario.getId());
        oferente.setCv("uploads/cv/"+oferente.getId()+".pdf");
        service.guardarOferente(oferente);
        Path ruta = Paths.get("uploads/cv/"+oferente.getId()+".pdf");
        Files.createDirectories(ruta.getParent());
        cv.transferTo(ruta);
        return "redirect:/oferente/MiCV";
    }
    @GetMapping("/salir")
    public String Salir() {
        session.invalidate();
        return "redirect:/";
    }
}
