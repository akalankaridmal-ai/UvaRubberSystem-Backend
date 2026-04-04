package com.uvarubber.service;

import java.time.LocalDate;

public class RubberService {

    /**
     * Calculates DRC percentage based on Metrolac reading.
     * Formula: 10 + (Reading * 0.2)
     */
    public double calculateDRC(int metrolacReading) {
        return 10 + (metrolacReading * 0.2);
    }

    /**
     * Calculates Dry KG for the farmer's bill.
     * Formula: (Liters * DRC) / 100
     */
    public double calculateDryWeight(double liters, int metrolacReading) {
        double drc = calculateDRC(metrolacReading);
        return (liters * drc) / 100;
    }

    /**
     * Determines which payment date this collection belongs to.
     * 1st - 15th -> Paid on the 25th of the current month.
     * 16th - 31st -> Paid on the 10th of the NEXT month.
     */
    public String getExpectedPaymentDate(LocalDate collectionDate) {
        int day = collectionDate.getDayOfMonth();

        if (day <= 15) {
            // Payment on the 25th of the same month
            return collectionDate.withDayOfMonth(25).toString();
        } else {
            // Payment on the 10th of the following month
            return collectionDate.plusMonths(1).withDayOfMonth(10).toString();
        }
    }
}