package ca.bc.gov.educ.api.sld.notification.service;

import ca.bc.gov.educ.api.sld.notification.messaging.MessagePublisher;
import ca.bc.gov.educ.api.sld.notification.properties.ApplicationProperties;
import ca.bc.gov.educ.api.sld.notification.repository.EventRepository;
import ca.bc.gov.educ.api.sld.notification.struct.v1.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * The type Student create merge service.
 */
@Service
@Slf4j
public class StudentMergedEventHandlerService extends BaseStudentMergeEventHandlerService {


  /**
   * Instantiates a new Student create merge service.
   *
   * @param eventRepository       the event repository
   * @param applicationProperties the application properties
   * @param messagePublisher      the message publisher
   */
  @Autowired
  public StudentMergedEventHandlerService(final EventRepository eventRepository, final ApplicationProperties applicationProperties, final MessagePublisher messagePublisher) {
    super(eventRepository, applicationProperties, messagePublisher);
  }


  @Override
  public String getEventType() {
    return EventType.CREATE_MERGE.toString();
  }

}
