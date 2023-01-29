package com.eldorado.ms.agendamento.controller;

import com.eldorado.ms.agendamento.dto.RegisterScheduleDto;
import com.eldorado.ms.agendamento.dto.SchedulingDto;
import com.eldorado.ms.agendamento.service.SchedulingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/scheduling")
@Slf4j
@RequiredArgsConstructor
public class SchedulingController {

    private final SchedulingService schedulingService;

    @PostMapping("/create")
    public ResponseEntity<SchedulingDto> createEmployeeSchedule(@RequestBody SchedulingDto dto) {
        return ResponseEntity.ok(schedulingService.createEmployeeSchedule(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchedulingDto> getEmployeeScheduleById(@PathVariable UUID id) {
        return ResponseEntity.ok(schedulingService.getEmployeeScheduleById(id));
    }

    @GetMapping
    public ResponseEntity<List<SchedulingDto>> getAllEmployeesSchedules() {
        return ResponseEntity.ok(schedulingService.getAllEmployeesSchedule());
    }

    @PutMapping("/update")
    public ResponseEntity<SchedulingDto> updateEmployeeSchedule(@RequestBody SchedulingDto dto) {
        return ResponseEntity.ok(schedulingService.updateEmployeeSchedule(dto));
    }

    @DeleteMapping("/{id}")
    public void deleteEmployeeSchedule(@PathVariable UUID id) {
        schedulingService.deleteEmployee(id);
    }

    @PostMapping
    public ResponseEntity<SchedulingDto> registerSchedule(@RequestBody RegisterScheduleDto dto) {
        return ResponseEntity.ok(schedulingService.schedule(dto));
    }
}
