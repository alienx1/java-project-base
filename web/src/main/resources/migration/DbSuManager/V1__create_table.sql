CREATE TABLE "db_language" (
  "language_code" varchar(10) PRIMARY KEY,
  "active" boolean NOT NULL DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp
);

CREATE TABLE "db_language_lang" (
  "language_code" varchar(10),
  "language_code_ref" varchar(10),
  "language_name" varchar(300),
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("language_code", "language_code_ref")
);

CREATE TABLE "db_country" (
  "country_code" varchar(10) PRIMARY KEY,
  "active" boolean NOT NULL DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp
);

CREATE TABLE "db_country_lang" (
  "country_code" varchar(10),
  "language_code" varchar(10),
  "country_name" varchar(300),
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("country_code", "language_code")
);

CREATE TABLE "db_province" (
  "province_code" varchar(10) PRIMARY KEY,
  "country_code" varchar(10) NOT NULL,
  "active" boolean NOT NULL DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp
);

CREATE TABLE "db_province_lang" (
  "province_code" varchar(10),
  "language_code" varchar(10),
  "province_name" varchar(300),
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("province_code", "language_code")
);

CREATE TABLE "db_district" (
  "district_code" varchar(10) PRIMARY KEY,
  "province_code" varchar(10) NOT NULL,
  "active" boolean NOT NULL DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp
);

CREATE TABLE "db_district_lang" (
  "district_code" varchar(10),
  "language_code" varchar(10),
  "district_name" varchar(300),
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("district_code", "language_code")
);

CREATE TABLE "db_sub_district" (
  "sub_district_code" varchar(10) PRIMARY KEY,
  "district_code" varchar(10) NOT NULL,
  "active" boolean NOT NULL DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp
);

CREATE TABLE "db_sub_district_lang" (
  "sub_district_code" varchar(10),
  "language_code" varchar(10),
  "sub_district_name" varchar(300),
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("sub_district_code", "language_code")
);

CREATE TABLE "db_postal_code" (
  "postal_code" varchar(10) PRIMARY KEY,
  "country_code" varchar(10),
  "province_code" varchar(10),
  "district_code" varchar(10),
  "sub_district_code" varchar(10),
  "active" boolean NOT NULL DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp
);

CREATE TABLE "db_bank" (
  "bank_code" varchar(20) PRIMARY KEY,
  "transfer_bank_code" varchar,
  "active" boolean NOT NULL DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp
);

CREATE TABLE "db_bank_lang" (
  "bank_code" varchar(20),
  "language_code" varchar(10),
  "bank_name" varchar(300),
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("bank_code", "language_code")
);

CREATE TABLE "db_bank_branch" (
  "bank_code" varchar(20),
  "branch_code" varchar(20),
  "active" boolean,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("bank_code", "branch_code")
);

CREATE TABLE "db_bank_branch_lang" (
  "bank_code" varchar(20),
  "branch_code" varchar(20),
  "language_code" varchar(10),
  "bank_branch_name" varchar(300),
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("bank_code", "branch_code", "language_code")
);

CREATE TABLE "db_currency" (
  "currency_code" varchar(20) PRIMARY KEY,
  "active" boolean NOT NULL DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp
);

CREATE TABLE "db_currency_lang" (
  "currency_code" varchar(20),
  "language_code" varchar(10),
  "currency_name" varchar(300),
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("currency_code", "language_code")
);

CREATE TABLE "db_prefix" (
  "prefix_id" uuid PRIMARY KEY,
  "personality_type" varchar(20),
  "active" boolean NOT NULL DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp
);

CREATE TABLE "db_prefix_lang" (
  "prefix_id" uuid,
  "language_code" varchar(10),
  "personality_name" varchar(300),
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("prefix_id", "language_code")
);

CREATE TABLE "db_organization" (
  "organization_id" uuid PRIMARY KEY,
  "active" boolean NOT NULL DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp
);

CREATE TABLE "db_organization_lang" (
  "organization_id" uuid,
  "language_code" varchar(10),
  "organization_name" varchar(300),
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("organization_id", "language_code")
);

CREATE TABLE "db_organization_parameter" (
  "organization_id" uuid,
  "organization_parameter_group" varchar(100),
  "organization_parameter_code" varchar(100),
  "organization_parameter_value" varchar(1000),
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("organization_id", "organization_parameter_group", "organization_parameter_code")
);

CREATE TABLE "db_authorize" (
  "authorize_id" uuid PRIMARY KEY,
  "description" varchar(300),
  "active" boolean DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp
);

CREATE TABLE "su_company" (
  "company_id" uuid,
  "organization_id" uuid,
  "branch_code" varchar(10) NOT NULL DEFAULT '00000',
  "address_id" uuid NOT NULL,
  "email" varchar(100) UNIQUE NOT NULL,
  "phone_no" varchar(20) UNIQUE,
  "fax_no" varchar(20) UNIQUE,
  "prefix_id" uuid NOT NULL,
  "is_branch" boolean NOT NULL DEFAULT false,
  "active" boolean DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("company_id", "organization_id")
);

CREATE TABLE "su_company_parameter" (
  "company_id" uuid,
  "company_parameter_group" varchar(100),
  "company_parameter_code" varchar(100),
  "company_parameter_value" varchar(1000),
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("company_id", "company_parameter_group", "company_parameter_code")
);

CREATE TABLE "su_company_lang" (
  "company_id" uuid,
  "organization_id" uuid,
  "language_code" varchar(10),
  "company_name" varchar(300),
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("company_id", "organization_id", "language_code")
);

CREATE TABLE "su_address" (
  "address_id" uuid PRIMARY KEY,
  "address" varchar(1000),
  "country_code" varchar(10),
  "province_code" varchar(10) NOT NULL,
  "district_code" varchar(10) NOT NULL,
  "sub_district_code" varchar(10) NOT NULL,
  "postal_code" varchar(10) NOT NULL,
  "address_character" uuid NOT NULL,
  "active_default" boolean NOT NULL DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp
);

CREATE TABLE "su_user" (
  "user_id" uuid PRIMARY KEY,
  "user_name" varchar(50) UNIQUE NOT NULL,
  "password_hash" bytea NOT NULL,
  "email" varchar(100) UNIQUE NOT NULL,
  "default_lang" varchar(10) NOT NULL,
  "active" boolean NOT NULL DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp
);

CREATE TABLE "su_user_organization" (
  "user_id" uuid,
  "organization_id" uuid,
  "company_id" uuid,
  "active" boolean NOT NULL DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("user_id", "organization_id", "company_id")
);

CREATE TABLE "su_user_profile" (
  "user_id" uuid PRIMARY KEY,
  "profile_code" varchar(20) NOT NULL,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp
);

CREATE TABLE "su_profile" (
  "profile_code" varchar(20) PRIMARY KEY,
  "description" varchar(300),
  "active" boolean NOT NULL DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp
);

CREATE TABLE "su_authorize" (
  "profile_code" varchar(20),
  "menu_code" varchar(20),
  "system_code" varchar(20),
  "authorize_id" uuid NOT NULL,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("profile_code", "menu_code", "system_code")
);

CREATE TABLE "db_system" (
  "system_code" varchar(20) PRIMARY KEY,
  "description" varchar(300),
  "active" boolean NOT NULL DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp
);

CREATE TABLE "db_system_lang" (
  "system_code" varchar(20),
  "language_code" varchar(10),
  "system_name" varchar(300),
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("system_code", "language_code")
);

CREATE TABLE "db_program" (
  "program_code" varchar(20),
  "system_code" varchar(20),
  "module_code" varchar(20),
  "main_program" varchar(20),
  "program_path" varchar(200),
  "active" boolean NOT NULL DEFAULT true,
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("program_code", "system_code", "module_code")
);

CREATE TABLE "db_program_lang" (
  "program_code" varchar(20),
  "system_code" varchar(20),
  "module_code" varchar(20),
  "language_code" varchar(10),
  "program_name" varchar(300),
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("program_code", "system_code", "module_code", "language_code")
);

CREATE TABLE "db_message" (
  "message_code" varchar(20),
  "language_code" varchar(10),
  "program_code" varchar(20),
  "message" varchar(1000),
  "create_by" varchar(300),
  "create_at" timestamp,
  "update_by" varchar(300),
  "update_at" timestamp,
  PRIMARY KEY ("message_code", "language_code")
);
