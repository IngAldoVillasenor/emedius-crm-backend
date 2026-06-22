package com.emelius.crmbackend.config.tenant;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

    private final TenantIdentifierResolver tenantIdentifierResolver;
    private final SchemaConnectionProvider schemaConnectionProvider;

    // Spring inyecta automáticamente nuestros componentes aquí
    public HibernateConfig(TenantIdentifierResolver tenantIdentifierResolver,
                           SchemaConnectionProvider schemaConnectionProvider) {
        this.tenantIdentifierResolver = tenantIdentifierResolver;
        this.schemaConnectionProvider = schemaConnectionProvider;
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return properties -> {
            // Le pasamos las INSTANCIAS reales a Hibernate, evitando el error de instanciación del YAML
            properties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
            properties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, schemaConnectionProvider);
        };
    }
}