/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.ids_mannheim.lza;

/**
 *
 * @author Herbert Lange <lange@ids-mannheim.de>
 */
public class NoSuchPropertyException extends Exception {

    public NoSuchPropertyException(String repository_is_null) {
        super(repository_is_null);
    }
    
}
