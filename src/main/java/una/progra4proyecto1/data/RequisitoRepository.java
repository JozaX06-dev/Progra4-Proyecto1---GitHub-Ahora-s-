package una.progra4proyecto1.data;
import una.progra4proyecto1.logic.Requisito;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequisitoRepository extends CrudRepository<Requisito,Integer> {
    List<Requisito> findByCaracteristicaIdIn(List<Integer> caracteristicaIds);
}