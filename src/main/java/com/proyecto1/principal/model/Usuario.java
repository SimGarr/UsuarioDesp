package com.proyecto1.principal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//data me crea getter y setter con lombok sin tener que hacerlo a mano
// y allargconstructor me crea el constructor con parametros y el no argsconstructor sin parametros
public class Usuario {
    private int idUsuario;
    private String nombre;
    private String apellidos;
    private String correo;
    private String contrasena;


    

}
