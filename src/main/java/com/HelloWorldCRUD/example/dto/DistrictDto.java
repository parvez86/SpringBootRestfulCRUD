package com.HelloWorldCRUD.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictDto {
    @NotNull
    @Size(min = 1, max = 20, message = " district name should be not null and less than or equal 20 characters")
    private String name;
    @NotNull(message = "district code must be between 1 to 64")
    @Min(1)
    @Max(64)
    private int code;
    @NotNull
    @Size(min=1, max = 20, message = "division must be between 1 to 20 characters")
    private String division;
}
