 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rocketseat.passIn.dto.event;

import com.rocketseat.passIn.domain.event.Event;
import lombok.Getter;
/**
 *
 * @author lara
 */

@Getter
public class EventResponseDTO {
    EventDetailDTO event;
    
    public EventResponseDTO(Event event, Integer numberOfAttendees){
        this.event = new EventDetailDTO(event.getId(), event.getTitle(), event.getDetails(), 
                event.getSlug(), event.getMaximumAttendees(), numberOfAttendees);
    }
    
 
}
