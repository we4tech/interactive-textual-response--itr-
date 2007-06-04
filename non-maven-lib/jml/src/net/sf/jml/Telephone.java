package net.sf.jml;

import java.util.regex.Pattern;

public final class Telephone {
	
	private static final Pattern TELEPHONE_PATTERN = Pattern
			.compile("tel:\\S+");

	private String telephoneNumber;

	private Telephone(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public static Telephone parseStr(String telephoneNumber) {
		if (TELEPHONE_PATTERN.matcher(telephoneNumber).matches())
			return new Telephone(telephoneNumber);
		return null;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Telephone)) {
			return false;
		}
		Telephone tel = (Telephone) obj;
		return telephoneNumber == null ? tel.telephoneNumber == null : telephoneNumber
				.equals(tel.telephoneNumber);
	}

	public int hashCode() {
		return telephoneNumber.hashCode();
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	
	public String toString(){
		return telephoneNumber;
	}


}
