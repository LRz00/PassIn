/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rocketseat.passIn.services;

import com.rocketseat.passIn.domain.attendee.Attendee;
import com.rocketseat.passIn.domain.event.Event;
import com.rocketseat.passIn.domain.event.exceptions.EventFullException;
import com.rocketseat.passIn.domain.event.exceptions.EventNotFoundException;
import com.rocketseat.passIn.dto.attendee.AttendeeIdDTO;
import com.rocketseat.passIn.dto.attendee.AttendeeRequestDTO;
import com.rocketseat.passIn.dto.event.EventIdDTO;
import com.rocketseat.passIn.dto.event.EventRequestDTO;
import com.rocketseat.passIn.dto.event.EventResponseDTO;
import com.rocketseat.passIn.repositories.EventRepository;
import java.text.Normalizer;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

/**
 *
 * @author lara
 */

@Service
@RequiredArgsConstructor
public class EventService {

    public final EventRepository eventRepoitory;
    public final AttendeeService attendeeService;

    //Finds the details of an event by its ID
    public EventResponseDTO getEventDetail(String eventId) {
        Event event = this.getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }

    //creates a new event using a data tranfers object to get from the user only the variables that 
    //are not generated by the system and generates the ones that are
    public EventIdDTO createEvent(EventRequestDTO eventDTO) {
        Event newEvent = new Event();
        newEvent.setTitle(eventDTO.title());
        newEvent.setDetails(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        newEvent.setSlug(this.createSlug(eventDTO.title()));
        
        this.eventRepoitory.save(newEvent);
        
        return new EventIdDTO(newEvent.getId());
    }
    
    
    //creates an event slug to be used when creating a new event
    
    private String createSlug(String text){
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        
        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("^[\\w\\s]", "").replaceAll("\\s+", "-").toLowerCase();
    }
    
    //registers attendee on a specific event, making sur ethe event isnt full and exists
    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO){
        this.attendeeService.verifyAttendeeSubscription(attendeeRequestDTO.email(),eventId);
        
        Event event = this.getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        
        if(event.getMaximumAttendees() <= attendeeList.size()) throw new EventFullException("Event is full");
            
        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeRequestDTO.name());
        newAttendee.setEmail(attendeeRequestDTO.email());        
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        
        this.attendeeService.registerAttendee(newAttendee);
        
        return new AttendeeIdDTO(newAttendee.getId());
    }
    
    
    private Event getEventById(String eventId){
         return this.eventRepoitory.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found"));         
    }
}
