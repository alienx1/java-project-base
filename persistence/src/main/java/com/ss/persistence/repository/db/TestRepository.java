package com.ss.persistence.repository.db;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.ss.domain.entity.db.Test;

public interface  TestRepository extends  R2dbcRepository<Test, UUID> {
    
}
