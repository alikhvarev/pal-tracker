package io.pivotal.pal.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@SpringBootApplication
public class PalTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PalTrackerApplication.class, args);
    }

//    @Bean
//    public TimeEntryRepository timeEntryRepository(DataSource dataSource) {
//        return new JdbcTimeEntryRepository(dataSource);
//    }
    @Bean
    public TimeEntryRepository timeEntryRepository(EntityManager entityManager) {
        return new JpaTimeEntryRepository(entityManager);
    }
}
