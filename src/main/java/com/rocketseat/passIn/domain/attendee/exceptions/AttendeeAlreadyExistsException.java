/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rocketseat.passIn.domain.attendee.exceptions;

/**
 *
 * @author lara
 */
public class AttendeeAlreadyExistsException extends RuntimeException{
    public AttendeeAlreadyExistsException(String message){
        super(message);
    }
}
