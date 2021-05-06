package ca.bc.gov.educ.api.sld.notification.service;

import ca.bc.gov.educ.api.sld.notification.constants.EventStatus;
import ca.bc.gov.educ.api.sld.notification.exception.BusinessError;
import ca.bc.gov.educ.api.sld.notification.exception.BusinessException;
import ca.bc.gov.educ.api.sld.notification.messaging.MessagePublisher;
import ca.bc.gov.educ.api.sld.notification.model.EventEntity;
import ca.bc.gov.educ.api.sld.notification.properties.ApplicationProperties;
import ca.bc.gov.educ.api.sld.notification.repository.EventRepository;
import ca.bc.gov.educ.api.sld.notification.struct.sld.v1.*;
import ca.bc.gov.educ.api.sld.notification.struct.student.v1.Student;
import ca.bc.gov.educ.api.sld.notification.struct.student.v1.StudentMerge;
import ca.bc.gov.educ.api.sld.notification.struct.v1.Event;
import ca.bc.gov.educ.api.sld.notification.struct.v1.EventOutcome;
import ca.bc.gov.educ.api.sld.notification.struct.v1.EventType;
import ca.bc.gov.educ.api.sld.notification.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ca.bc.gov.educ.api.sld.notification.struct.v1.EventType.*;

/**
 * The type Base student merge event handler service.
 */
@Slf4j
public abstract class BaseStudentMergeEventHandlerService implements EventHandlerService {
  /**
   * The constant SLD_API_TOPIC.
   */
  public static final String SLD_API_TOPIC = "SLD_API_TOPIC";
  /**
   * The Event repository.
   */
  protected final EventRepository eventRepository;
  /**
   * The Application properties.
   */
  protected final ApplicationProperties applicationProperties;
  /**
   * The Message publisher.
   */
  protected final MessagePublisher messagePublisher;
  /**
   * The Object mapper.
   */
  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Instantiates a new Base student merge event handler service.
   *
   * @param eventRepository       the event repository
   * @param applicationProperties the application properties
   * @param messagePublisher      the message publisher
   */
  protected BaseStudentMergeEventHandlerService(final EventRepository eventRepository, final ApplicationProperties applicationProperties, final MessagePublisher messagePublisher) {
    this.eventRepository = eventRepository;
    this.applicationProperties = applicationProperties;
    this.messagePublisher = messagePublisher;
  }

  /**
   * Mark record as processed.
   *
   * @param eventEntity the event entity
   */
  protected void markRecordAsProcessed(final EventEntity eventEntity) {
    eventEntity.setEventStatus(EventStatus.PROCESSED.toString());
    eventEntity.setUpdateDate(LocalDateTime.now());
    this.eventRepository.save(eventEntity);
    log.info("event processed {}", eventEntity.getEventId());
  }


  @Override
  public void processEvent(final EventEntity eventEntity) throws JsonProcessingException {
    log.info("processing event {}", eventEntity);
    final List<StudentMerge> studentMerges = new ObjectMapper().readValue(eventEntity.getEventPayload(), new TypeReference<>() {
    });
    val mergeTo = studentMerges.stream().filter(this::mergeToPredicate).findFirst().orElseThrow();
    try {
      this.processMergeTO(mergeTo);
      this.eventRepository.findByEventId(eventEntity.getEventId()).ifPresent(this::markRecordAsProcessed);
    } catch (final BusinessException ex) {// should be retried.
      log.error("Exception :: ", ex);
    }
  }


  /**
   * Process merge to.
   *
   * @param studentMerge the student merge
   * @throws BusinessException the business exception
   */
  @SneakyThrows({JsonProcessingException.class})
  private void processMergeTO(final StudentMerge studentMerge) throws BusinessException {
    final String studentID = studentMerge.getStudentID();
    final String mergedToStudentID = studentMerge.getMergeStudentID();
    final List<String> studentIDs = new ArrayList<>();
    studentIDs.add(studentID);
    studentIDs.add(mergedToStudentID);
    log.info("called STUDENT_API to get students :: {}", studentIDs);
    final var event = Event.builder().sagaId(UUID.randomUUID()).eventType(EventType.GET_STUDENTS).eventPayload(JsonUtil.getJsonStringFromObject(studentIDs)).build();
    var responseEvent = new Event();
    var i = 0;
    while (i < 3) {
      try {
        responseEvent = JsonUtil.getJsonObjectFromByteArray(Event.class,
          this.messagePublisher.requestMessage("STUDENT_API_TOPIC", JsonUtil.getJsonBytesFromObject(event)).get(5, TimeUnit.SECONDS).getData());
        i = 3; // break out of loop
      } catch (final IOException | ExecutionException | TimeoutException e) {
        log.error("exception while getting student data", e);
        i++;
      } catch (final InterruptedException e) {
        Thread.currentThread().interrupt();
        log.error("exception while getting student data", e);
        i++;
      }
    }


    log.info("got response from STUDENT_API  :: {}", responseEvent);
    if (responseEvent.getEventOutcome() == EventOutcome.STUDENT_NOT_FOUND) {
      log.error("Students not found or student size mismatch for student IDs:: {}, this should not have happened", studentIDs);
      throw new BusinessException(BusinessError.STUDENT_NOT_FOUND);
    }
    final List<Student> students = this.objectMapper.readValue(responseEvent.getEventPayload(), new TypeReference<>() {
    });
    final Map<String, Student> studentMap = students.stream().collect(Collectors.toConcurrentMap(Student::getStudentID, Function.identity()));
    val student = studentMap.get(studentID);
    val trueStudent = studentMap.get(mergedToStudentID);
    this.processStudentsMergeInfo(student, trueStudent);
  }


  /**
   * Process students merge info.
   *
   * @param student     the student
   * @param trueStudent the true student
   * @throws BusinessException the business exception
   */
  @SneakyThrows({JsonProcessingException.class})
  private void processStudentsMergeInfo(final Student student, final Student trueStudent) throws BusinessException {
    val updateSldStudentEvent = Event.builder().eventType(UPDATE_SLD_STUDENTS).eventPayload(JsonUtil.getJsonStringFromObject(SldUpdateStudentsEvent.builder().pen(student.getPen()).sldStudent(SldStudent.builder().pen(trueStudent.getPen()).build()).build())).build();
    val updateDiaStudentsEvent = Event.builder().eventType(UPDATE_SLD_DIA_STUDENTS).eventPayload(JsonUtil.getJsonStringFromObject(SldUpdateDiaStudentsEvent.builder().pen(student.getPen()).sldDiaStudent(SldDiaStudent.builder().pen(trueStudent.getPen()).build()).build())).build();
    val updateStudentProgramsEvent = Event.builder().eventType(UPDATE_SLD_STUDENT_PROGRAMS).eventPayload(JsonUtil.getJsonStringFromObject(SldUpdateStudentProgramsEvent.builder().pen(student.getPen()).sldStudentProgram(SldStudentProgram.builder().pen(trueStudent.getPen()).build()).build())).build();
    log.info("called SLD_API to update");
    var i = 0;
    while (i < 3) {
      try {
        val futureSldStudentResponse = this.messagePublisher.requestMessage(SLD_API_TOPIC, JsonUtil.getJsonBytesFromObject(updateSldStudentEvent));
        val futureSldDiaStudentResponse = this.messagePublisher.requestMessage(SLD_API_TOPIC, JsonUtil.getJsonBytesFromObject(updateDiaStudentsEvent));
        val futureSldStudentProgramResponse = this.messagePublisher.requestMessage(SLD_API_TOPIC, JsonUtil.getJsonBytesFromObject(updateStudentProgramsEvent));
        val sldStudentResponseData = futureSldStudentResponse.get(5, TimeUnit.SECONDS).getData();
        val sldDiaStudentResponseData = futureSldDiaStudentResponse.get(5, TimeUnit.SECONDS).getData();
        val sldStudentProgramResponseData = futureSldStudentProgramResponse.get(5, TimeUnit.SECONDS).getData();
        if (sldDiaStudentResponseData.length > 0 && sldStudentResponseData.length > 0 && sldStudentProgramResponseData.length > 0) {
          log.info("got response for all 3 updates from SLD_API");
          i = 3;
        }
      } catch (final IOException | ExecutionException | TimeoutException e) {
        log.error("exception while updating sld data", e);
        i++;
      } catch (final InterruptedException e) {
        Thread.currentThread().interrupt();
        log.error("exception while updating sld data", e);
        i++;
      }
    }

  }

  /**
   * Merge to predicate boolean.
   *
   * @param studentMerge the student merge
   * @return the boolean
   */
  private boolean mergeToPredicate(final StudentMerge studentMerge) {
    return StringUtils.equals(studentMerge.getStudentMergeDirectionCode(), "TO");
  }

}
