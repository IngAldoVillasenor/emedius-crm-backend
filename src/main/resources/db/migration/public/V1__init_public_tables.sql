CREATE TABLE IF NOT EXISTS tenants (
                                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    schema_name VARCHAR(50) UNIQUE NOT NULL,
    domain VARCHAR(100) UNIQUE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Insertar el taller inicial para pruebas locales
INSERT INTO tenants (name, schema_name, domain)
VALUES ('Emedius Guitar Workshop', 'emelius_gw', 'emelius.localhost')
    ON CONFLICT (schema_name) DO NOTHING;