package fr.certu.chouette.model.neptune.type;

import lombok.Getter;
import lombok.Setter;

public class Address {
	@Getter @Setter private String streetName;
	@Getter @Setter private String countryCode;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("streetName=").append(streetName).append(" contryCode=").append(countryCode);
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (countryCode == null) {
			if (other.countryCode != null)
				return false;
		} else if (!countryCode.equals(other.countryCode))
			return false;
		if (streetName == null) {
			if (other.streetName != null)
				return false;
		} else if (!streetName.equals(other.streetName))
			return false;
		return true;
	}
}
