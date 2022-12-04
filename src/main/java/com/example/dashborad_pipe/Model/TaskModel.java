package com.example.dashborad_pipe.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskModel {

    private Long id;
    private String name;
    private Date date_depart;
    private Date date_arrive;
    private String version;
    private Integer phase;
    private List<String> pred;
}
