package com.emelius.crmbackend.config.tenant;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class FlywayConfig {

    private final DataSource dataSource;

    public FlywayConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public static BeanFactoryPostProcessor entityManagerFactoryDependsOnFlywayMigrations() {
        return beanFactory -> {
            if (beanFactory.containsBeanDefinition("entityManagerFactory")
                    && beanFactory.containsBeanDefinition("runFlywayMigrations")) {
                BeanDefinition entityManagerFactory = beanFactory.getBeanDefinition("entityManagerFactory");
                entityManagerFactory.setDependsOn("runFlywayMigrations");
            }
        };
    }

    @Bean
    public Boolean runFlywayMigrations() {
        Flyway flywayPublic = Flyway.configure()
                .dataSource(dataSource)
                .schemas("public")
                .locations("db/migration/public")
                .baselineOnMigrate(true)
                .load();
        flywayPublic.migrate();

        List<String> tenants = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT schema_name FROM public.tenants")) {

            while (resultSet.next()) {
                tenants.add(resultSet.getString("schema_name"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al leer los inquilinos para las migraciones de Flyway", e);
        }

        for (String tenantSchema : tenants) {
            Flyway flywayTenant = Flyway.configure()
                    .dataSource(dataSource)
                    .schemas(tenantSchema)
                    .locations("db/migration/tenant")
                    .baselineOnMigrate(true)
                    .load();
            flywayTenant.migrate();
        }

        return true;
    }
}