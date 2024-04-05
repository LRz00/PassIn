/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.rocketseat.passIn.repositories;

import com.rocketseat.passIn.domain.checkin.CheckIn;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author lara
 */
public interface CheckInRepository extends JpaRepository<CheckIn, Integer>{
    Optional<CheckIn> findByAttendeeId(String attendeeID);
}
