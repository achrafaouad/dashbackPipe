package com.example.dashborad_pipe.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DataModel {
    private String name;
    private ArrayList<CoordinateGantt> data = new ArrayList<>();
}
