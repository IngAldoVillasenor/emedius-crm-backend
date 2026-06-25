-- V4__create_instrument_logs_table.sql

CREATE TABLE instrument_logs (
                                 id UUID PRIMARY KEY,
                                 instrument_id UUID NOT NULL,
                                 service_order_id VARCHAR(255),
                                 author VARCHAR(255) NOT NULL,
                                 note TEXT NOT NULL,
                                 type VARCHAR(255),
                                 entry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    -- Llave foránea para vincular la bitácora con el instrumento
                                 CONSTRAINT fk_instrument_logs_instrument
                                     FOREIGN KEY (instrument_id)
                                         REFERENCES instruments (id)
                                         ON DELETE CASCADE
);

-- Opcional pero recomendado: Crear un índice para búsquedas rápidas
-- ya que las bitácoras crecerán mucho y casi siempre buscaremos por instrumento
CREATE INDEX idx_instrument_logs_instrument_id ON instrument_logs(instrument_id);