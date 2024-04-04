/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rocketseat.passIn.dto.attendee;

import java.util.List;
import lombok.Getter;

/**
 *
 * @author lara
 */

public record AttendeesListResponseDTO( List<AttendeeDetails> attendees) {
   
}
