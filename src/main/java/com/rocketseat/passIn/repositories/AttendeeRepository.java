/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.rocketseat.passIn.repositories;

import com.rocketseat.passIn.domain.attendee.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author lara
 */
public interface AttendeeRepository extends JpaRepository<Attendee, String> {
    
    List<Attendee> findByEventId(String eventId);
    Optional<Attendee> findByEventIdAndEmail(String eventId, String email);    
    Optional<Attendee> findByEmail(String email);    
}
