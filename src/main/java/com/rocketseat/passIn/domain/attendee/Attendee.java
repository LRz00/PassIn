/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rocketseat.passIn.domain.attendee;

import com.rocketseat.passIn.domain.event.Event;
import jakarta.persistence.*;
import java.time.LocalDateTime;
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
@Table(name="attendees")
public class Attendee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String email;
    
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
