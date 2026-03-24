package una.progra4proyecto1.presentation.admin;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import una.progra4proyecto1.logic.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Controller("admin")
public class AdminController {
    @Autowired
    private Service service;

    @GetMapping("/admin/AdminDashboard")
    public String dasboard(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Administrador admin = service.adminFindById(usuario.getId());
        model.addAttribute("admin", admin);
        return "presentation/admin/AdminDashboard";
    }

    @GetMapping("/admin/EmpresasPendientes")
    public String empresasPendientes(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Administrador admin = service.adminFindById(usuario.getId());
        model.addAttribute("admin", admin);
        model.addAttribute("empresas", service.empresasPendientes());
        return "presentation/admin/EmpresasPendientes";
    }

    @PostMapping("/admin/aprobarEmpresa")
    public String aprobarEmpresa(@RequestParam Integer usuarioId) {
        service.aprobarEmpresa(usuarioId);
        return "redirect:/admin/EmpresasPendientes";
    }

    @GetMapping("/admin/OferentesPendientes")
    public String oferentesPendientes(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Administrador admin = service.adminFindById(usuario.getId());
        model.addAttribute("admin", admin);
        model.addAttribute("oferentes", service.oferentesPendientes());
        return "presentation/admin/OferentesPendientes";
    }
    @PostMapping("/admin/aprobarOferente")
    public String aprobarOferente(@RequestParam Integer usuarioId) {
        service.aprobarOferente(usuarioId);
        return "redirect:/admin/OferentesPendientes";
    }

    @GetMapping("/admin/Caracteristicas")
    public String caracteristicas(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Administrador admin = service.adminFindById(usuario.getId());
        List<Caracteristica> raices = service.caracteristicasRaiz();
        model.addAttribute("admin", admin);
        model.addAttribute("caracteristicas", raices);
        model.addAttribute("tieneHijos", service.mapTieneHijos(raices));
        model.addAttribute("todasCaracteristicas", service.caracteristicasRaiz());
        return "presentation/admin/Caracteristicas";
    }

    @GetMapping("/admin/Caracteristicas/hijos/{id}")
    public String caracteristicasHijos(@PathVariable Integer id, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Administrador admin = service.adminFindById(usuario.getId());
        Caracteristica padre = service.caracteristicaFindById(id);
        List<Caracteristica> hijos = service.caracteristicasHijos(padre);
        model.addAttribute("admin", admin);
        model.addAttribute("caracteristicas", hijos);
        model.addAttribute("tieneHijos", service.mapTieneHijos(hijos));
        model.addAttribute("padre", padre);
        model.addAttribute("todasCaracteristicas", service.caracteristicasRaiz());
        return "presentation/admin/Caracteristicas";
    }

    @PostMapping("/admin/crearCaracteristica")
    public String crearCaracteristica(@RequestParam String nombre,
                                      @RequestParam(required = false) Integer padreId) {
        service.crearCaracteristica(nombre, padreId);
        return "redirect:/admin/Caracteristicas";
    }

    @GetMapping("/admin/Reportes")
    public String reportes(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Administrador admin = service.adminFindById(usuario.getId());
        model.addAttribute("admin", admin);
        model.addAttribute("annioActual", java.time.Year.now().getValue());
        return "presentation/admin/Reportes";
    }


    @GetMapping("/admin/salir")
    public String salir(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
