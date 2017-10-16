package me.maartendev.seeders;

public class NumberSeeder {
    private int lastNumber = 0;

    public int getNext(){
        return ++lastNumber;
    }
}
