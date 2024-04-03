/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.rocketseat.passIn.repositories;

import com.rocketseat.passIn.domain.checkin.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author lara
 */
public interface CheckinRepository extends JpaRepository<CheckIn, Integer>{
    
}
