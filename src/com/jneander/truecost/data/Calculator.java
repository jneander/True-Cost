package com.jneander.truecost.data;

public class Calculator {
	// Declare class storage.
	private double accountBalance, APR, payment; // Repayment values
	private double price, interest, truecost; // Purchase values
	private int duration;
	private METHOD method;
	
	public enum METHOD {
		CASH,
		CREDIT
	};
	
	public double getTrueCost() {
		return this.truecost;
	}
	
	public double getInterest() {
		return this.interest;
	}
	
	public int getDuration() {
		return this.duration;
	}

	public Calculator( double balance, double APR, double payment, double price, METHOD method ) {
		this.accountBalance = balance;
		this.APR = APR;
		this.payment = payment;
		this.price = price;
		this.method = method;
	}
	
	private class Repayment {
		// Declare class storage.
		public double balance;
		public double totalCost, totalInterest;
		public int duration;

		/* Calculation Class Constructor */
		public Repayment( double balance ) {
			this.balance = balance;
		}

		/* Commit to the calculation */
		public boolean commit() {
			// Ensure that usable values are provided
			if ( Double.isNaN( balance ) || Double.isNaN( APR )
					|| Double.isNaN( payment ) ) {
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
				
				if ( balanceRemaining > balance ) {
					return false;
				}

				// Increment the duration.
				duration++;
			} // end while

			return true;
		} // end method commit
	}

	public boolean commit() {
		// Declare class storage.
		Repayment unmodified = new Repayment( accountBalance );
		Repayment withPayment = new Repayment( accountBalance - price );
		Repayment withPurchase = new Repayment( accountBalance + price );

		// Perform the 'unmodified account' calculation
		if ( !unmodified.commit() )
			return false;
		
		if ( method == METHOD.CASH ) { // method is 'cash'
			// Perform the 'with payment' calculation
			if ( !withPayment.commit() )
				return false;

			// Store the results of the calculation
			interest = unmodified.totalInterest - withPayment.totalInterest;
			truecost = unmodified.totalCost - withPayment.totalCost;
			duration = unmodified.duration - withPayment.duration;
		} else { // method is 'credit'
			// Perform the 'with purchase' calculation
			if ( !withPurchase.commit() )
				return false;
			
			// Store the results of the calculation
			interest = withPurchase.totalInterest - unmodified.totalInterest;
			truecost = withPurchase.totalCost - unmodified.totalCost;
			duration = withPurchase.duration - unmodified.duration;
		}
		
		return true;
	}
}