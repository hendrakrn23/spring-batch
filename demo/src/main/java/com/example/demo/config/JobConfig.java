package com.example.demo.config;

import com.example.demo.entity.Country;
import com.example.demo.entity.NewCountry;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.NewCountryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Configuration
@EnableBatchProcessing
@Slf4j
public class JobConfig {

    Logger log = Logger.getLogger(String.valueOf(JobConfig.class));
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    NewCountryRepository newCountryRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Bean(name = "firstBatchJob")
    public Job job(JobRepository jobRepository, @Qualifier("step1") Step step1) {
        return new JobBuilder("firstBatchJob", jobRepository).start(step1).build();
    }


    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<Country, NewCountry>chunk(20, transactionManager)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter(jdbcTemplate))
                .faultTolerant()
//                .retry(IllegalArgumentException.class)
//                .retryLimit(5)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public ItemReader<Country> itemReader() {
        List<Country> listCountry = countryRepository.findAll();

        if (listCountry.isEmpty()) {
            log.info("Data is empty.");
        }

        return new ListItemReader<>(listCountry);
    }


    @Bean
    public ItemProcessor<Country, NewCountry> itemProcessor() {
        return country -> {

            log.info("Process country data : " + atomicInteger.getAndAdd(1));;

            if (atomicInteger.get() == 200) {
                log.info("Process country error.");

//                throw new IllegalArgumentException();
            }

            System.out.println(country);

            NewCountry newData = new NewCountry();
            newData.setCode(country.getCode());
            newData.setName("New " + country.getName());
            return newData;
        };
    }

    @Bean
    public ItemWriter<NewCountry> itemWriter(JdbcTemplate jdbcTemplate) {
        return newCountries -> {
            String sql = "INSERT INTO new_country (CODE, NAME, CREATED_DATE, JOB_ID) VALUES (?, ?, ?, ?)";
            Long jobId = 0L; // Get the current job execution ID somehow;
            LocalDateTime timestamp = LocalDateTime.now();

            for (NewCountry country : newCountries) {
                jdbcTemplate.update(sql, country.getCode(), country.getName(), timestamp, jobId);
            }
        };
    }

}
