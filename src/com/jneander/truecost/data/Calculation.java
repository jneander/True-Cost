package com.jneander.truecost.data;

public class Calculation {
  private double balance, APR, payment;
  private double totalCost, totalInterest, monthlyInterestRate;
  private int duration;

  public Calculation( double balance, double APR, double payment ) {
    setBalance( balance );
    setAPR( APR );
    setPayment( payment );
  }

  public void setBalance( double balance ) {
    this.balance = balance;
  }

  public void setAPR( double APR ) {
    this.APR = APR;
    monthlyInterestRate = APR / 1200.0;
  }

  public void setPayment( double payment ) {
    this.payment = payment;
  }

  public double getBalance() {
    return this.balance;
  }

  public double getAPR() {
    return this.APR;
  }

  public double getPayment() {
    return this.payment;
  }

  public int getDuration() {
    return this.duration;
  }

  public boolean calculate() {
    boolean returnSuccess = !(Double.isNaN( balance ) || Double.isNaN( APR )
        || Double.isNaN( payment ) || paymentIsTooSmall());

    double monthlyInterestRate = APR / 1200.0;
    double balanceRemaining = balance;

    totalInterest = 0;
    totalCost = 0;
    duration = 0;

    if ( returnSuccess )
      while ( balanceRemaining > 0 ) {
        if ( balanceRemaining > payment ) {
          totalCost += payment;
          balanceRemaining -= payment;

          double currentInterest = balanceRemaining * monthlyInterestRate;

          totalInterest += currentInterest;
          balanceRemaining += currentInterest;
        } else {
          totalCost += balanceRemaining;
          balanceRemaining = 0.0;
        }

        duration++;
      }

    return returnSuccess;
  }

  public double getInterest() {
    return this.totalInterest;
  }

  public double getCost() {
    return this.totalCost;
  }

  public boolean paymentIsTooSmall() {
    return (payment <= (balance - payment) * monthlyInterestRate);
  }
}