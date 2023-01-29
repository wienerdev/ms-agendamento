package com.eldorado.ms.agendamento.dto;

import com.eldorado.commons.dto.UserLoginDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterScheduleDto {

    private UserLoginDto user;
    private String scheduleTime;
    private UUID employeeId;


}
