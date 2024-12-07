/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rocketseat.passIn.controllers;

import com.rocketseat.passIn.domain.checkin.CheckIn;
import com.rocketseat.passIn.dto.attendee.AttendeeIdDTO;
import com.rocketseat.passIn.dto.attendee.AttendeeRequestDTO;
import com.rocketseat.passIn.dto.attendee.AttendeesListResponseDTO;
import com.rocketseat.passIn.dto.event.EventIdDTO;
import com.rocketseat.passIn.dto.event.EventRequestDTO;
import com.rocketseat.passIn.dto.event.EventResponseDTO;
import com.rocketseat.passIn.dto.event.EventUpdateCapacityDTO;
import com.rocketseat.passIn.services.AttendeeService;
import com.rocketseat.passIn.services.CheckInService;
import com.rocketseat.passIn.services.EventService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author André
 */

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    
    private final EventService eventService;
    private final AttendeeService attendeeService;
    private final CheckInService checkinService;
    
    @GetMapping("{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id){
        EventResponseDTO event = this.eventService.getEventDetail(id);
        
        return ResponseEntity.ok(event);
    }
    @GetMapping("{id}/all-checkins")
    public ResponseEntity<List<CheckIn>> getAllCheckIns(@PathVariable String id){
        return ResponseEntity.ok(this.checkinService.getCheckInsByEvent(id));
    }
    
    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        EventIdDTO eventIdDTO = this.eventService.createEvent(body);
        
        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri();
        return ResponseEntity.created(uri).body(eventIdDTO);
    }
    
    @PatchMapping("{id}")
    public ResponseEntity updateCapacity(@PathVariable String id, @RequestBody EventUpdateCapacityDTO dto){
        this.eventService.updateEventCapacity(dto, id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/attendees/{id}")
    public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable String id){
        AttendeesListResponseDTO attendeeListResponse = this.attendeeService.getEventsAttendee(id);
        
        return ResponseEntity.ok(attendeeListResponse);
    } 
    
    
     @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerParticipant(@PathVariable String eventId,@RequestBody AttendeeRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        AttendeeIdDTO attendeeIdDTO = this.eventService.registerAttendeeOnEvent(eventId, body);
        
        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(attendeeIdDTO.AttendeeId()).toUri();
        return ResponseEntity.created(uri).body(attendeeIdDTO);
    }
    
}
