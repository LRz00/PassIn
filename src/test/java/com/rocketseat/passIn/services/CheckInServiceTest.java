package com.rocketseat.passIn.services;

import com.rocketseat.passIn.domain.attendee.Attendee;
import com.rocketseat.passIn.domain.checkin.CheckIn;
import com.rocketseat.passIn.domain.checkin.exceptions.CheckInAlreadyExistsException;
import com.rocketseat.passIn.repositories.CheckInRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class CheckInServiceTest {

    @InjectMocks
    private CheckInService checkInService;

    @Mock
    private CheckInRepository checkInRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterCheckIn_Success() {
        Attendee attendee = new Attendee("participante1", "Wessou", "ws@example.com", null, LocalDateTime.now());

        // Mock para indicar que o participante ainda não fez check-in
        Mockito.when(checkInRepository.findByAttendeeId(attendee.getId())).thenReturn(Optional.empty());

        // Chama o método
        checkInService.registerCheckIn(attendee);

        // Verifica se o método save foi chamado com os dados corretos
        Mockito.verify(checkInRepository, Mockito.times(1)).save(Mockito.argThat(checkIn ->
                checkIn.getAttendee().equals(attendee) && checkIn.getCreatedAt() != null
        ));
    }

    @Test
    void testRegisterCheckIn_AttendeeAlreadyCheckedIn() {
        Attendee attendee = new Attendee("participante1", "Wessou", "ws@example.com", null, LocalDateTime.now());

        // Mock para indicar que o participante já fez check-in
        Mockito.when(checkInRepository.findByAttendeeId(attendee.getId())).thenReturn(Optional.of(new CheckIn()));

        // Verifica se a exceção é lançada
        Assertions.assertThrows(CheckInAlreadyExistsException.class, () -> {
            checkInService.registerCheckIn(attendee);
        });

        // Verifica que o método save não foi chamado
        Mockito.verify(checkInRepository, Mockito.never()).save(Mockito.any(CheckIn.class));
    }

    @Test
    void testGetCheckIn_Found() {
        String attendeeId = "participante1";
        CheckIn checkIn = new CheckIn();
        checkIn.setAttendee(new Attendee(attendeeId, "Wessou", "ws@example.com", null, LocalDateTime.now()));

        // Mock para retornar um check-in existente
        Mockito.when(checkInRepository.findByAttendeeId(attendeeId)).thenReturn(Optional.of(checkIn));

        // Chama o método
        Optional<CheckIn> result = checkInService.getCheckIn(attendeeId);

        // Verifica o resultado
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(checkIn, result.get());

        // Verifica que o repositório foi chamado
        Mockito.verify(checkInRepository, Mockito.times(1)).findByAttendeeId(attendeeId);
    }

    @Test
    void testGetCheckIn_NotFound() {
        String attendeeId = "participante2";

        // Mock para indicar que nenhum check-in foi encontrado
        Mockito.when(checkInRepository.findByAttendeeId(attendeeId)).thenReturn(Optional.empty());

        // Chama o método
        Optional<CheckIn> result = checkInService.getCheckIn(attendeeId);

        // Verifica o resultado
        Assertions.assertFalse(result.isPresent());

        // Verifica que o repositório foi chamado
        Mockito.verify(checkInRepository, Mockito.times(1)).findByAttendeeId(attendeeId);
    }

    @Test
    void testGetCheckInsByEvent_Success() {
        String eventId = "Sao Joao";
        CheckIn checkIn1 = new CheckIn();
        CheckIn checkIn2 = new CheckIn();

        // Mock para retornar uma lista de check-ins
        Mockito.when(checkInRepository.findByAttendeeEventId(eventId)).thenReturn(List.of(checkIn1, checkIn2));

        // Chama o método
        List<CheckIn> result = checkInService.getCheckInsByEvent(eventId);

        // Verifica o resultado
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(checkIn1));
        Assertions.assertTrue(result.contains(checkIn2));

        // Verifica que o repositório foi chamado
        Mockito.verify(checkInRepository, Mockito.times(1)).findByAttendeeEventId(eventId);
    }
}
