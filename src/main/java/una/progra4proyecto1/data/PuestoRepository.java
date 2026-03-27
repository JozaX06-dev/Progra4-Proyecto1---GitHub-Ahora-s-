package una.progra4proyecto1.data;

import una.progra4proyecto1.logic.Empresa;
import una.progra4proyecto1.logic.Puesto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.List;

@Repository
public interface PuestoRepository extends CrudRepository<Puesto, Integer>{
    @Query("SELECT p from Puesto p where p.esPublico = 1 and p.activo = 1 order by p.id DESC limit 5")
    List<Puesto> find5PuestosPublicosActivos();
    List<Puesto> findByEmpresa(Empresa empresa);
    @Query("SELECT p FROM Puesto p WHERE YEAR(p.fechaCreacion) = :anio")
    List<Puesto> findByAnio(@Param("anio") int anio);
}

