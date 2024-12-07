/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.rocketseat.passIn.services;

import com.rocketseat.passIn.domain.attendee.Attendee;
import com.rocketseat.passIn.domain.checkin.CheckIn;
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
    public void testVerifyAttendeeSubscription() {
        System.out.println("verifyAttendeeSubscription");
        String email = "";
        String eventId = "";
        AttendeeService instance = null;
        instance.verifyAttendeeSubscription(email, eventId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAttendeeBadge method, of class AttendeeService.
     */
    @Test
    public void testGetAttendeeBadge() {
        System.out.println("getAttendeeBadge");
        String attendeeId = "";
        UriComponentsBuilder uriComponentsBuilder = null;
        AttendeeService instance = null;
        AttendeeBadgeResponseDTO expResult = null;
        AttendeeBadgeResponseDTO result = instance.getAttendeeBadge(attendeeId, uriComponentsBuilder);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkInAttendee method, of class AttendeeService.
     */
    @Test
    public void testCheckInAttendee() {
        System.out.println("checkInAttendee");
        String attendeeId = "";
        AttendeeService instance = null;
        instance.checkInAttendee(attendeeId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of unregisterAttendee method, of class AttendeeService.
     */
    @Test
    public void testUnregisterAttendee() {
        System.out.println("unregisterAttendee");
        String email = "";
        AttendeeService instance = null;
        instance.unregisterAttendee(email);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
