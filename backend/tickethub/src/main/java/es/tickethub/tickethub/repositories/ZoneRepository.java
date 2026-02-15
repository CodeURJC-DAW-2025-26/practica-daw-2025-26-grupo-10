package es.tickethub.tickethub.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import es.tickethub.tickethub.entities.Zone;
/**Internamente spring crea una "clase mágica" con la que hace el CRUD.
 * Id es Zone y es de tipo Long
*/

public interface ZoneRepository extends JpaRepository<Zone, Long> {
}