/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rocketseat.passIn.domain.checkin;

import com.rocketseat.passIn.domain.attendee.Attendee;
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
@Table(name = "check_ins")
public class CheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @OneToOne
    @JoinColumn(name = "attendee_id", nullable = false)
    private Attendee attendee;
}
