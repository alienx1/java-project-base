CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE "test" (
  "id" UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  "name" VARCHAR(255) NOT NULL,
  "create_by" VARCHAR(300),
  "create_at" TIMESTAMP DEFAULT NOW(),
  "update_by" VARCHAR(300),
  "update_at" TIMESTAMP
);
