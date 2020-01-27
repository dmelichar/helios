package at.fhtw.ode.helios.event;

import at.fhtw.ode.helios.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;


@Repository
public interface HeliosRepository extends JpaRepository<Location, Long> {

    Location findByDate(Date date);

}