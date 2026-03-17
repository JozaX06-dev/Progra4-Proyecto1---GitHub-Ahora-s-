package una.progra4proyecto1.logic;

import una.progra4proyecto1.data.*;
import org.springframework.beans.factory.annotation.*;
import java.util.List;

@org.springframework.stereotype.Service("service")
public class Service {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EmpresaRepository empresaRepository;

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
}
