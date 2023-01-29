package com.eldorado.ms.agendamento.feign;

import com.eldorado.commons.dto.EmployeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "ms-funcionario")
public interface EmployeeInterface {

    @GetMapping(value = "/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable UUID id);
}
