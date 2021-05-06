package ca.bc.gov.educ.api.sld.notification.service;

import ca.bc.gov.educ.api.sld.notification.model.EventEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Event handler service.
 */
public interface EventHandlerService {

  /**
   * Process event.
   *
   * @param eventEntity the event entity
   * @throws JsonProcessingException the json processing exception
   */
  @Transactional
  void processEvent(EventEntity eventEntity) throws JsonProcessingException;

  /**
   * Gets event type.
   *
   * @return the event type
   */
  String getEventType();
}
