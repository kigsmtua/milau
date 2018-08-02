/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.kigsmtua.milau.client;

/**
 *
 * @author john.kiragu
 */
public interface Client {
    
    void enqueue(String queue, String job);
    
    void enqueAt(String queue, String job);
    
    void recurringEnque(String queue, String job);
}
