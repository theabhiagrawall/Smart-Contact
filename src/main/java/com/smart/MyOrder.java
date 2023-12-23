package com.smart;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class MyOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long myOrderId;
	
	private String orderId;
	
	private String amount;
	private String receipt;
	private String status;
	
	@ManyToOne
	private User user;
	
		private String paymentId;

		public long getMyOrderId() {
			return myOrderId;
		}

		public void setMyOrderId(long myOrderId) {
			this.myOrderId = myOrderId;
		}

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getReceipt() {
			return receipt;
		}

		public void setReceipt(String receipt) {
			this.receipt = receipt;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public String getPaymentId() {
			return paymentId;
		}

		public void setPaymentId(String paymentId) {
			this.paymentId = paymentId;
		}

}
