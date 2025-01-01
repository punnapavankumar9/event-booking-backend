package com.punna.eventcatalog.mapper;

import com.punna.eventcatalog.dto.SeatingArrangementDto;
import com.punna.eventcatalog.model.SeatingArrangement;

public class SeatingArrangementMapper {

    public static SeatingArrangement toSeatingArrangement(SeatingArrangementDto seatingArrangement) {
        return SeatingArrangement
                .builder()
                .id(seatingArrangement.getId())
                .seatTiers(seatingArrangement.getSeatTiers())
                .capacity(seatingArrangement.getCapacity())
                .build();
    }

    public static SeatingArrangementDto toSeatingArrangementDto(SeatingArrangement seatingArrangement) {
        return SeatingArrangementDto
                .builder()
                .capacity(seatingArrangement.getCapacity())
                .seatTiers(seatingArrangement.getSeatTiers())
                .id(seatingArrangement.getId())
                .build();
    }

    public static void merge(SeatingArrangement seatingArrangement, SeatingArrangementDto seatingArrangementDto) {
        if (seatingArrangementDto.getCapacity() != null) {
            seatingArrangement.setCapacity(seatingArrangementDto.getCapacity());
        }
        if (seatingArrangementDto.getSeatTiers() != null) {
            seatingArrangement.setSeatTiers(seatingArrangementDto.getSeatTiers());
        }
    }
}
