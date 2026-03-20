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
    public void registrarEmpresa(String nombre, String correo, String localizacion, String telefono, String descripcion, String clave){
        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario.setActivo((byte) 0);
        usuario.setClave(encoder.encode(clave));
        usuarioRepository.save(usuario);

        Empresa empresa = new Empresa();
        empresa.setUsuario(usuario);
        empresa.setNombre(nombre);
        empresa.setLocalizacion(localizacion);
        empresa.setTelefono(telefono);
        empresa.setDescripcion(descripcion);
        empresaRepository.save(empresa);
    }

    public void registrarOferente(String correo, String identificacion, String nombre, String apellido, Nacionalidad nacionalidad, String telefono, String lugarResidencia, String clave){
        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario.setActivo((byte) 0);
        usuario.setClave(encoder.encode(clave));
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
}
