package com.punna.eventcatalog.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.punna.eventcatalog.repository.EventRepository;
import com.punna.eventcatalog.repository.SeatingLayoutRepository;
import com.punna.eventcatalog.repository.VenueRepository;
import com.punna.eventcatalog.service.EventSeatStateService;
import com.punna.eventcatalog.service.EventService;
import com.punna.eventcatalog.service.SeatingLayoutService;
import com.punna.eventcatalog.service.VenueService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @SneakyThrows
    <T> T clone(T obj, Class<T> clazz) {
        String s = objectMapper.writeValueAsString(obj);
        return objectMapper.readValue(s, clazz);
    }


}
