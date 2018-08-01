/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milau.cli;

import com.beust.jcommander.Parameter;
/**
 *
 * @author john.kiragu
 */
public class App {
//    This is how we need to look at the concurrency of the values that 
//    Come here and use the values that need to come here and use the va
    @Parameter(names={"--concurrency", "-c"})
    int concurrency;
    @Parameter(names={"--name", "-n"})
    String name;
    @Parameter(names={"--queue", "-n"})
    String queue;
    
    public static void main(String[] args) {
        System.out.println("Step up implementation of work....");
    }
    
}
