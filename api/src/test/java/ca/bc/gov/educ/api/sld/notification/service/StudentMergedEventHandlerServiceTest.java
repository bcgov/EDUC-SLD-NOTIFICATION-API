package ca.bc.gov.educ.api.sld.notification.service;

import ca.bc.gov.educ.api.sld.notification.messaging.MessagePublisher;
import ca.bc.gov.educ.api.sld.notification.model.EventEntity;
import ca.bc.gov.educ.api.sld.notification.repository.EventRepository;
import ca.bc.gov.educ.api.sld.notification.struct.sld.v1.SldUpdateStudentProgramsEvent;
import ca.bc.gov.educ.api.sld.notification.struct.sld.v1.SldUpdateStudentsEvent;
import ca.bc.gov.educ.api.sld.notification.struct.student.v1.Student;
import ca.bc.gov.educ.api.sld.notification.struct.student.v1.StudentMerge;
import ca.bc.gov.educ.api.sld.notification.struct.v1.Event;
import ca.bc.gov.educ.api.sld.notification.support.NatsMessageImpl;
import ca.bc.gov.educ.api.sld.notification.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.nats.client.Connection;
import io.nats.client.Message;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static ca.bc.gov.educ.api.sld.notification.struct.v1.EventOutcome.MERGE_CREATED;
import static ca.bc.gov.educ.api.sld.notification.struct.v1.EventOutcome.STUDENTS_FOUND;
import static ca.bc.gov.educ.api.sld.notification.struct.v1.EventType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class StudentMergedEventHandlerServiceTest {

  @Autowired
  MessagePublisher messagePublisher;

  @Autowired
  Connection connection;

  @Autowired
  StudentMergedEventHandlerService studentMergedEventHandlerService;

  @Autowired
  EventRepository eventRepository;

  @After
  public void after() {
    this.eventRepository.deleteAll();
    Mockito.reset(messagePublisher);
  }

  @Test
  public void handleEvent_givenCreateMergeEvent_shouldSendEventsToSldApi() throws JsonProcessingException {
    final EventEntity eventEntity = getCreateMergeEventEntity();

    final Message studentMessage = createStudentResponseMessage();
    final Message sldMessage = createSldResponseMessage();
    ArgumentCaptor<byte[]> eventCaptor = ArgumentCaptor.forClass(byte[].class);

    when(this.messagePublisher.requestMessage(eq("STUDENT_API_TOPIC"), any())).thenReturn(CompletableFuture.completedFuture(studentMessage));
    when(this.messagePublisher.requestMessage(eq("SLD_API_TOPIC"), any())).thenReturn(CompletableFuture.completedFuture(sldMessage));
    this.studentMergedEventHandlerService.processEvent(eventEntity);
    verify(this.messagePublisher, atLeast(2)).requestMessage(eq("SLD_API_TOPIC"), eventCaptor.capture());
    final var sldRequests = eventCaptor.getAllValues();
    assertThat(sldRequests).size().isEqualTo(2);

    final var updateSldStudentEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(sldRequests.get(0)));
    assertThat(updateSldStudentEvent).isNotNull();
    assertThat(updateSldStudentEvent.getEventType()).isEqualTo(UPDATE_SLD_STUDENTS);
    final var updateSldStudentEventPayload = JsonUtil.getJsonObjectFromString(SldUpdateStudentsEvent.class, updateSldStudentEvent.getEventPayload());
    assertThat(updateSldStudentEventPayload.getPen()).isEqualTo("123456781");
    assertThat(updateSldStudentEventPayload.getSldStudent().getPen()).isEqualTo("123456782");

    final var updateStudentProgramsEvent = JsonUtil.getJsonObjectFromString(Event.class, new String(sldRequests.get(1)));
    assertThat(updateStudentProgramsEvent).isNotNull();
    assertThat(updateStudentProgramsEvent.getEventType()).isEqualTo(UPDATE_SLD_STUDENT_PROGRAMS);
    final var updateStudentProgramsEventPayload = JsonUtil.getJsonObjectFromString(SldUpdateStudentProgramsEvent.class, updateStudentProgramsEvent.getEventPayload());
    assertThat(updateStudentProgramsEventPayload.getPen()).isEqualTo("123456781");
    assertThat(updateStudentProgramsEventPayload.getSldStudentProgram().getPen()).isEqualTo("123456782");

  }

  private EventEntity getCreateMergeEventEntity() throws JsonProcessingException {
    final var payload = this.createStudentMergePayload();
    return EventEntity.builder()
      .sldNotificationEventId(UUID.randomUUID())
      .eventType(CREATE_MERGE.toString())
      .eventOutcome(MERGE_CREATED.toString())
      .eventPayload(JsonUtil.getJsonStringFromObject(payload))
      .eventId(UUID.randomUUID())
      .build();
  }

  private Message createSldResponseMessage() {
    return NatsMessageImpl.builder()
        .connection(this.connection)
        .data("{}".getBytes())
        .SID("SID")
        .replyTo("TEST_TOPIC")
        .build();
  }

  private Message createStudentResponseMessage() throws JsonProcessingException {
    final var students = this.createStudentsPayload();
    final var studentEvent = Event.builder()
      .eventType(GET_STUDENTS)
      .eventOutcome(STUDENTS_FOUND)
      .eventPayload(JsonUtil.getJsonStringFromObject(students))
      .build();
    return NatsMessageImpl.builder()
      .connection(this.connection)
      .data(JsonUtil.getJsonBytesFromObject(studentEvent))
      .SID("SID")
      .replyTo("TEST_TOPIC")
      .build();
  }

  private List<StudentMerge> createStudentMergePayload() {
    return List.of(
      StudentMerge.builder()
        .studentID("7f000101-7151-1d84-8171-5187006c0001")
        .mergeStudentID("7f000101-7151-1d84-8171-5187006c0003")
        .studentMergeDirectionCode("TO")
        .studentMergeSourceCode("MI")
        .build(),
      StudentMerge.builder()
        .studentID("7f000101-7151-1d84-8171-5187006c0003")
        .mergeStudentID("7f000101-7151-1d84-8171-5187006c0001")
        .studentMergeDirectionCode("FROM")
        .studentMergeSourceCode("MI")
        .build()
    );
  }

  private List<Student> createStudentsPayload() {
    return List.of(
      Student.builder().studentID("7f000101-7151-1d84-8171-5187006c0001").pen("123456781").build(),
      Student.builder().studentID("7f000101-7151-1d84-8171-5187006c0003").pen("123456782").build()
    );
  }
}
