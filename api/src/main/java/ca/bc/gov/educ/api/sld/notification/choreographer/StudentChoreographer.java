package ca.bc.gov.educ.api.sld.notification.choreographer;

import ca.bc.gov.educ.api.sld.notification.model.EventEntity;
import ca.bc.gov.educ.api.sld.notification.service.EventHandlerService;
import ca.bc.gov.educ.api.sld.notification.struct.v1.EventType;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jboss.threads.EnhancedQueueExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * The type Student choreographer.
 */
@Component
@Slf4j
public class StudentChoreographer {
  /**
   * The Task executor.
   */
  private final Executor taskExecutor = new EnhancedQueueExecutor.Builder()
    .setThreadFactory(new ThreadFactoryBuilder().setNameFormat("task-executor-%d").build())
    .setCorePoolSize(2).setMaximumPoolSize(2).build();
  /**
   * The Event service map.
   */
  private final Map<String, EventHandlerService> eventServiceMap;

  /**
   * Instantiates a new Student choreographer.
   *
   * @param eventHandlerServices the event handler services
   */
  public StudentChoreographer(final List<EventHandlerService> eventHandlerServices) {
    this.eventServiceMap = eventHandlerServices.stream().collect(Collectors.toMap(EventHandlerService::getEventType, Function.identity()));
  }

  public boolean canHandleEvent(EventType eventType) {
    return this.eventServiceMap.containsKey(eventType.toString());
  }

  /**
   * Handle event.
   *
   * @param eventEntity the event entity
   */
  public void handleEvent(@NonNull final EventEntity eventEntity) {
    this.taskExecutor.execute(() -> {
      try {
        this.eventServiceMap.get(eventEntity.getEventType()).processEvent(eventEntity);
      } catch (final Exception exception) {
        log.error("Exception while processing event :: {}", eventEntity, exception);
      }
    });
  }
}
