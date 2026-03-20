package una.progra4proyecto1.data;
import una.progra4proyecto1.logic.Caracteristica;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CaracteristicaRepository extends CrudRepository<Caracteristica,Integer> {
    List<Caracteristica> findByPadre(Caracteristica padre);
    List<Caracteristica> findByPadreIsNull();
    boolean existsByPadre(Caracteristica padre);
    List<Caracteristica> findAll();
}