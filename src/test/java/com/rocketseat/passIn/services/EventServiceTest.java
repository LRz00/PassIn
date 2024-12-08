package com.rocketseat.passIn.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rocketseat.passIn.domain.attendee.Attendee;
import com.rocketseat.passIn.domain.event.Event;
import com.rocketseat.passIn.domain.event.exceptions.EventAlreadyExistsException;
import com.rocketseat.passIn.domain.event.exceptions.EventNotFoundException;
import com.rocketseat.passIn.dto.event.EventDetailDTO;
import com.rocketseat.passIn.dto.event.EventIdDTO;
import com.rocketseat.passIn.dto.event.EventRequestDTO;
import com.rocketseat.passIn.dto.event.EventResponseDTO;
import com.rocketseat.passIn.repositories.EventRepository;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private AttendeeService attendeeService;

    // Teste para validar o sucesso ao obter dados do detalho
    @Test
    void testVerifyEventDetail_success() {
        // Dados simulados
        String eventId = "123";
        Event event = new Event();
        event.setId(eventId);
        event.setTitle("Test Event");
        event.setDetails("Event Details");
        event.setSlug("test-event");
        event.setMaximumAttendees(100);

        List<Attendee> attendees = List.of(new Attendee(), new Attendee());

        // Configurando mocks
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(attendeeService.getAllAttendeesFromEvent(eventId)).thenReturn(attendees);

        // Chamando o método do serviço
        EventResponseDTO response = eventService.getEventDetail(eventId);

        // Validando o resultado
        EventDetailDTO eventDetail = response.getEvent();
        assertEquals(eventId, eventDetail.id());
        assertEquals("Test Event", eventDetail.title());
        assertEquals("Event Details", eventDetail.details());
        assertEquals("test-event", eventDetail.slug());
        assertEquals(100, eventDetail.maximumAttendees());
        assertEquals(2, eventDetail.attendeesAmmount());

        // Verificando interações com mocks
        verify(eventRepository).findById(eventId);
        verify(attendeeService).getAllAttendeesFromEvent(eventId);
    }

    // Teste para indicar falha ao obter dados do detalho
    @Test
    void testVerifyEventDetail_failedToGetEventDetails() {
        String eventId = "123";

        // Configurando mocks
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Validando o resultado
        assertThrows(EventNotFoundException.class, () -> eventService.getEventDetail(eventId));
        verify(eventRepository).findById(eventId);
    }

    // Teste para validar o sucesso ao criar novo evento
    @Test
    void testCreateEvent_success() {
        // Dados simulados
        EventRequestDTO requestDTO = new EventRequestDTO("Test Event", "Event Details", 50);
        Event savedEvent = new Event();
        savedEvent.setId("123");
        savedEvent.setTitle(requestDTO.title());
        savedEvent.setDetails(requestDTO.details());
        savedEvent.setMaximumAttendees(requestDTO.maximumAttendees());
        savedEvent.setSlug("test-event");

        // Configurando mocks
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> {
            Event event = invocation.getArgument(0);
            event.setId("123");
            return event;
        });

        // Chamando o método do serviço
        EventIdDTO result = eventService.createEvent(requestDTO);

        // Validando e verificando o resultado
        assertEquals("123", result.eventId());
        verify(eventRepository).save(any(Event.class));
    }

    // Teste para indicar se o evento com o mesmo título já existe
    @Test
    void testCreateEvent_eventAlreadyExists() {
        // Dados simulados
        String title = "Test Event";
        String details = "Event Details";
        Integer maximumAttendees = 100;
        EventRequestDTO eventDTO = new EventRequestDTO(title, details, maximumAttendees);

        // Simula que já existe um evento com o mesmo título ou slug
        Event existingEvent = new Event();
        existingEvent.setTitle(title);
        existingEvent.setSlug("test-event"); 

        // Configurando mocks
        when(eventRepository.findByTitle(title)).thenReturn(Optional.of(existingEvent));

        // Validando e verificando o resultado
        assertThrows(EventAlreadyExistsException.class, () -> {
            eventService.createEvent(eventDTO);
        });
        verify(eventRepository).findByTitle(title);
    }

    // Teste para validar a inscrição do partipante no evento
    @Test
    void testAttendeeIsResgisteredOnEvent() {
        // Dados simulados
        String eventId = "123";
        String email = "attendee@example.com";

        // Configurando mocks
        when(attendeeService.verifyAttendeeSubscription(email, eventId)).thenReturn(true);

        // Chamando o método do serviço
        boolean isRegistered = attendeeService.verifyAttendeeSubscription(email, eventId);

        // Validando e verificando o resultado
        assertTrue(isRegistered);
        verify(attendeeService).verifyAttendeeSubscription(email, eventId);
    }

    // Teste para indicar que o participante não está inscrito no evento
    @Test
    void testAttendeeIsNotRegisteredOnEvent() {
        // Dados simulados
        String eventId = "123";
        String email = "attendee@example.com";

        // Configurando mocks
        when(attendeeService.verifyAttendeeSubscription(email, eventId)).thenReturn(false);

        // Chamando o método do serviço
        boolean isRegistered = attendeeService.verifyAttendeeSubscription(email, eventId);

        // Validando e verificando o resultado
        assertFalse(isRegistered);
        verify(attendeeService).verifyAttendeeSubscription(email, eventId);
    }
}
