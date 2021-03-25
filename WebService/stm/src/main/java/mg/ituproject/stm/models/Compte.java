package mg.ituproject.stm.models;

import java.math.BigDecimal;

public class Compte extends Client{
	// Fields
	protected BigDecimal credit;
	protected BigDecimal mobileMoney;
	
	// Setters & Getters
	public BigDecimal getCredit() {
		return credit;
	}
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}
	public BigDecimal getMobileMoney() {
		return mobileMoney;
	}
	public void setMobileMoney(BigDecimal mobileMoney) {
		this.mobileMoney = mobileMoney;
	}
}
