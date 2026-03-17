package una.progra4proyecto1.data;
import una.progra4proyecto1.logic.Empresa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends CrudRepository<Empresa,Integer> {

}
