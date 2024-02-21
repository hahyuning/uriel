package com.uriel.travel.dto.product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleDto {

    Long scheduleId;
    int day;
    String dayContent;
    String hotel;
    String meal;
    String vehicle;
}
