package una.progra4proyecto1.data;

import una.progra4proyecto1.logic.Nacionalidad;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NacionalidadRepository extends CrudRepository<Nacionalidad,String> {
}
