/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  lara
 * Created: 3 de abr. de 2024
 */

CREATE UNIQUE INDEX events_slug_key ON events(slug);

CREATE UNIQUE INDEX attendees_event_id_email_key ON attendees(event_id, email);

CREATE UNIQUE INDEX check_ins_attendee_id_key ON check_ins(attendee_id);
