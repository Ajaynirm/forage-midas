package com.jpmc.midascore.foundation; // use the same package as Transaction or wherever suitable

public class Incentive {
    private double amount; // matches the JSON field

    // default constructor (required for Jackson)
    public Incentive() {}

    public Incentive(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Incentive{" +
                "amount=" + amount +
                '}';
    }
}
