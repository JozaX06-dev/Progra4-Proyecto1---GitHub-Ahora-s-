package una.progra4proyecto1.data;
import una.progra4proyecto1.logic.Administrador;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends CrudRepository<Administrador,Integer> {

}

