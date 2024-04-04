/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rocketseat.passIn.services;

import com.rocketseat.passIn.domain.attendee.Attendee;
import com.rocketseat.passIn.domain.checkin.CheckIn;
import com.rocketseat.passIn.dto.attendee.AttendeesListResponseDTO;
import com.rocketseat.passIn.repositories.AttendeeRepository;
import com.rocketseat.passIn.repositories.CheckinRepository;
import com.rocketseat.passIn.dto.attendee.AttendeeDetails;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author lara
 */
@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckinRepository checkinRepository;
    
    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        List<Attendee> attendeeList = this.attendeeRepository.findByEventId(eventId);
        
        return attendeeList;
    }
    
 public AttendeesListResponseDTO getEventsAttendee(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkinRepository.findByAttendeeId(attendee.getId());
            LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailsList);
    }

     
}
