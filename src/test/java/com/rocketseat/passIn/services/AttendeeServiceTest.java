/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.rocketseat.passIn.services;

import com.rocketseat.passIn.domain.attendee.Attendee;
import com.rocketseat.passIn.domain.attendee.exceptions.AttendeeAlreadyExistsException;
import com.rocketseat.passIn.domain.attendee.exceptions.AttendeeNotFoundException;
import com.rocketseat.passIn.domain.checkin.CheckIn;
import com.rocketseat.passIn.domain.event.Event;
import com.rocketseat.passIn.dto.attendee.AttendeeBadgeResponseDTO;
import com.rocketseat.passIn.dto.attendee.AttendeesListResponseDTO;
import com.rocketseat.passIn.repositories.AttendeeRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.util.UriComponentsBuilder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;

/**
 *
 * @author lara
 */
@ExtendWith(MockitoExtension.class)
public class AttendeeServiceTest {

    @InjectMocks
    private AttendeeService attendeeService;
    @Mock
    private AttendeeRepository attendeeRepository;
    @Mock
    private CheckInService checkInService;

    @Test
    public void testGetEventsAttendee_Success() {
        String eventId = "idValida";
        Attendee attendee = new Attendee("1", "WS", "WS@wmail.com", null, LocalDateTime.now());
        List<Attendee> mockAttendees = List.of(attendee);

        Mockito.when(attendeeRepository.findByEventId(eventId)).thenReturn(mockAttendees);
        Mockito.when(checkInService.getCheckIn("1")).thenReturn(Optional.of(new CheckIn(1, LocalDateTime.now(), attendee)));

        AttendeesListResponseDTO result = attendeeService.getEventsAttendee(eventId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.attendees().size());
        Assertions.assertEquals("WS", result.attendees().get(0).name());

        Mockito.verify(attendeeRepository, Mockito.times(1)).findByEventId(eventId);
        Mockito.verify(checkInService, Mockito.times(1)).getCheckIn("1");
    }

    @Test
    public void testGetAllAttendeesFromEvent_Sucess() {
        String eventId = "idValida";
        Attendee attendee = new Attendee("1", "André", "andre@email.com", null, LocalDateTime.now());

        List<Attendee> mockAttendees = List.of(attendee);
        Mockito.when(attendeeRepository.findByEventId(eventId)).thenReturn(mockAttendees);

        List<Attendee> result = attendeeService.getAllAttendeesFromEvent(eventId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("André", result.get(0).getName());
        Mockito.verify(attendeeRepository, Mockito.times(1)).findByEventId(eventId);

    }

    @Test
    public void testGetAllAttendeesFromEvent_EmptyList_EventDoesntExist() {
        String invalidEventId = "idInvalida";
        Mockito.when(attendeeRepository.findByEventId(invalidEventId)).thenReturn(Collections.emptyList());

        List<Attendee> result = attendeeService.getAllAttendeesFromEvent(invalidEventId);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        Mockito.verify(attendeeRepository, Mockito.times(1)).findByEventId(invalidEventId);
    }

    /**
     * Test of registerAttendee method, of class AttendeeService.
     */
    @Test
    public void testRegisterAttendee_Success() {
        Attendee attendee = new Attendee("1", "WS", "WS@wmail.com", null, LocalDateTime.now());

        Mockito.when(attendeeRepository.save(Mockito.any(Attendee.class))).thenReturn(attendee);

        Attendee result = attendeeService.registerAttendee(attendee);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(attendee.getId(), result.getId());
        Assertions.assertEquals(attendee.getName(), result.getName());

        Mockito.verify(attendeeRepository, Mockito.times(1)).save(attendee);
    }

    /**
     * Test of verifyAttendeeSubscription method, of class AttendeeService.
     */
    @Test
    public void testVerifyAttendeeSubscription_NotSubscribed() {
        String email = "ws@email.com";
        String eventId = "eventId";

        Mockito.when(attendeeRepository.findByEventIdAndEmail(eventId, email))
                .thenReturn(Optional.empty());

        Assertions.assertDoesNotThrow(() -> attendeeService.verifyAttendeeSubscription(email, eventId));

        Mockito.verify(attendeeRepository, Mockito.times(1)).findByEventIdAndEmail(eventId, email);
    }

    @Test
    public void testVerifyAttendeeSubscription_IsSubscribed() {
        String email = "ws@email.com";
        String eventId = "eventId";

        Attendee attendee = new Attendee("1", "WS", email, null, LocalDateTime.now());

        Mockito.when(attendeeRepository.findByEventIdAndEmail(eventId, email))
                .thenReturn(Optional.of(attendee));

        // Act & Assert
        AttendeeAlreadyExistsException exception = Assertions.assertThrows(
                AttendeeAlreadyExistsException.class,
                () -> attendeeService.verifyAttendeeSubscription(email, eventId)
        );

        Assertions.assertEquals("Attendee is already registereed", exception.getMessage());
        Mockito.verify(attendeeRepository, Mockito.times(1)).findByEventIdAndEmail(eventId, email);
    }

    /**
     * Test of getAttendeeBadge method, of class AttendeeService.
     */
    @Test
    public void testGetAttendeeBadge_Sucess() {
        String attendeeId = "1";
        String eventId = "eventId";
        String attendeeName = "WS";
        String attendeeEmail = "ws@email.com";
        String expectedUri = "/attendees/" + attendeeId + "/check-in";

        Event event = new Event();
        event.setId(eventId);

        Attendee attendee = new Attendee(attendeeId, attendeeName, attendeeEmail, event, LocalDateTime.now());

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        Mockito.when(attendeeRepository.findById(attendeeId)).thenReturn(Optional.of(attendee));

        AttendeeBadgeResponseDTO responseDTO = attendeeService.getAttendeeBadge(attendeeId, uriBuilder);

        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(attendeeName, responseDTO.badge().name());
        Assertions.assertEquals(attendeeEmail, responseDTO.badge().email());
        Assertions.assertEquals(expectedUri, responseDTO.badge().checkInUrl());
        Assertions.assertEquals(eventId, responseDTO.badge().eventId());
    }

    @Test
    public void testGetAttendeeBadge_AttendeeDoesNotExist() {

        String attendeeId = "idInvalida";

        Mockito.when(attendeeRepository.findById(attendeeId)).thenThrow(new AttendeeNotFoundException("Attendee not found with id"));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        AttendeeNotFoundException exception = Assertions.assertThrows(
                AttendeeNotFoundException.class,
                () -> attendeeService.getAttendeeBadge(attendeeId, uriBuilder)
        );

        Assertions.assertEquals("Attendee not found with id", exception.getMessage());
    }

    /**
     * Test of checkInAttendee method, of class AttendeeService.
     */
    @Test
    public void testCheckInAttendee_Success() {
        String attendeeId = "1";
        String attendeeName = "WS";
        String attendeeEmail = "ws@email.com";
        Event event = new Event();
        event.setId("eventId");

        Attendee attendee = new Attendee(attendeeId, attendeeName, attendeeEmail, event, LocalDateTime.now());

        Mockito.when(attendeeRepository.findById(attendeeId)).thenReturn(Optional.of(attendee));

        attendeeService.checkInAttendee(attendeeId);

        Mockito.verify(attendeeRepository, Mockito.times(1)).findById(attendeeId);

        Mockito.verify(checkInService, Mockito.times(1)).registerCheckIn(attendee);
    }

    @Test
    public void testCheckInAttendee_NotFound() {
        String attendeeId = "nonExistentId";

        Mockito.when(attendeeRepository.findById(attendeeId)).thenReturn(Optional.empty());

        Assertions.assertThrows(AttendeeNotFoundException.class, () -> {
            attendeeService.checkInAttendee(attendeeId);
        });

        Mockito.verify(attendeeRepository, Mockito.times(1)).findById(attendeeId);
    }

    /**
     * Test of unregisterAttendee method, of class AttendeeService.
     */
    @Test
    public void testUnregisterAttendee_Sucess() {
        String email = "ws@email.com";
        String attendeeId = "1";
        String attendeeName = "WS";
        Event event = new Event();
        event.setId("eventId");

        Attendee attendee = new Attendee(attendeeId, attendeeName, email, event, LocalDateTime.now());

        Mockito.when(attendeeRepository.findByEmail(email)).thenReturn(Optional.of(attendee));

        attendeeService.unregisterAttendee(email);

        Mockito.verify(attendeeRepository, Mockito.times(1)).findByEmail(email);

        Mockito.verify(attendeeRepository, Mockito.times(1)).delete(attendee);
    }

    @Test
    public void testUnregisterAttendee_AttendeeNotFound() {
        String email = "naotem@email.com";
        Mockito.when(attendeeRepository.findByEmail(email)).thenReturn(Optional.empty());

        Assertions.assertThrows(AttendeeNotFoundException.class, () -> {
            attendeeService.unregisterAttendee(email);
        });

        Mockito.verify(attendeeRepository, Mockito.times(1)).findByEmail(email);
    }

}
