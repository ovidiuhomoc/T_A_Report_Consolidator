package TA_Report_Tool.Data;

import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;
import TA_Report_Tool.Tools.check;

public class MappingUnit {
	private String name;
	private MaskTemplate mask;
	private MappingType type;

	/**
	 * Constructor of the object that defines the way a string of a table can be
	 * parsed in certain types of data in java <b>
	 * 
	 * @param name This stores the name of the object. Different variations of
	 *             mapping details can be differentiated by this name.
	 * @param mask This object will be used to store the parsing details, character
	 *             by character
	 * @param type The general type of data that this mappingUnit will generate.
	 *             These are:<b>
	 *             <ul>
	 *             <li>Date</li>
	 *             <li>Time</li>
	 *             <li>DateAndTime</li>
	 *             <li>Number</li>
	 *             <li>FixedDigitNumber</li>
	 *             <li>SignalingDevice</li>
	 *             <li>EmployeeUniqueId</li>
	 *             <li>EmployeeFirstName</li>
	 *             <li>EmployeeMiddleName</li>
	 *             <li>EmployeeLastName</li>
	 *             <li>EmployeeFullName</li>
	 *             <li>Event</li>
	 *             <li>CustomFieldText</li>
	 *             <li>NotSet</li>
	 *             </ul>
	 * @throws nullArgument 
	 */
	public MappingUnit(String name, MaskTemplate mask, MappingType type) throws nullArgument {
		new check();

		if (check.isNull(name)) {
			throw new ExceptionsPack.nullArgument("The MappingUnit's name was null");
		}
		
		if (check.isNull(mask)) {
			throw new ExceptionsPack.nullArgument("The MappingUnit's mask was null");
		}
		
		if (check.isNull(type)) {
			throw new ExceptionsPack.nullArgument("The MappingUnit's type was null");
		}

		this.name = name;
		this.mask = mask;
		this.type = type;
	}

	public MappingType getType() {
		return this.type;
	}

	public String getName() {
		return this.name;
	}

	public MaskTemplate getMask() {
		return this.mask;
	}

	public boolean isMaskSet() {
		return this.mask.isSet();
	}

	public void setMask(MaskTemplate fullNameEmplMask) {
		this.mask = fullNameEmplMask;
	}

}
