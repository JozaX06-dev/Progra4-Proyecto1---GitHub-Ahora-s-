package una.progra4proyecto1.data;
import una.progra4proyecto1.logic.Habilidad;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import una.progra4proyecto1.logic.Oferente;
import java.util.List;

@Repository
public interface HabilidadRepository extends CrudRepository<Habilidad,Integer> {
    List<Habilidad> findByOferente(Oferente oferente);
}