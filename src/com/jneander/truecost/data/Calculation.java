package com.jneander.truecost.data;

public class Calculation {
	// Declare class storage.
	private double balance, APR, payment;
	private double totalCost, totalInterest;
	private int duration;

	/* Calculation Class Constructor */
	public Calculation( double balance, double APR, double payment ) {
		// Place constructor values into 'this' storage.
		setBalance( balance );
		setAPR( APR );
		setPayment( payment );
	}

	public void setBalance( double balance ) {
		this.balance = balance;
	}

	public void setAPR( double APR ) {
		this.APR = APR;
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

	/* Commit to the calculation */
	public boolean commit() {
		// Ensure that usable values are provided
		if ( Double.isNaN( balance ) || Double.isNaN( APR )
				|| Double.isNaN( payment ) || paymentIsTooSmall() ) {
			return false;
		}
		
		// TODO: Protect against 'impossible payoff' scenario.

		// Initialize local storage.
		double balanceRemaining = balance;
		totalInterest = 0;
		totalCost = 0;
		duration = 0;

		// Perform calculations until balance is reduced to target.
		while ( balanceRemaining > 0 ) {
			// If the balance is greater than the payment...
			if ( balanceRemaining > payment ) {
				// Apply the payment.
				totalCost += payment;
				balanceRemaining -= payment;

				// TODO: Verify that no other interest functions apply.
				
				// Apply the interest.
				totalInterest += balanceRemaining * (APR / 1200.0);
				balanceRemaining *= 1 + (APR / 1200.0);
			} else {
				// Apply one final payment.
				totalCost += balanceRemaining;
				balanceRemaining = 0.0; // balance is now zero
			}
			
			// Increment the duration.
			duration++;
		}

		return true;
	}

	public double getInterest() {
		return this.totalInterest;
	}
	
	public double getCost() {
		return this.totalCost;
	}
	
	public boolean paymentIsTooSmall() {
	  return (balance <= (balance - payment) * (APR / 1200.0));
	}
}