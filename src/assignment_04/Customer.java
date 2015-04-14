/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment_04;

/**
 *
 * @author Barry Speller
 */
public class Customer {
    
    private int arrivalTime;
    
    /**
     * Constructs a customer setting the time (integer) of arrival
     * @param arrivalTime 
     */
    Customer(int arrivalTime) {
        super();
        this.arrivalTime = arrivalTime;
    }

    /**
     * Returns the customer's arrival time which was set at the time of
     * instantiation.
     * @return 
     */
    public int getArrivalTime() {
        return arrivalTime;
    }
    
}