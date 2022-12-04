package com.example.dashborad_pipe.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CoordinateGantt {
private String id;
    private String x;
    private List<Date> y = new ArrayList<Date>();
    private List<String> pedecessors = new ArrayList<String>();

}
