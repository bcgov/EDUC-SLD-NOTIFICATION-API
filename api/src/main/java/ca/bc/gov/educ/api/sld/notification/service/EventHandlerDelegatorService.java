package ca.bc.gov.educ.api.sld.notification.service;

import ca.bc.gov.educ.api.sld.notification.choreographer.StudentChoreographer;
import ca.bc.gov.educ.api.sld.notification.exception.BusinessException;
import ca.bc.gov.educ.api.sld.notification.struct.v1.ChoreographedEvent;
import io.nats.client.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;


/**
 * The type Event handler delegator service.
 */
@Service
@Slf4j
public class EventHandlerDelegatorService {

  /**
   * The Choreographed event persistence service.
   */
  private final ChoreographedEventPersistenceService choreographedEventPersistenceService;
  /**
   * The Student choreographer.
   */
  private final StudentChoreographer studentChoreographer;

  /**
   * Instantiates a new Event handler delegator service.
   *
   * @param choreographedEventPersistenceService the choreographed event persistence service
   * @param studentChoreographer                 the student choreographer
   */
  @Autowired
  public EventHandlerDelegatorService(final ChoreographedEventPersistenceService choreographedEventPersistenceService, final StudentChoreographer studentChoreographer) {
    this.choreographedEventPersistenceService = choreographedEventPersistenceService;
    this.studentChoreographer = studentChoreographer;
  }

  /**
   * Handle choreography event.
   *
   * @param choreographedEvent the choreographed event
   * @param message            the message
   */
  public void handleChoreographyEvent(@NonNull final ChoreographedEvent choreographedEvent, @NonNull final Message message) {

    if (this.studentChoreographer.canHandleEvent(choreographedEvent.getEventType())) {
      try {
        final var persistedEvent = this.choreographedEventPersistenceService.persistEventToDB(choreographedEvent);
        message.ack(); // acknowledge to Jet Stream that api got the message and it is now in DB.
        log.info("acknowledged to Jet Stream...");
        this.studentChoreographer.handleEvent(persistedEvent);
      } catch (final BusinessException businessException) {
        message.ack(); // acknowledge to Jet Stream that api got the message already...
        log.info("acknowledged to  Jet Stream...");
      }
    } else {
      message.ack();
      log.warn("API not interested in other events, ignoring event :: {}", choreographedEvent);
    }

  }
}
