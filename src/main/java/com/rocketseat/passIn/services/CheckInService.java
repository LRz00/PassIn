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
 * @author wesley
 */
@Service
@RequiredArgsConstructor
public class CheckInService {
    private final CheckInRepository checkinRepository;
    
    //registra o check-in de um novo participante
    public void registerCheckIn(Attendee attendee){
        this.verifyCheckInExists(attendee.getId());
        
        CheckIn newCheckIn = new CheckIn();
        newCheckIn.setAttendee(attendee);
        newCheckIn.setCreatedAt(LocalDateTime.now());
        this.checkinRepository.save(newCheckIn);
    }
    
    
    //verifica se já há um participante com esse ID registrado
    private void verifyCheckInExists(String attendeeId){
        Optional<CheckIn> isCheckedIn = this.getCheckIn(attendeeId);
        if(isCheckedIn.isPresent()) throw new CheckInAlreadyExistsException("Attendee already checked in");
    }

    //pega o checkIn pelo id
    public Optional<CheckIn> getCheckIn(String attendeeId){
        return this.checkinRepository.findByAttendeeId(attendeeId);
    }

    //pega os checkIns vinculados a eventos
    public List<CheckIn> getCheckInsByEvent(String eventId) {
        return checkinRepository.findByAttendeeEventId(eventId);
    }
}
