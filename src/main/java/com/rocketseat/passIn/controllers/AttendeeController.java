/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rocketseat.passIn.controllers;

import com.rocketseat.passIn.dto.attendee.AtendeeEmailDTO;
import com.rocketseat.passIn.dto.attendee.AttendeeBadgeResponseDTO;
import com.rocketseat.passIn.services.AttendeeService;
import com.rocketseat.passIn.services.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author lara
 */
@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
public class AttendeeController {
    private final AttendeeService attendeeService;
    
    //returns the atendee badge with their information
    @GetMapping("/{attendeeId}/badge")
    public ResponseEntity<AttendeeBadgeResponseDTO> getAttendeeBadge(@PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder){
       AttendeeBadgeResponseDTO response = this.attendeeService.getAttendeeBadge(attendeeId, uriComponentsBuilder);
       return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{attendeeId}/check-in")
    public ResponseEntity registerCheckIn(@PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        this.attendeeService.checkInAttendee(attendeeId);
        
        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(attendeeId).toUri();
        
        return ResponseEntity.created(uri).build();
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity unregisterAtendee(@RequestBody AtendeeEmailDTO dto){
        this.attendeeService.unregisterAttendee(dto.email());
        return ResponseEntity.noContent().build();
    }
    
    
}
