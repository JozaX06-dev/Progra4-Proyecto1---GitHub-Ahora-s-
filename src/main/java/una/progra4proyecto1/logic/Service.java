package una.progra4proyecto1.logic;

import una.progra4proyecto1.data.PuestoRepository;
import una.progra4proyecto1.data.UsuarioRepository;
import org.springframework.beans.factory.annotation.*;

@org.springframework.stereotype.Service("service")
public class Service {
    @Autowired
    private UsuarioRepository usuarioRepository;
    public Iterable<Usuario> usuarioFindAll() {
        return usuarioRepository.findAll();
    }
    @Autowired
    private PuestoRepository puestoRepository;
    public Iterable<Puesto> puestoFindAll() {
        return puestoRepository.findAll();
    }
}
