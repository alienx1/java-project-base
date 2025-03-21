package com.ss.domain.entity.base;


import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.data.relational.core.mapping.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity implements Serializable {

    @Column("created_By")
    private String createdBy;

    @Column("created_date")
    private Timestamp createdDate;

    @Column("created_program")
    private String createdProgram;

    @Column("updated_By")
    private String updatedBy;


    @Column("updated_date")
    private Timestamp updatedDate;

    @Column("updated_program")
    private String updatedProgram;

}
