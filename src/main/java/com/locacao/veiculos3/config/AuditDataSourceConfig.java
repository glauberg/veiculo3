package com.locacao.veiculos3.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.locacao.veiculos3.repository.audit",
    entityManagerFactoryRef = "auditEntityManagerFactory",
    transactionManagerRef = "auditTransactionManager"
)
public class AuditDataSourceConfig {

    @Bean(name = "auditDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.audit")
    public DataSource auditDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "auditEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean auditEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("auditDataSource") DataSource dataSource) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        return builder
                .dataSource(dataSource)
                .packages("com.locacao.veiculos3.model.audit")
                .persistenceUnit("audit")
                .properties(properties)
                .build();
    }

    @Bean(name = "auditTransactionManager")
    public PlatformTransactionManager auditTransactionManager(
            @Qualifier("auditEntityManagerFactory") EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }
}