/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rocketseat.passIn.services;

import com.rocketseat.passIn.domain.attendee.Attendee;
import com.rocketseat.passIn.domain.attendee.exceptions.AttendeeAlreadyExistsException;
import com.rocketseat.passIn.domain.attendee.exceptions.AttendeeNotFoundException;
import com.rocketseat.passIn.domain.checkin.CheckIn;
import com.rocketseat.passIn.dto.attendee.AttendeeBadgeResponseDTO;
import com.rocketseat.passIn.dto.attendee.AttendeesListResponseDTO;
import com.rocketseat.passIn.repositories.AttendeeRepository;
import com.rocketseat.passIn.dto.attendee.AttendeeDetails;
import com.rocketseat.passIn.dto.attendee.AttendeeBadgeDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import com.rocketseat.passIn.repositories.CheckInRepository;

/**
 *
 * @author lara
 */
@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

    //returns a list with all attendees from an event
    public List<Attendee> getAllAttendeesFromEvent(String eventId) {
        List<Attendee> attendeeList = this.attendeeRepository.findByEventId(eventId);

        return attendeeList;
    }

    public AttendeesListResponseDTO getEventsAttendee(String eventId) {
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());
            LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailsList);
    }

    //registers a new attendee
    public Attendee registerAttendee(Attendee newAttendee) {
        this.attendeeRepository.save(newAttendee);
        return newAttendee;
    }

    //checks if an attendee is registered to an event
    public void verifyAttendeeSubscription(String email, String eventId) {

        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
        if (isAttendeeRegistered.isPresent()) {
            throw new AttendeeAlreadyExistsException("Attendee is already registereed");
        }

    }

    //returns the badge with the relevant informations
    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
        Attendee attendee = this.getAttendee(attendeeId);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();

        AttendeeBadgeDTO badgeDto = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());
        return new AttendeeBadgeResponseDTO(badgeDto);
    }

    public void checkInAttendee(String attendeeId) {
        Attendee attendee = this.getAttendee(attendeeId);
        this.checkInService.registerCheckIn(attendee);
    }

    public void unregisterAttendee(String email) {

        Attendee attendee = this.attendeeRepository.findByEmail(email).orElseThrow(() -> new AttendeeNotFoundException("Attendee not found"));
        attendeeRepository.delete(attendee);
    }

    private Attendee getAttendee(String attendeeId) {
        return this.attendeeRepository.findById(attendeeId).orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with id"));
    }
}
