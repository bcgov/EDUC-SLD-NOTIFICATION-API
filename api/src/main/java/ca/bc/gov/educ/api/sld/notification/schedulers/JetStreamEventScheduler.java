package ca.bc.gov.educ.api.sld.notification.schedulers;

import static java.util.stream.Collectors.toList;

import ca.bc.gov.educ.api.sld.notification.choreographer.StudentChoreographer;
import ca.bc.gov.educ.api.sld.notification.constants.EventStatus;
import ca.bc.gov.educ.api.sld.notification.model.EventEntity;
import ca.bc.gov.educ.api.sld.notification.repository.EventRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 * The type Jet stream event scheduler.
 */
@Component
@Slf4j
public class JetStreamEventScheduler {

  /**
   * The Event repository.
   */
  private final EventRepository eventRepository;

  /**
   * The Student choreographer.
   */
  private final StudentChoreographer studentChoreographer;


  /**
   * Instantiates a new Jet stream event scheduler.
   *
   * @param eventRepository      the event repository
   * @param studentChoreographer the student choreographer
   */
  public JetStreamEventScheduler(final EventRepository eventRepository, final StudentChoreographer studentChoreographer) {
    this.eventRepository = eventRepository;
    this.studentChoreographer = studentChoreographer;
  }

  /**
   * Find and process events.
   */
  @Scheduled(cron = "${cron.scheduled.process.events.stan}") // every 5 minutes
  @SchedulerLock(name = "PROCESS_CHOREOGRAPHED_EVENTS_FROM_JET_STREAM", lockAtLeastFor = "${cron.scheduled.process.events.stan.lockAtLeastFor}", lockAtMostFor = "${cron.scheduled.process.events" +
    ".stan.lockAtMostFor}")
  public void findAndProcessEvents() {
    LockAssert.assertLocked();
    List<EventEntity> results = this.eventRepository.findAllByEventStatusOrderByCreateDate(EventStatus.DB_COMMITTED.toString())
        .stream()
        .filter(el -> el.getUpdateDate().isBefore(LocalDateTime.now().minusMinutes(5)))
        .collect(toList());

    if (!results.isEmpty()) {
      log.info("found {} choreographed events which needs to be processed in SLD-NOTIFICATION-API.", results.size());
      results.forEach(this.studentChoreographer::handleEvent);
    }
  }
}
