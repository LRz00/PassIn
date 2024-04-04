/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rocketseat.passIn.domain.event.exceptions;

/**
 *
 * @author lara
 */
public class EventNotFoundException extends RuntimeException {
    
    public EventNotFoundException(String message){
        super(message);
    }
}
