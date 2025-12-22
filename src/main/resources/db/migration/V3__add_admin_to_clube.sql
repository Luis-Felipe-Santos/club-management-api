ALTER TABLE clubes
ADD COLUMN usuario_admin_id BIGINT NOT NULL;

ALTER TABLE clubes
ADD CONSTRAINT fk_clube_admin
FOREIGN KEY (usuario_admin_id)
REFERENCES usuarios (id);