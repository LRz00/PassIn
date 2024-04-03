/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rocketseat.passIn.domain.event;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author lara
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {
    
    @Id @Column(nullable=false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable=false)
    private String title;
    
    @Column(nullable = false)
    private String details;
    
    @Column(nullable = false, unique = true)
    private String slug;
    
    @Column(nullable = false, name = "maximum_attendees")
    private Integer maximumAttendees;
}
