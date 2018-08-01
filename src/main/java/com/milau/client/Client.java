/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milau.client;

/**
 *
 * @author john.kiragu
 */
public interface Client {
    
    void enqueue(String queue, String job);
    
//    Enqueue at a specifi time and see what happens here
    void enqueAt(String queue, String job);
    
//    This just means I can enqueue work as it comes here
    void recurringEnque(String queue, String job);
}
