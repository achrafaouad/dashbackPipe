package com.example.dashborad_pipe.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class Taskphase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private Date date_depart;
    private Date date_arrive;
    private String version;
    @ElementCollection
    private List<String> predecessors;
    @ManyToOne(cascade=CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinColumn(
            name="phase_id",
            referencedColumnName = "id"
    )
    private Phase phase;
}
