package com.jneander.truecost.data;

public class Repayment {
  private double balance, APR, payment;
  private double totalCost, totalInterest, monthlyInterestRate;
  private int duration;

  public Repayment( double balance, double APR, double payment ) {
    setBalance( balance );
    setAPR( APR );
    setPayment( payment );
  }

  public boolean setBalance( double balance ) {
    boolean returnSuccess = true;

    if ( isValidBalance() )
      this.balance = balance;
    else
      returnSuccess = false;

    return returnSuccess;
  }

  public boolean setAPR( double APR ) {
    boolean returnSuccess = true;

    if ( isValidAPR() ) {
      this.APR = APR;
      monthlyInterestRate = APR / 1200.0;
    } else
      returnSuccess = false;

    return returnSuccess;
  }

  public boolean setPayment( double payment ) {
    boolean returnSuccess = true;

    if ( isValidBalance() )
      this.payment = payment;
    else
      returnSuccess = false;

    return returnSuccess;
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

  public void calculate() {
    double balanceRemaining = balance;

    totalInterest = 0;
    totalCost = 0;
    duration = 0;

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
  }

  public boolean hasValidValues() {
    return isValidBalance() && isValidAPR() && isValidPayment();
  }

  private boolean isValidPayment() {
    return !(Double.isNaN( payment ) || (payment <= (balance - payment) * monthlyInterestRate));
  }

  private boolean isValidAPR() {
    return !Double.isNaN( APR );
  }

  private boolean isValidBalance() {
    return !Double.isNaN( balance );
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