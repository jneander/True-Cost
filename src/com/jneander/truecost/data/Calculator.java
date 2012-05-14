package com.jneander.truecost.data;

public class Calculator {
  private double accountBalance, apr, payment;
  private double price, interest, truecost;
  private int duration;
  private Method method;
  private boolean resultsAreCurrent;

  public enum Method {
    CASH,
    CREDIT
  };

  public void setAccountBalance( double accountBalance ) {
    if ( this.accountBalance != accountBalance ) {
      this.accountBalance = accountBalance;
      resultsAreCurrent = false;
    }
  }

  public void setApr( double apr ) {
    if ( this.apr != apr ) {
      this.apr = apr;
      resultsAreCurrent = false;
    }
  }

  public void setPayment( double payment ) {
    if ( this.payment != payment ) {
      this.payment = payment;
      resultsAreCurrent = false;
    }
  }

  public void setPrice( double price ) {
    if ( this.price != price ) {
      this.price = price;
      resultsAreCurrent = false;
    }
  }

  public void setMethod( Method method ) {
    if ( this.method != method ) {
      this.method = method;
      resultsAreCurrent = false;
    }
  }

  public double getTrueCost() {
    if ( !resultsAreCurrent )
      calculate();
    return this.truecost;
  }

  public double getInterest() {
    if ( !resultsAreCurrent )
      calculate();
    return this.interest;
  }

  public int getDuration() {
    if ( !resultsAreCurrent )
      calculate();
    return this.duration;
  }

  public void calculate() {
    Repayment unmodified = new Repayment( accountBalance, apr, payment );
    Repayment withPayment = new Repayment( accountBalance - price, apr, payment );
    Repayment withPurchase = new Repayment( accountBalance + price, apr, payment );

    unmodified.calculate();

    if ( method == Method.CASH ) {
      withPayment.calculate();
      storeRepaymentsDifference( unmodified, withPayment );
    } else {
      withPurchase.calculate();
      storeRepaymentsDifference( withPurchase, unmodified );
    }

    resultsAreCurrent = true;
  }

  private void storeRepaymentsDifference( Repayment unmodified, Repayment withPayment ) {
    interest = unmodified.getInterest() - withPayment.getInterest();
    truecost = unmodified.getCost() - withPayment.getCost();
    duration = unmodified.getDuration() - withPayment.getDuration();
  }

  public boolean paymentIsTooSmall() {
    return (new Repayment( accountBalance, apr, payment )).paymentIsTooSmall();
  }
}