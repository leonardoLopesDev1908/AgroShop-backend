package com.dailycodework.agroshop.controller.dto.pesquisa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class FreteDTO {
    public int id;
    public String name;
    public String price;
    public int delivery_time;
    public Company company;

    public static class Company {
        public int id;
        public String name;
        public String picture;
    }
}