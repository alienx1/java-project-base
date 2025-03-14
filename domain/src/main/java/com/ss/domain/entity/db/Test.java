package com.ss.domain.entity.db;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("test")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Test implements Serializable{

    private static final long serialVersionUID = -4426646934L;
    
    @Id
    private UUID id;

    private String name;
}
