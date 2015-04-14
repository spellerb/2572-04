/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment_04;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

/**
 *
 * @author Barry
 */
public class GroceryLineSimulation {

    private int arrivalInterval;
    private int serviceInterval;
    private int simulationInterval;

    private int nextCustomerArrivalTime;
    private int clock;

    private int maxNumberOfCustomersInQueue;
    private int longestCustomerWaitTime;
    private int customerCount;
    private String simulationLog;
    private int[] queueSizeLog;
    private int[] customerCountLog;

    private Customer currentlyServicedCustomer;
    private Queue<Customer> groceryLine;

    /**
     * Default constructor. Sets arrival interval of 1 - 4 minutes. Sets service
     * interval of 1 - 4 minutes. Sets simulation length of 720 minutes (12
     * hours).
     */
    public GroceryLineSimulation() {
        super();
        arrivalInterval = 4;
        serviceInterval = 4;
        simulationInterval = 720;
        queueSizeLog = new int[simulationInterval + 1];
        customerCountLog = new int[simulationInterval + 1];

        currentlyServicedCustomer = null;
        groceryLine = new LinkedList<>();
    }

    /**
     * Sets the upper boundaries of the random arrival and service intervals,
     * and sets the simulation length in minutes.
     *
     * @param arrivalInterval
     * @param serviceInterval
     * @param simulationInterval
     */
    public GroceryLineSimulation(int arrivalInterval, int serviceInterval, int simulationInterval) {
        super();
        this.arrivalInterval = arrivalInterval;
        this.serviceInterval = serviceInterval;
        this.simulationInterval = simulationInterval;
        this.queueSizeLog = new int[simulationInterval + 1];
        this.customerCountLog = new int[simulationInterval + 1];

        currentlyServicedCustomer = null;
        groceryLine = new LinkedList<>();
    }

    /**
     * Returns the maximum number of customers in the queue during the 
     * simulation
     * @return int
     */
    public int getMaxNumberOfCustomersInQueue() {
        return maxNumberOfCustomersInQueue;
    }

    /**
     * Returns the log of the most recent simulation
     *
     * @return
     */
    public String getSimulationLog() {
        return simulationLog;
    }

    /**
     * Returns the array containing the size of the grocery line over the course
     * of the simulation
     *
     * @return queueSizeLog[]
     */
    public int[] getQueueSizeLog() {
        return queueSizeLog;
    }

    /**
     * Returns the array containing the customer count over the course 
     * of the simulation
     * @return customerCountLog[]
     */
    public int[] getCustomerCountLog() {
        return customerCountLog;
    }

    /**
     * Sets the upper boundary of the random arrival interval inclusively in
     * minutes
     *
     * @param arrivalInterval
     */
    public void setArrivalInterval(int arrivalInterval) {
        this.arrivalInterval = arrivalInterval;
    }

    /**
     * Sets the upper boundary of the random service interval inclusively in
     * minutes
     *
     * @param serviceInterval
     */
    public void setServiceInterval(int serviceInterval) {
        this.serviceInterval = serviceInterval;
    }

    /**
     * Sets the simulation run interval in minutes
     *
     * @param simulationInterval
     */
    public void setSimulationInterval(int simulationInterval) {
        this.simulationInterval = simulationInterval;
    }

    /**
     * Runs the grocery line simulation
     */
    public void runSimulation() {

        int hours;
        int minutes;
        int customerServiceTime = -1;
        int oldClock = -1;
        Random rng = new Random();

        maxNumberOfCustomersInQueue = 0;
        longestCustomerWaitTime = 0;
        customerCount = 0;
        simulationLog = new String();
        clock = 0;

        groceryLine.clear();
        currentlyServicedCustomer = null;

        scheduleNextCustomerArrival();

        while (clock <= simulationInterval) {
            if (oldClock != clock) {
                hours = clock / 60;
                minutes = clock % 60;
                simulationLog += String.format("%02d:%02d—————\n", hours, minutes);
                oldClock = clock;

                if (clock == nextCustomerArrivalTime) {
                    //HANDLE CUSTOMER ARRIVAL
                    simulationLog += "   A new customer has arrived";
                    //If no customer being serviced
                    if (currentlyServicedCustomer == null) {
                        //Service the new customer now
                        currentlyServicedCustomer = new Customer(clock);
                        simulationLog += " and is receiving service with no wait\n";
                        customerServiceTime = clock + rng.nextInt(serviceInterval) + 1;
                    } else {
                        simulationLog += "\n";
                        //add newly arrived customer to the grocery line
                        groceryLine.offer(new Customer(clock));
                    }
                    scheduleNextCustomerArrival();
                    customerCount++;
                }

                if (clock == customerServiceTime) {
                    //Take the next customer in line
                    currentlyServicedCustomer = groceryLine.poll();
                    if (currentlyServicedCustomer != null) {
                        int customerWaitTime = clock - currentlyServicedCustomer.getArrivalTime();
                        simulationLog += String.format("   A new customer is receiving service after waiting %d minutes\n", customerWaitTime);
                        if (customerWaitTime > longestCustomerWaitTime) {
                            longestCustomerWaitTime = customerWaitTime;
                        }
                        customerServiceTime = clock + rng.nextInt(serviceInterval) + 1;
                    }
                }
                if (groceryLine.size() > maxNumberOfCustomersInQueue) {
                    maxNumberOfCustomersInQueue = groceryLine.size();
                }
                simulationLog += String.format("   Grocery line length: %d\n", groceryLine.size());
            }
            queueSizeLog[clock] = groceryLine.size();
            customerCountLog[clock] = customerCount;
            clock++;
        }

        simulationLog += String.format("\nThe maximum number of customers in the grocery line was %d\n", maxNumberOfCustomersInQueue);
        simulationLog += String.format("The longest wait time was %d\n", longestCustomerWaitTime);
        simulationLog += String.format("The total number of customers was %d\n", customerCount);
        simulationLog += String.format("The number of customers still in line is %d\n", groceryLine.size());
    }

    private void scheduleNextCustomerArrival() {
        nextCustomerArrivalTime = clock + (int) (Math.random() * arrivalInterval) + 1;
        if (nextCustomerArrivalTime < simulationInterval) {
            int nextArrivalHour = nextCustomerArrivalTime / 60;
            int nextArrivalMinutes = nextCustomerArrivalTime % 60;
        } else {
            //System.out.println("<Next customer arrives after store closes>");
        }
    }

}
