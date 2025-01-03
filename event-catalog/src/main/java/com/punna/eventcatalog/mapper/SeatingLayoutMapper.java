package com.punna.eventcatalog.mapper;

import com.punna.eventcatalog.dto.SeatingLayoutDto;
import com.punna.eventcatalog.model.SeatingLayout;

public class SeatingLayoutMapper {

    public static SeatingLayout toSeatingLayout(SeatingLayoutDto seatingLayout) {
        return SeatingLayout
                .builder()
                .id(seatingLayout.getId())
                .capacity(seatingLayout.getCapacity())
                .seats(seatingLayout.getSeats())
                .rows(seatingLayout.getRows())
                .columns(seatingLayout.getColumns())
                .build();
    }

    public static SeatingLayoutDto toSeatingLayoutDto(SeatingLayout seatingLayout) {
        return SeatingLayoutDto
                .builder()
                .capacity(seatingLayout.getCapacity())
                .seats(seatingLayout.getSeats())
                .rows(seatingLayout.getRows())
                .columns(seatingLayout.getColumns())
                .id(seatingLayout.getId())
                .build();
    }

    public static void merge(SeatingLayout seatingLayout, SeatingLayoutDto seatingLayoutDto) {
        if (seatingLayoutDto.getCapacity() != null) {
            seatingLayout.setCapacity(seatingLayoutDto.getCapacity());
        }
        if (seatingLayoutDto.getSeats() != null) {
            seatingLayout.setSeats(seatingLayoutDto.getSeats());
        }
        if (seatingLayoutDto.getRows() != null) {
            seatingLayout.setRows(seatingLayoutDto.getRows());
        }
        if (seatingLayoutDto.getColumns() != null) {
            seatingLayout.setColumns(seatingLayoutDto.getColumns());
        }
    }
}
