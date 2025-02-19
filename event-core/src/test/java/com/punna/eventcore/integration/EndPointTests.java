package com.punna.eventcore.integration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punna.eventcore.client.CatalogServiceWebClient;
import com.punna.eventcore.repository.EventRepository;
import com.punna.eventcore.repository.SeatingLayoutRepository;
import com.punna.eventcore.repository.VenueRepository;
import com.punna.eventcore.service.EventSeatStateService;
import com.punna.eventcore.service.EventService;
import com.punna.eventcore.service.SeatingLayoutService;
import com.punna.eventcore.service.VenueService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;

@Component
public class EndPointTests extends ContainerBase {

  @Autowired
  EventService eventService;

  @Autowired
  EventRepository eventRepository;

  @Autowired
  VenueRepository venueRepository;

  @Autowired
  VenueService venueService;

  @Autowired
  SeatingLayoutService seatingLayoutService;

  @Autowired
  SeatingLayoutRepository seatingLayoutRepository;

  @Autowired
  EventSeatStateService eventSeatStateService;

  @Autowired
  ObjectMapper objectMapper;
  @MockitoBean
  private CatalogServiceWebClient catalogServiceWebClient;

  @SneakyThrows
  <T> T clone(T obj, Class<T> clazz) {
    String s = objectMapper.writeValueAsString(obj);
    return objectMapper.readValue(s, clazz);
  }

  protected void mockCatalogServiceWebClient(boolean result) {
    when(catalogServiceWebClient.checkMovieIdExists(anyString())).thenReturn(
        Mono.just(result));
  }

}
