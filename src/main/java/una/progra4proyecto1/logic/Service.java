package una.progra4proyecto1.logic;

import una.progra4proyecto1.data.*;
import org.springframework.beans.factory.annotation.*;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@org.springframework.stereotype.Service("service")
public class Service {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private AdminRepository adminRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public Iterable<Usuario> usuarioFindAll() {
        return usuarioRepository.findAll();
    }
    @Autowired
    private PuestoRepository puestoRepository;
    public List<Puesto> find5PuestosPublicosActivos() {
        return puestoRepository.find5PuestosPublicosActivos();
    }
    @Autowired
    private OferenteRepository oferenteRepository;
    public Iterable<Oferente> oferenteFindAll() {
        return oferenteRepository.findAll();
    }

    @Autowired
    private NacionalidadRepository nacionalidadRepository;
    public Iterable<Nacionalidad> nacionalidadFindAll() {
        return nacionalidadRepository.findAll();
    }
    public Nacionalidad nacionalidadFindById(String iso) {
        return nacionalidadRepository.findById(iso).orElse(null);
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
}
