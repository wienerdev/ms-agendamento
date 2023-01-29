package com.eldorado.ms.agendamento.service;

import com.eldorado.commons.dto.MessageDto;
import com.eldorado.commons.dto.UserDto;
import com.eldorado.commons.dto.UserLoginDto;
import com.eldorado.ms.agendamento.domain.model.SchedulingEntity;
import com.eldorado.ms.agendamento.domain.repository.SchedulingRepository;
import com.eldorado.ms.agendamento.dto.CustomSchedulingList;
import com.eldorado.ms.agendamento.dto.RegisterScheduleDto;
import com.eldorado.ms.agendamento.dto.SchedulingDto;
import com.eldorado.ms.agendamento.feign.EmployeeInterface;
import com.eldorado.ms.agendamento.feign.UserInterface;
import com.eldorado.ms.agendamento.publisher.SchedulingPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulingService implements UserDetailsService {

    private static final String MESSAGE = "Agendamento realizado\nData do Agendamento: %s\nNome do Funcionário: %s";

    private static final String SUBJECT = "NÃO RESPONDA";

    private final SchedulingRepository schedulingRepository;

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    private final SchedulingPublisher schedulingPublisher;

    @Autowired
    private UserInterface userInterface;

    @Autowired
    private EmployeeInterface employeeInterface;

    public SchedulingDto createEmployeeSchedule(@Validated SchedulingDto dto) {
        var schedulingEntity = modelMapper.map(dto, SchedulingEntity.class);

        schedulingEntity.setSchedulingId(UUID.randomUUID());

        schedulingEntity = schedulingRepository.save(schedulingEntity);
        log.info("schedulingEntity Saved with sucefull {}", schedulingEntity);

        return modelMapper.map(schedulingEntity, SchedulingDto.class);
    }

    public List<SchedulingDto> getAllEmployeesSchedule() {
        List<SchedulingEntity> schedulingList = schedulingRepository.findAll();

        return modelMapper.map(schedulingList, CustomSchedulingList.class);
    }

    @SneakyThrows
    public SchedulingDto getEmployeeScheduleById(UUID id) {
        var scheduling = schedulingRepository.findById(id)
                .orElseThrow(() -> new Exception("scheduling NOT FOUND"));

        return modelMapper.map(scheduling, SchedulingDto.class);
    }

    @SneakyThrows
    public SchedulingDto updateEmployeeSchedule(@Validated SchedulingDto dto) {

        var employee = schedulingRepository.findById(dto.getSchedulingId())
                .orElseThrow(() -> new Exception("EMPLOYEE NOT FOUND"));

        return modelMapper.map(schedulingRepository.save(employee), SchedulingDto.class);
    }

    public void deleteEmployee(UUID id) {
        schedulingRepository.deleteById(id);
    }

    @SneakyThrows
    public SchedulingDto schedule(RegisterScheduleDto dto) {

        var userDto = userInterface.getLogin(UserLoginDto.builder().userName(dto.getUser().getUserName()).password(dto.getUser().getPassword()).build()).getBody();
        var employeeDto = employeeInterface.getEmployeeById(dto.getEmployeeId()).getBody();

        Optional.of(userDto).orElseThrow(() -> new Exception("USER NOT FOUND"));
        Optional.of(employeeDto).orElseThrow(() -> new Exception("EMPLOYEE NOT FOUND"));

        SchedulingEntity schedule = SchedulingEntity.builder()
                .employeeId(employeeDto.getEmployeeCode())
                .employeeName(employeeDto.getName())
                .clientName(userDto.getName())
                .clientDocument(userDto.getDocument())
                .schedulingId(UUID.randomUUID())
                .schedulingDate(LocalDateTime.now())
                .build();

        schedule.registerScheduleTime(dto.getScheduleTime());

        schedulingRepository.save(schedule);

        return modelMapper.map(schedule, SchedulingDto.class);
    }

    @SneakyThrows
    private void sendMessage(LocalDateTime dtAgendamento, String nomeFuncionario, UserDto userDto) {
        var message = MessageDto.builder().to(userDto.getUserName()).message(String.format(MESSAGE, dtAgendamento.toString(), nomeFuncionario)).subject(SUBJECT).build();

        schedulingPublisher.sendToQueue(objectMapper.writeValueAsString(message));
        log.info("Message to queue {}", message);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userDto = userInterface.getLogin(UserLoginDto.builder().userName(username).build()).getBody();

        return new User(userDto.getUserName(),
                userDto.getPassword(), Collections.emptyList());
    }
}
