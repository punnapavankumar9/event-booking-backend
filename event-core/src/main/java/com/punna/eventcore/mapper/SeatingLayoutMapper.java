package com.punna.eventcore.mapper;

import com.punna.eventcore.dto.SeatingLayoutDto;
import com.punna.eventcore.model.SeatingLayout;

public class SeatingLayoutMapper {

  public static SeatingLayout toSeatingLayout(SeatingLayoutDto seatingLayout) {
    return SeatingLayout
        .builder()
        .id(seatingLayout.getId())
        .capacity(seatingLayout.getCapacity())
        .seats(seatingLayout.getSeats())
        .rows(seatingLayout.getRows())
        .columns(seatingLayout.getColumns())
        .screenPosition(seatingLayout.getScreenPosition())
        .name(seatingLayout.getName())
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
        .screenPosition(seatingLayout.getScreenPosition())
        .name(seatingLayout.getName())
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
    if (seatingLayoutDto.getScreenPosition() != null) {
      seatingLayout.setScreenPosition(seatingLayoutDto.getScreenPosition());
    }
    if (seatingLayoutDto.getName() != null) {
      seatingLayout.setName(seatingLayoutDto.getName());
    }
  }
}
