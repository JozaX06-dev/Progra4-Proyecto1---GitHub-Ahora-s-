package una.progra4proyecto1.data;

import una.progra4proyecto1.logic.Puesto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface PuestoRepository extends CrudRepository<Puesto, Integer>{
    @Query("SELECT p from Puesto p where p.esPublico = 1 and p.activo = 1 order by p.id DESC limit 5")
    List<Puesto> find5PuestosPublicosActivos();
}

