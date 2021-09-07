package ca.bc.gov.educ.api.sld.notification.service;

import ca.bc.gov.educ.api.sld.notification.messaging.MessagePublisher;
import ca.bc.gov.educ.api.sld.notification.model.EventEntity;
import ca.bc.gov.educ.api.sld.notification.repository.EventRepository;
import ca.bc.gov.educ.api.sld.notification.struct.sld.v1.SldUpdateStudentsEvent;
import ca.bc.gov.educ.api.sld.notification.struct.student.v1.Student;
import ca.bc.gov.educ.api.sld.notification.struct.student.v1.StudentMerge;
import ca.bc.gov.educ.api.sld.notification.struct.v1.ChoreographedEvent;
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
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static ca.bc.gov.educ.api.sld.notification.struct.v1.EventOutcome.*;
import static ca.bc.gov.educ.api.sld.notification.struct.v1.EventType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EventHandlerDelegatorServiceTest {

  @Autowired
  MessagePublisher messagePublisher;

  @Autowired
  Connection connection;

  @Autowired
  EventHandlerDelegatorService eventHandlerDelegatorService;

  @Autowired
  EventRepository eventRepository;

  @After
  public void after() {
    this.eventRepository.deleteAll();
    Mockito.reset(messagePublisher);
  }

  @Test
  public void handleEvent_givenCreateMergeEvent_shouldRespondToSTAN() throws JsonProcessingException {
    final var payload = this.createStudentMergePayload();
    final var event = ChoreographedEvent.builder()
        .eventType(CREATE_MERGE)
        .eventOutcome(MERGE_CREATED)
        .eventPayload(JsonUtil.getJsonStringFromObject(payload))
        .eventID(UUID.randomUUID())
        .build();
    final Message message = Mockito.mock(Message.class);

    final Message studentMessage = createStudentResponseMessage();
    final Message sldMessage = createSldResponseMessage();

    when(this.messagePublisher.requestMessage(eq("STUDENT_API_TOPIC"), any())).thenReturn(CompletableFuture.completedFuture(studentMessage));
    when(this.messagePublisher.requestMessage(eq("SLD_API_TOPIC"), any())).thenReturn(CompletableFuture.completedFuture(sldMessage));
    this.eventHandlerDelegatorService.handleChoreographyEvent(event, message);

    verify(message).ack();

  }

  @Test
  public void handleEvent_givenDeleteMergeEvent_shouldRespondToSTAN() throws JsonProcessingException {
    final var payload = this.createStudentMergePayload();
    final var event = ChoreographedEvent.builder()
      .eventType(DELETE_MERGE)
      .eventOutcome(MERGE_DELETED)
      .eventPayload(JsonUtil.getJsonStringFromObject(payload))
      .eventID(UUID.randomUUID())
      .build();
    final Message message = Mockito.mock(Message.class);

    final Message studentMessage = createStudentResponseMessage();
    final Message sldMessage = createSldResponseMessage();

    when(this.messagePublisher.requestMessage(eq("STUDENT_API_TOPIC"), any())).thenReturn(CompletableFuture.completedFuture(studentMessage));
    when(this.messagePublisher.requestMessage(eq("SLD_API_TOPIC"), any())).thenReturn(CompletableFuture.completedFuture(sldMessage));
    this.eventHandlerDelegatorService.handleChoreographyEvent(event, message);

    verify(message).ack();

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
