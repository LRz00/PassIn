/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  lara
 * Created: 3 de abr. de 2024
 */

CREATE TABLE attendees(
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    event_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT attendees_event_if_fkey FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

