package com.example.dashborad_pipe.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

@Table(name = "projet")
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 399)
    private String name;


/*
    TODO [JPA Buddy] create field to map the 'geom' column
     Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "geom", columnDefinition = "geometry")
    private Object geom;
*/
}