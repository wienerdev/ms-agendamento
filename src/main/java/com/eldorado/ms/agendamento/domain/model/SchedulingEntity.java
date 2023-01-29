package com.eldorado.ms.agendamento.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("scheduling")
public class SchedulingEntity {

    @Id
    private UUID schedulingId;

    private LocalDateTime schedulingDate;

    @NonNull
    private String employeeName;

    @NonNull
    private UUID employeeId;

    @NonNull
    private String clientName;

    @NonNull
    private String clientDocument;

    private Map<String, Boolean> schedulingTimes = new HashMap<>();

    @SneakyThrows
    public void registerScheduleTime(String scheduleHour) {
        if (Boolean.FALSE.equals(schedulingTimes.get(scheduleHour))) {
            schedulingTimes.put(scheduleHour, false);
        } else {
            throw new Exception("BUSY, TRY ANOTHER SCHEDULE!");
        }
    }
}
