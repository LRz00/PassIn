/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  lara
 * Created: 3 de abr. de 2024
 */

CREATE TABLE events(
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    details VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    maximum_attendees INTEGER NOT NULL
);

