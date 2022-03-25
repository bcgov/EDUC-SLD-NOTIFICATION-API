package ca.bc.gov.educ.api.sld.notification.repository;

import ca.bc.gov.educ.api.sld.notification.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Event repository.
 */
@Repository
public interface EventRepository extends JpaRepository<EventEntity, UUID> {
  /**
   * Find by event id optional.
   *
   * @param eventId the event id
   * @return the optional
   */
  Optional<EventEntity> findByEventId(UUID eventId);

  /**
   * Find all by event status list.
   *
   * @param eventStatus the event status
   * @return the list
   */
  List<EventEntity> findAllByEventStatusOrderByCreateDate(String eventStatus);


  @Transactional
  @Modifying
  @Query("delete from EventEntity where createDate <= :createDate")
  void deleteByCreateDateBefore(LocalDateTime createDate);
}
