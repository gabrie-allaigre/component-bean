package com.talanlabs.component.field;

public interface IField {

	/**
	 * Get name of field
	 * 
	 * @return
	 */
    String name();

	/**
	 * Not use toString for get a name, use name()
	 * 
	 * @return
	 */
	@Override
    String toString();

}
