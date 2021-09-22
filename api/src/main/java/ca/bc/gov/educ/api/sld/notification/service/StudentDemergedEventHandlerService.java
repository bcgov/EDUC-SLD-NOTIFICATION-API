package ca.bc.gov.educ.api.sld.notification.service;

import ca.bc.gov.educ.api.sld.notification.exception.BusinessException;
import ca.bc.gov.educ.api.sld.notification.messaging.MessagePublisher;
import ca.bc.gov.educ.api.sld.notification.properties.ApplicationProperties;
import ca.bc.gov.educ.api.sld.notification.repository.EventRepository;
import ca.bc.gov.educ.api.sld.notification.struct.student.v1.Student;
import ca.bc.gov.educ.api.sld.notification.struct.v1.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static ca.bc.gov.educ.api.sld.notification.struct.v1.EventType.*;


/**
 * The type Student demerged event handler service.
 */
@Service
@Slf4j
public class StudentDemergedEventHandlerService extends BaseStudentMergeEventHandlerService {


  /**
   * Instantiates a new Student merged event handler service.
   *
   * @param eventRepository       the event repository
   * @param applicationProperties the application properties
   * @param messagePublisher      the message publisher
   */
  @Autowired
  public StudentDemergedEventHandlerService(final EventRepository eventRepository, final ApplicationProperties applicationProperties, final MessagePublisher messagePublisher) {
    super(eventRepository, applicationProperties, messagePublisher);
  }


  @Override
  public String getEventType() {
    return EventType.DELETE_MERGE.toString();
  }

  @Override
  protected void processStudentsMergeInfo(final Student student, final Student trueStudent) throws BusinessException {
    this.processStudentsMergeInfo(student, trueStudent, RESTORE_SLD_STUDENTS, RESTORE_SLD_STUDENT_PROGRAMS);
  }

}
