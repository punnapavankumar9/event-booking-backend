package com.punna.eventcatalog.mapper;

import com.punna.eventcatalog.dto.VenueDto;
import com.punna.eventcatalog.model.Venue;

public class VenueMapper {

    public static VenueDto toVenueDto(Venue venue) {
        return VenueDto
                .builder()
                .id(venue.getId())
                .name(venue.getName())
                .description(venue.getDescription())
                .capacity(venue.getCapacity())
                .ownerId(venue.getOwnerId())
                .location(venue.getLocation())
                .city(venue.getCity())
                .country(venue.getCountry())
                .pincode(venue.getPincode())
                .state(venue.getState())
                .googleMapsUrl(venue.getGoogleMapsUrl())
                .build();
    }

    public static Venue toVenue(VenueDto venueDto) {
        return Venue
                .builder()
                .id(venueDto.getId())
                .name(venueDto.getName())
                .description(venueDto.getDescription())
                .capacity(venueDto.getCapacity())
                .ownerId(venueDto.getOwnerId())
                .location(venueDto.getLocation())
                .city(venueDto.getCity())
                .country(venueDto.getCountry())
                .pincode(venueDto.getPincode())
                .state(venueDto.getState())
                .googleMapsUrl(venueDto.getGoogleMapsUrl())
                .build();
    }

    public static Venue merge(VenueDto venueDto, Venue venue) {
        if (venueDto == null) {
            return null;
        }
        if (venueDto.getName() != null) {
            venue.setName(venueDto.getName());
        }
        if (venueDto.getDescription() != null) {
            venue.setDescription(venueDto.getDescription());
        }
        if (venueDto.getCapacity() != null) {
            venue.setCapacity(venueDto.getCapacity());
        }
        if (venueDto.getOwnerId() != null) {
            venue.setOwnerId(venueDto.getOwnerId());
        }
        if (venueDto.getLocation() != null) {
            venue.setLocation(venueDto.getLocation());
        }
        if (venueDto.getCity() != null) {
            venue.setCity(venueDto.getCity());
        }
        if (venueDto.getCountry() != null) {
            venue.setCountry(venueDto.getCountry());
        }
        if (venueDto.getPincode() != null) {
            venue.setPincode(venueDto.getPincode());
        }
        if (venueDto.getState() != null) {
            venue.setState(venueDto.getState());
        }
        if (venueDto.getGoogleMapsUrl() != null) {
            venue.setGoogleMapsUrl(venueDto.getGoogleMapsUrl());
        }
        return venue;
    }
}
