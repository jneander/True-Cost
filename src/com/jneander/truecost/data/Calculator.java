package com.jneander.truecost.data;

public class Calculator {
	private double accountBalance, APR, payment;
	private double price, interest, truecost;
	private int duration;
	private Method method;
	
	public enum Method {
		CASH,
		CREDIT
	};
	
	public Calculator( double balance, double APR, double payment, double price, Method method ) {
		this.accountBalance = balance;
		this.APR = APR;
		this.payment = payment;
		this.price = price;
		this.method = method;
	}
	
	public double getTrueCost() {
    return this.truecost;
  }
  
  public double getInterest() {
    return this.interest;
  }
  
  public int getDuration() {
    return this.duration;
  }

	public void calculate() {
		Repayment unmodified = new Repayment( accountBalance, APR, payment );
		Repayment withPayment = new Repayment( accountBalance - price, APR, payment );
		Repayment withPurchase = new Repayment( accountBalance + price, APR, payment );
		
		unmodified.calculate();
		
		if (method == Method.CASH) {
		  withPayment.calculate();
		  storeRepaymentsDifference( unmodified, withPayment );
		} else {
		  withPurchase.calculate();
		  storeRepaymentsDifference( withPurchase, unmodified );
		}
	}

  private void storeRepaymentsDifference( Repayment unmodified, Repayment withPayment ) {
    interest = unmodified.getInterest() - withPayment.getInterest();
    truecost = unmodified.getCost() - withPayment.getCost();
    duration = unmodified.getDuration() - withPayment.getDuration();
  }

  public boolean paymentIsTooSmall() {
    return (new Repayment(accountBalance, APR, payment)).paymentIsTooSmall();
  }
}