-- V5__add_approval_fields.sql

ALTER TABLE service_orders
    ADD COLUMN approval_token VARCHAR(255) UNIQUE,
ADD COLUMN extra_cost DOUBLE PRECISION,
ADD COLUMN extra_work_reason TEXT,
ADD COLUMN terms_accepted_at TIMESTAMP WITHOUT TIME ZONE;

-- Creamos un índice para el token, ya que haremos búsquedas públicas rápidas con él
CREATE INDEX idx_service_orders_approval_token ON service_orders(approval_token);