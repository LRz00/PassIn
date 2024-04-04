/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.rocketseat.passIn.dto.attendee;

import java.time.LocalDateTime;

/**
 *
 * @author lara
 */
public record AttendeeDetails(String id, String name, String email, LocalDateTime createdAt, LocalDateTime checkedInAt) {

}
