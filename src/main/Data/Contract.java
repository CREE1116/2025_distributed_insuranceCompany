package main.Data;

import java.time.LocalDate;
import java.util.*;
import main.Enum.ProcessState;

import java.time.LocalDate;
import java.util.Date; // java.util.Date를 사용하는 것으로 가정합니다.

public class Contract {

	private Date contractDate;
	private int contractID;
	private String customerID;
	private LocalDate expirationDate; // LocalDate를 사용하는 것으로 가정합니다.
	private String productID;
	private String salesID;
	private ProcessState state; // 아래에서 정의할 ENUM
	private InsuranceProduct insuranceProduct; // 아래에서 정의할 클래스 (또는 DB ID만 저장)

	// 모든 필드를 포함하는 생성자 (빌더에서만 사용)
	private Contract(Builder builder) {
		this.contractID = builder.contractID;
		this.contractDate = builder.contractDate;
		this.customerID = builder.customerID;
		this.expirationDate = builder.expirationDate;
		this.productID = builder.productID;
		this.salesID = builder.salesID;
		this.state = builder.state;
		this.insuranceProduct = builder.insuranceProduct;
	}

	// 모든 필드에 대한 Getter
	public Date getContractDate() {
		return contractDate;
	}

	public int getContractID() {
		return contractID;
	}

	public String getCustomerID() {
		return customerID;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public String getProductID() {
		return productID;
	}

	public String getSalesID() {
		return salesID;
	}

	public ProcessState getState() {
		return state;
	}

	/**
	 * @param isReceipt 심사 통과하려면 False, 거부하려면 False
	 */
	public Contract receiptEvaluation(boolean isReceipt){
		if(isReceipt) {
			this.state = ProcessState.Completed;
		}
		else {
			this.state = ProcessState.Rejected;
		}
		return this;
	}




	@Override
	public String toString() {
		return "Contract{" +
				"contractDate=" + contractDate +
				", contractID='" + contractID + '\'' +
				", customerID='" + customerID + '\'' +
				", expirationDate=" + expirationDate +
				", productID='" + productID + '\'' +
				", salesID='" + salesID + '\'' +
				", state=" + state +
				", insuranceProduct=" + insuranceProduct +
				'}';
	}

	// --- Builder Class ---
	public static class Builder {
		private Date contractDate;
		private int contractID; // Builder에서 초기 설정 가능 (예: DB에서 자동 생성되지 않는 경우)
		private String customerID;
		private LocalDate expirationDate;
		private String productID;
		private String salesID;
		private ProcessState state;
		private InsuranceProduct insuranceProduct;

		public Builder contractDate(Date contractDate) {
			this.contractDate = contractDate;
			return this;
		}

		public Builder contractID(int contractID) {
			this.contractID = contractID;
			return this;
		}

		public Builder customerID(String customerID) {
			this.customerID = customerID;
			return this;
		}

		public Builder expirationDate(LocalDate expirationDate) {
			this.expirationDate = expirationDate;
			return this;
		}

		public Builder productID(String productID) {
			this.productID = productID;
			return this;
		}

		public Builder salesID(String salesID) {
			this.salesID = salesID;
			return this;
		}

		public Builder state(ProcessState state) {
			this.state = state;
			return this;
		}

		public Builder insuranceProduct(InsuranceProduct insuranceProduct) {
			this.insuranceProduct = insuranceProduct;
			return this;
		}

		public Contract build() {
			return new Contract(this);
		}
	}
}

/**
 * CREATE TABLE contracts (
 *     contract_id VARCHAR(50) PRIMARY KEY, -- ContractID를 직접 PK로 사용
 *     contract_date DATE NOT NULL,
 *     customer_id VARCHAR(50) NOT NULL,
 *     expiration_date DATE NOT NULL,
 *     product_id VARCHAR(50) NOT NULL, -- InsuranceProduct의 productID와 매핑될 수 있음
 *     sales_id VARCHAR(50) NOT NULL,
 *     state VARCHAR(20) NOT NULL, -- ENUM 값 저장 (예: PENDING, APPROVED)
 *     insurance_product_id VARCHAR(50), -- InsuranceProduct 테이블의 PK를 외래 키로 참조
 *     FOREIGN KEY (insurance_product_id) REFERENCES insurance_products(product_id)
 * );
 *
 * CREATE TABLE insurance_products (
 *     product_id VARCHAR(50) PRIMARY KEY,
 *     product_name VARCHAR(255) NOT NULL,
 *     premium DECIMAL(10, 2) NOT NULL
 *     -- 기타 보험 상품 관련 필드
 * );
 */
