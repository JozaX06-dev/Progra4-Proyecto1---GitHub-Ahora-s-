package una.progra4proyecto1.logic;

import una.progra4proyecto1.data.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@org.springframework.stereotype.Service("service")
public class Service {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PuestoRepository puestoRepository;
    @Autowired
    private OferenteRepository oferenteRepository;
    @Autowired
    private NacionalidadRepository nacionalidadRepository;
    @Autowired
    private CaracteristicaRepository caracteristicaRepository;
    @Autowired
    private HabilidadRepository habilidadRepository;
    @Autowired
    private RequisitoRepository requisitoRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public Iterable<Usuario> usuarioFindAll() {
        return usuarioRepository.findAll();
    }
    public List<Puesto> find5PuestosPublicosActivos() {
        return puestoRepository.find5PuestosPublicosActivos();
    }
    public Iterable<Oferente> oferenteFindAll() {
        return oferenteRepository.findAll();
    }
    public Iterable<Nacionalidad> nacionalidadFindAll() {
        return nacionalidadRepository.findAll();
    }
    public Nacionalidad nacionalidadFindById(String iso) {
        return nacionalidadRepository.findById(iso).orElse(null);
    }
    public Caracteristica caracteristicaFindById(int id) {
        return caracteristicaRepository.findById(id).orElse(null);
    }
    public Oferente oferenteFindById(int usuarioId) {
        return oferenteRepository.findById(usuarioId).orElse(null);
    }
    public void registrarEmpresa(String nombre, String correo, String localizacion, String telefono, String descripcion){
        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario.setActivo((byte) 0);
        usuario.setClave("");
        usuarioRepository.save(usuario);

        Empresa empresa = new Empresa();
        empresa.setUsuario(usuario);
        empresa.setNombre(nombre);
        empresa.setLocalizacion(localizacion);
        empresa.setTelefono(telefono);
        empresa.setDescripcion(descripcion);
        empresaRepository.save(empresa);
    }

    public void registrarOferente(String correo, String identificacion, String nombre, String apellido, Nacionalidad nacionalidad, String telefono, String lugarResidencia){
        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario.setActivo((byte) 0);
        usuario.setClave("");
        usuarioRepository.save(usuario);

        Oferente oferente = new Oferente();
        oferente.setUsuario(usuario);
        oferente.setIdentificacion(identificacion);
        oferente.setNombre(nombre);
        oferente.setApellido(apellido);
        oferente.setNacionalidadIso(nacionalidad);
        oferente.setTelefono(telefono);
        oferente.setLugarResidencia(lugarResidencia);
        oferenteRepository.save(oferente);
    }
    public Usuario login (String correo, String clave){
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);
            if (usuario.isPresent() && encoder.matches(clave, usuario.get().getClave()) && usuario.get().getActivo()==(byte)1) {
                return usuario.get();
            }
        return null;
    }
    public String getTipoUsuario(Integer usuarioId){
        if (adminRepository.existsById(usuarioId)) return "admin";
        if (oferenteRepository.existsById(usuarioId)) return "oferente";
        if (empresaRepository.existsById(usuarioId)) return "empresa";
        return null;
    }
    public List<Caracteristica> caracteristicasRaiz(){

        return caracteristicaRepository.findByPadreIsNull();
    }
    public List<Caracteristica> caracteristicasHijos(Caracteristica padre){
        return caracteristicaRepository.findByPadre(padre);
    }
    public List<Habilidad> habilidadesOferente(Oferente oferente){
        return habilidadRepository.findByOferente(oferente);
    }
    public Boolean caracteristicaTieneHijos(Caracteristica caracteristica){
        return caracteristicaRepository.existsByPadre(caracteristica);
    }
    public Map<Integer, Boolean> mapTieneHijos(List<Caracteristica> caracteristicas){
        Map<Integer, Boolean> map = new HashMap<>();
        for (Caracteristica caracteristica : caracteristicas) {
            map.put(caracteristica.getId(), caracteristicaRepository.existsByPadre(caracteristica));
        }
        return map;
    }
    public List<Caracteristica> caracteristicasNodosFinales(){
        List<Caracteristica> todas = (List<Caracteristica>) caracteristicaRepository.findAll();
        List<Caracteristica> finales = new ArrayList<>();
        for (Caracteristica caracteristica:todas) {
            if (!caracteristicaRepository.existsByPadre(caracteristica)) {
                finales.add(caracteristica);
            }
        }
        return finales;
    }
    public void agregarHabilidad(Oferente oferente, Caracteristica caracteristica, Integer nivel){
        Habilidad habilidad = new Habilidad();
        habilidad.setNivel(nivel);
        habilidad.setCaracteristica(caracteristica);
        habilidad.setOferente(oferente);
        habilidadRepository.save(habilidad);
    }
    public void guardarOferente(Oferente oferente){
        oferenteRepository.save(oferente);
    }
    public List<Puesto> buscarPuestosPorCaracteristicas(List<Integer> ids){
        List<Requisito> requisitos = requisitoRepository.findByCaracteristicaIdIn(ids);
        List<Puesto> puestos = new ArrayList<>();
        for (Requisito r:requisitos){
            Puesto p = r.getPuesto();
            if(p.getEsPublico()==(byte)1 && p.getActivo()==(byte)1 && !puestos.contains(p)){
                puestos.add(p);
            }
        }
        return puestos;
    }
    public List<Caracteristica> obtenerArbolCaracteristicas(){
        List<Caracteristica> resultado = new ArrayList<>();
        List<Caracteristica> raices = caracteristicasRaiz();
        for(Caracteristica raiz:raices){
            resultado.add(raiz);
            List<Caracteristica> hijos  = caracteristicasHijos(raiz);
            for(Caracteristica hijo:hijos){
                resultado.add(hijo);
            }
        }
        return resultado;
    }
    public Empresa empresaFindById(int usuarioId) {
        return empresaRepository.findById(usuarioId).orElse(null);
    }

    public List<Puesto> puestosFindByEmpresa(Empresa empresa) {
        return puestoRepository.findByEmpresa(empresa);
    }

    public Puesto puestoFindById(int id) {
        return puestoRepository.findById(id).orElse(null);
    }

    public void desactivarPuesto(int puestoId) {
        Puesto puesto = puestoRepository.findById(puestoId).orElse(null);
        if (puesto != null) {
            puesto.setActivo((byte) 0);
            puestoRepository.save(puesto);
        }
    }
    public void publicarPuesto(Empresa empresa, String descripcion, Double salario,
                               int esPublico, List<Integer> caracteristicaIds, List<Integer> niveles) {
        Puesto puesto = new Puesto();
        puesto.setEmpresa(empresa);
        puesto.setDescripcion(descripcion);
        puesto.setSalario(salario);
        puesto.setEsPublico((byte) esPublico);
        puesto.setActivo((byte) 1);
        puestoRepository.save(puesto);

        if (caracteristicaIds != null) {
            for (int i = 0; i < caracteristicaIds.size(); i++) {
                int nivel = niveles.get(i);
                if (nivel > 0) { // ← solo guarda si el nivel es mayor a 0
                    Requisito req = new Requisito();
                    req.setPuesto(puesto);
                    req.setCaracteristica(caracteristicaFindById(caracteristicaIds.get(i)));
                    req.setNivelDeseado(nivel);
                    requisitoRepository.save(req);
                }
            }
        }
    }
    public List<Map<String, Object>> buscarCandidatos(Puesto puesto) {
        List<Requisito> requisitos = requisitoRepository.findByPuesto(puesto);
        if (requisitos.isEmpty()) return new ArrayList<>();

        Map<Integer, Integer> vectorEmpresa = new HashMap<>();
        for (Requisito req : requisitos) {
            vectorEmpresa.put(req.getCaracteristica().getId(), req.getNivelDeseado());
        }

        double normaEmpresa = 0;
        for (int nivel : vectorEmpresa.values()) {
            normaEmpresa += nivel * nivel;
        }
        normaEmpresa = Math.sqrt(normaEmpresa);

        List<Oferente> todos = (List<Oferente>) oferenteRepository.findAll();
        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Oferente oferente : todos) {
            List<Habilidad> habilidades = habilidadRepository.findByOferente(oferente);

            Map<Integer, Integer> vectorOferente = new HashMap<>();
            for (Requisito req : requisitos) {
                vectorOferente.put(req.getCaracteristica().getId(), 0); // default 0
            }
            for (Habilidad hab : habilidades) {
                int id = hab.getCaracteristica().getId();
                if (vectorOferente.containsKey(id)) {
                    vectorOferente.put(id, hab.getNivel());
                }
            }

            double normaOferente = 0;
            for (int nivel : vectorOferente.values()) {
                normaOferente += nivel * nivel;
            }
            normaOferente = Math.sqrt(normaOferente);

            if (normaOferente == 0) continue;

            double productoPunto = 0;
            for (int id : vectorEmpresa.keySet()) {
                productoPunto += vectorEmpresa.get(id) * vectorOferente.getOrDefault(id, 0);
            }
            double similitud = (productoPunto / (normaEmpresa * normaOferente)) * 100;

            int cumplidos = 0;
            for (Requisito req : requisitos) {
                int nivelOferente = vectorOferente.getOrDefault(req.getCaracteristica().getId(), 0);
                if (nivelOferente >= req.getNivelDeseado()) cumplidos++;
            }
            Map<String, Object> entrada = new HashMap<>();
            entrada.put("oferente", oferente);
            entrada.put("cumplidos", cumplidos);
            entrada.put("total", requisitos.size());
            entrada.put("similitud", String.format("%.2f", similitud));
            resultado.add(entrada);
        }
        resultado.sort((a, b) -> Double.compare(
                Double.parseDouble((String) b.get("similitud")),
                Double.parseDouble((String) a.get("similitud"))
        ));

        return resultado;
    }

    public Administrador adminFindById(int usuarioId) {
        return adminRepository.findById(usuarioId).orElse(null);
    }

    public List<Empresa> empresasPendientes(){
        return empresaRepository.findByUsuarioActivo((byte) 0);
    }

    public List<Oferente> oferentesPendientes(){
        return oferenteRepository.findByUsuarioActivo((byte) 0);
    }

    public void aprobarEmpresa(int usuarioId){
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario != null){
            usuario.setActivo((byte) 1);
            usuarioRepository.save(usuario);
        }
    }

    public void aprobarOferente(int usuarioId){
        aprobarEmpresa(usuarioId);
    }

    public void crearCaracteristica(String nombre, Integer padreId) {
        Caracteristica c = new Caracteristica();
        c.setNombre(nombre);
        if (padreId != null) {
            c.setPadre(caracteristicaFindById(padreId));
        }
        caracteristicaRepository.save(c);
    }

}
