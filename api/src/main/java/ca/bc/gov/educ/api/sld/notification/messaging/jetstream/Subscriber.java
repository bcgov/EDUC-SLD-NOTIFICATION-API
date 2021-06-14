package ca.bc.gov.educ.api.sld.notification.messaging.jetstream;

import ca.bc.gov.educ.api.sld.notification.helpers.LogHelper;
import ca.bc.gov.educ.api.sld.notification.properties.ApplicationProperties;
import ca.bc.gov.educ.api.sld.notification.service.EventHandlerDelegatorService;
import ca.bc.gov.educ.api.sld.notification.struct.v1.ChoreographedEvent;
import ca.bc.gov.educ.api.sld.notification.struct.v1.EventType;
import ca.bc.gov.educ.api.sld.notification.util.JsonUtil;
import io.nats.client.Connection;
import io.nats.client.JetStreamApiException;
import io.nats.client.Message;
import io.nats.client.PushSubscribeOptions;
import io.nats.client.api.ConsumerConfiguration;
import io.nats.client.api.DeliverPolicy;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The type Subscriber.
 */
@Component
@Slf4j
public class Subscriber {

  /**
   * The Event handler delegator service.
   */
  private final EventHandlerDelegatorService eventHandlerDelegatorService;
  /**
   * The Stream topics map.
   */
  private final Map<String, List<String>> streamTopicsMap = new HashMap<>(); // one stream can have multiple topics.
  /**
   * The Nats connection.
   */
  private final Connection natsConnection;

  /**
   * Instantiates a new Subscriber.
   *
   * @param natsConnection               the nats connection
   * @param eventHandlerDelegatorService the event handler delegator service
   */
  @Autowired
  public Subscriber(final Connection natsConnection, final EventHandlerDelegatorService eventHandlerDelegatorService) {
    this.eventHandlerDelegatorService = eventHandlerDelegatorService;
    this.natsConnection = natsConnection;
    this.initializeStreamTopicMap();
  }

  /**
   * Initialize stream topic map.
   */
  private void initializeStreamTopicMap() {
    List<String> penServicesEventsTopics = new ArrayList<>();
    penServicesEventsTopics.add("PEN_SERVICES_EVENTS_TOPIC");
    this.streamTopicsMap.put("PEN_SERVICES_EVENTS", penServicesEventsTopics);
  }

  /**
   * Subscribe.
   *
   * @throws IOException           the io exception
   * @throws JetStreamApiException the jet stream api exception
   */
  @PostConstruct
  public void subscribe() throws IOException, JetStreamApiException {
    val qName = ApplicationProperties.API_NAME.concat("-QUEUE");
    val autoAck = false;
    for (val entry : streamTopicsMap.entrySet()) {
      for (val topic : entry.getValue()) {
        PushSubscribeOptions options = PushSubscribeOptions.builder().stream(entry.getKey())
          .durable(ApplicationProperties.API_NAME.concat("-DURABLE"))
          .configuration(ConsumerConfiguration.builder().deliverPolicy(DeliverPolicy.New).build()).build();
        this.natsConnection.jetStream().subscribe(topic, qName, this.natsConnection.createDispatcher(), this::onMessage,
          autoAck, options);
      }
    }
  }

  /**
   * On message.
   *
   * @param message the message
   */
  public void onMessage(final Message message) {
    if (message != null) {
      log.info("Received message Subject:: {} , SID :: {} , sequence :: {}, pending :: {} ", message.getSubject(), message.getSID(), message.metaData().consumerSequence(), message.metaData().pendingCount());
      try {
        val eventString = new String(message.getData());
        LogHelper.logMessagingEventDetails(eventString);
        val event = JsonUtil.getJsonObjectFromString(ChoreographedEvent.class, eventString);
        if (event.getEventPayload() == null) {
          message.ack();
          log.warn("payload is null, ignoring event :: {}", event);
          return;
        }
        if (event.getEventType().equals(EventType.CREATE_MERGE)) {
          this.eventHandlerDelegatorService.handleChoreographyEvent(event, message);
        } else {
          message.ack();
          log.warn("API not interested in other events, ignoring event :: {}", event);
        }

      } catch (final Exception ex) {
        log.error("Exception ", ex);
      }
    }
  }

}
