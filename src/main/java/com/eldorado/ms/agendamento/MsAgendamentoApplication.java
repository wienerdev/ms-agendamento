package com.eldorado.ms.agendamento;

import com.eldorado.commons.configuration.EnableMapper;
import com.eldorado.commons.interception.EnableAuthorization;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EnableMapper
@EnableFeignClients
public class MsAgendamentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAgendamentoApplication.class, args);
	}

}
