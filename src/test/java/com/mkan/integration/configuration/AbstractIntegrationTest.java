package com.mkan.integration.configuration;

import com.mkan.GhApiAggregateApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(
        classes = {GhApiAggregateApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public abstract class AbstractIntegrationTest {
//    @Autowired
//    private PetRepository petRepository;
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    @AfterEach
//    public void after() {
//    }
}
