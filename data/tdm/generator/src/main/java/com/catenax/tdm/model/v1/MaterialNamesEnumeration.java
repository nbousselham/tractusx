/*
 *
 */
package com.catenax.tdm.model.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

// TODO: Auto-generated Javadoc
/**
 * Gets or Sets MaterialNamesEnumeration.
 */
public enum MaterialNamesEnumeration {

	/** The aluminium. */
	ALUMINIUM("Aluminium"),
	/** The polyamide. */
	POLYAMIDE("Polyamide"),
	/** The lithium. */
	LITHIUM("Lithium"),
	/** The iron. */
	IRON("Iron"),
	/** The Eisen. */
	EISEN("Eisen"),
	/** The Nickel. */
	NICKEL("Nickel"),
	/** The Cobalt. */
	COBALT("Cobalt"),
	/** The Mangan. */
	MANGAN("Mangan"),
	/** The Kleber. */
	KLEBER("Kleber"),
	/** The PA6. */
	PA6("PA6"),
	/** The Carbon. */
	CARBON("Carbon"),
	/** The Copper. */
	COPPER("Copper"),
	/** The Gummi. */
	GUMMI("Gummi"),
	/** The Lubricant. */
	LUBRICANT("Lubricant"),
	/** The others. */
	OTHERS("Others");

	/**
	 * From value.
	 *
	 * @param text the text
	 * @return the material names enumeration
	 */
	@JsonCreator
	public static MaterialNamesEnumeration fromValue(String text) {
		for (final MaterialNamesEnumeration b : MaterialNamesEnumeration.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}

	/** The value. */
	private String value;

	/**
	 * Instantiates a new material names enumeration.
	 *
	 * @param value the value
	 */
	MaterialNamesEnumeration(String value) {
		this.value = value;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}
}
