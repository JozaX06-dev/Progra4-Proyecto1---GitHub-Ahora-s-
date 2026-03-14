package una.progra4proyecto1.data;
import una.progra4proyecto1.logic.Puesto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuestoRepository extends CrudRepository<Puesto,Integer> {

}
