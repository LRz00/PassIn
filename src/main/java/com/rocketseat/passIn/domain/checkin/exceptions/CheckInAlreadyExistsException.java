/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rocketseat.passIn.domain.checkin.exceptions;

/**
 *
 * @author lara
 */
public class CheckInAlreadyExistsException extends RuntimeException {
    public CheckInAlreadyExistsException(String message){
        super(message);
    }
}
