package com.modris;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;



@SpringBootTest
public class TestContainersBase {

	
	//MySQL image with base.sql script: modrisl/testcontainers_manager1.0
	// Check DockerTestContainers folder for more info.
	@Container
	@ServiceConnection
	public static MySQLContainer<?> mysql = new MySQLContainer<>(
		    DockerImageName.parse("modrisl/testcontainers_manager1.1")
		        .asCompatibleSubstituteFor("mysql"))
				.withDatabaseName("testdb")
				.withUsername("root")
				.withPassword("root");
		    
	static {
		mysql.start();
	}

	 
	@Test
	@DisplayName("Successfully launched TestContainersBase.")
	void beforeAll() {

	}
	
}
