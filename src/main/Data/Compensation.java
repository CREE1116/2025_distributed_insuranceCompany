package main.Data;
import java.util.Objects;
import main.Enum.ProcessState;

public class Compensation {

	/**
	 * Class 다이어그램과 다른점 몇가지
	 * 1. resultOFPaid와 PaidState를 합침
	 */


	// Example Fields
	private final int compensationID;
	private final int evaluationID; // Link to an Evaluation
	private final int customerID;   // Link to a Customer
	private ProcessState processOfCompensation;
	private int amountOfPaid;
	// Builder Pattern

	private Compensation(Builder builder) {
		this.compensationID = builder.compensationID;
		this.evaluationID = builder.evaluationID;
		this.customerID = builder.customerID;
		this.amountOfPaid = builder.claimsPaid;
		this.processOfCompensation = ProcessState.Awaiting;
	}

	// Getters
	public int getCompensationID() {
		return compensationID;
	}

	public int getEvaluationID() {
		return evaluationID;
	}

	public int getCustomerID() {
		return customerID;
	}

	public ProcessState getState() {
		return processOfCompensation;
	}

	public int getAmountOfPaid() {
		return amountOfPaid;
	}
	public ProcessState getProcessOfCompensation(){
		return processOfCompensation;
	}

	// setter?
	public void receiptCompensation(boolean isReceipt){
		if(isReceipt) {
			this.processOfCompensation = ProcessState.Completed;
		}
		else {
			this.processOfCompensation = ProcessState.Rejected;
		}
	}

	public void setAmountOfPaid(int paid){
		this.amountOfPaid = paid;
  }

	public static class Builder {
		private final int evaluationID;
		private final int customerID;
		private final int compensationID;
		private ProcessState paidState;
		private int claimsPaid;


		public Builder(int compensationID, int evaluationID, int customerID) {
			this.evaluationID = evaluationID;
			this.customerID = customerID;
			this.compensationID = compensationID;
			this.paidState = ProcessState.Awaiting;
			this.claimsPaid = 0;

		}

		public Builder paidState(ProcessState paidState) {
			this.paidState = paidState;
			return this;
		}

		public Builder claimsPaid(int claimsPaid) {
			this.claimsPaid = claimsPaid;
			return this;
		}


		public Compensation build() {
			return new Compensation(this);
		}
	}


	@Override
	public String toString() {
		return "Compensation{" +
				"compensationID='" + compensationID + '\'' +
				", evaluationID='" + evaluationID + '\'' +
				", customerID='" + customerID + '\'' +
				", paidState=" + processOfCompensation +
				", claimsPaid=" + amountOfPaid +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Compensation that = (Compensation) o;
		return amountOfPaid == that.amountOfPaid && Objects.equals(compensationID, that.compensationID) && Objects.equals(evaluationID, that.evaluationID) && Objects.equals(customerID, that.customerID) && processOfCompensation
				== that.processOfCompensation;
	}

}