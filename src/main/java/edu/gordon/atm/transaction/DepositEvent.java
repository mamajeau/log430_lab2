/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gordon.atm.transaction;

import java.util.EventObject;

/**
 *
 * @author maaj
 */
public class DepositEvent extends EventObject{
    
    public DepositEvent(Object source) {
        super(source);
    }
}
