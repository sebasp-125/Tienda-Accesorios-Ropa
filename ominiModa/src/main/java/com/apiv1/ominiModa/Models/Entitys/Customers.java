package com.apiv1.ominiModa.Models.Entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "customers")
public class Customers {

    @Id 
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name = "completeName" , nullable= false , length= 100)
    private String Nombre;

    @Column(name = "correo" , nullable= false , length= 60)
    private String Correo;
}
