/**
 * Author: Tania López Martín
 * Date: 25/01/2019
 * Version: 1.0
 *
 */

package com.skipha.ssdstoreapp;

import java.io.Serializable;

/**
 * Class Items
 */
public class Items implements Serializable{
    /**
     * Attributes
     */
    private String name;
    private int price;
    private int number;

    /**
     * Getter
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Setter
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter
     * @return
     */
    public int getPrice() {
        return price;
    }

    /**
     * Setter
     * @param price
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Getter
     * @return
     */
    public int getNumber() {
        return number;
    }

    /**
     * Setter
     * @param number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Constructor
     * @param name
     * @param price
     * @param number
     */
    public Items(String name, int price, int number) {
        this.name = name;
        this.price = price;
        this.number = number;
    }
}
