package una.progra4proyecto1.data;
import una.progra4proyecto1.logic.Oferente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OferenteRepository extends CrudRepository<Oferente,Integer> {
    List<Oferente> findByUsuarioActivo(byte activo);
}
