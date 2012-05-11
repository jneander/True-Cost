package com.jneander.truecost.data;

public class Calculator {
	private double accountBalance, APR, payment;
	private double price, interest, truecost;
	private int duration;
	private METHOD method;
	
	public enum METHOD {
		CASH,
		CREDIT
	};
	
	public Calculator( double balance, double APR, double payment, double price, METHOD method ) {
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

	public boolean calculate() {
		Repayment unmodified = new Repayment( accountBalance, APR, payment );
		Repayment withPayment = new Repayment( accountBalance - price, APR, payment );
		Repayment withPurchase = new Repayment( accountBalance + price, APR, payment );
		
		boolean returnSuccess = true;

		if ( !unmodified.calculate() )
			returnSuccess = false;
		
		if ( method == METHOD.CASH ) {
			if ( !withPayment.calculate() )
				returnSuccess =  false;

			interest = unmodified.getInterest() - withPayment.getInterest();
			truecost = unmodified.getCost() - withPayment.getCost();
			duration = unmodified.getDuration() - withPayment.getDuration();
		} else {
			if ( !withPurchase.calculate() )
				returnSuccess =  false;
			
			interest = withPurchase.getInterest() - unmodified.getInterest();
			truecost = withPurchase.getCost() - unmodified.getCost();
			duration = withPurchase.getDuration() - unmodified.getDuration();
		}
		
		return returnSuccess;
	}
}