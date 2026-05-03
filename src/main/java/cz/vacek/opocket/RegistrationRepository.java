package cz.vacek.opocket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByUser(User user);
    List<Registration> findByEventId(Long eventId);
    List<Registration> findByCategoryIdAndStatusOrderByDurationInSecondsAsc(Long categoryId, String status);
}