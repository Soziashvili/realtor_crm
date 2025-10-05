package ru.realtorcrm.core_crm.config;

import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetailFactoryBean ownerContactJobDetail() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(OwnerContactJob.class);
        factory.setDurability(true);
        return factory;
    }

    @Bean
    public CronTriggerFactoryBean ownerContactTrigger(JobDetail ownerContactJobDetail) {
        CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
        factory.setJobDetail(ownerContactJobDetail);
        factory.setCronExpression("0 0 9 * * ?");
        return factory;
    }

}
