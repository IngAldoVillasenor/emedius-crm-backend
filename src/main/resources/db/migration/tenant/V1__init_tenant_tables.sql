CREATE TABLE customers (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           name VARCHAR(255) NOT NULL,
                           whatsapp_number VARCHAR(20),
                           email VARCHAR(255),
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE instruments (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             customer_id UUID REFERENCES customers(id) ON DELETE CASCADE,
                             type VARCHAR(50) NOT NULL, -- Eléctrica, Acústica, Bajo, etc.
                             brand VARCHAR(100) NOT NULL,
                             model VARCHAR(100),
                             serial_number VARCHAR(100),
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE service_orders (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                instrument_id UUID REFERENCES instruments(id) ON DELETE CASCADE,
                                status VARCHAR(50) NOT NULL DEFAULT 'RECIBIDO', -- RECIBIDO, EN_PROCESO, LISTO, ENTREGADO
                                specific_requests TEXT,
                                received_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                estimated_delivery_date TIMESTAMP,
                                total_price NUMERIC(10, 2),
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE intake_conditions (
                                   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                   service_order_id UUID REFERENCES service_orders(id) ON DELETE CASCADE,
                                   string_gauge VARCHAR(30),             -- Ej: 10-46, 11-54
                                   action_1st_fret VARCHAR(30),          -- Altura en traste 1
                                   action_12th_fret VARCHAR(30),         -- Altura en traste 12
                                   paint_condition TEXT,                 -- Detalles de pintura/golpes
                                   fretboard_status TEXT,                -- Estado del diapasón
                                   hardware_status TEXT                  -- Estado de puente/electrónica
);