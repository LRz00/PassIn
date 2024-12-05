/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rocketseat.passIn.services;

import com.rocketseat.passIn.domain.attendee.Attendee;
import com.rocketseat.passIn.domain.checkin.CheckIn;
import com.rocketseat.passIn.domain.checkin.exceptions.CheckInAlreadyExistsException;
import com.rocketseat.passIn.domain.event.exceptions.EventNotFoundException;
import com.rocketseat.passIn.repositories.CheckInRepository;
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
public class CheckInService {
    private final CheckInRepository checkinRepository;
    
    //registers the check in of a new attendee
    public void registerCheckIn(Attendee attendee){
        this.verifyCheckInExists(attendee.getId());
        
        CheckIn newCheckIn = new CheckIn();
        newCheckIn.setAttendee(attendee);
        newCheckIn.setCreatedAt(LocalDateTime.now());
        this.checkinRepository.save(newCheckIn);
    }
    
    
    //checks if theres already an attende with that id checked in
    private void verifyCheckInExists(String attendeeId){
        Optional<CheckIn> isCheckedIn = this.getCheckIn(attendeeId);
        if(isCheckedIn.isPresent()) throw new CheckInAlreadyExistsException("Attendee already checked in");
    }
    
    public Optional<CheckIn> getCheckIn(String attendeeId){
        return this.checkinRepository.findByAttendeeId(attendeeId);
    }
    
    public List<CheckIn> getCheckInsByEvent(String eventId) {
        return checkinRepository.findByAttendeeEventId(eventId);
    }
}
