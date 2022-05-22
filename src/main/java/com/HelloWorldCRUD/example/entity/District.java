package com.HelloWorldCRUD.example.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Component
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "districts")
@Builder
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    @Length(max = 20)
    private String name;

    @Column(unique = true, nullable = false)
    @Min(1)
    @Max(64)
    private int dist_code;

    @Column(nullable = false)
    @Length(max = 20)
    private String division;
}
