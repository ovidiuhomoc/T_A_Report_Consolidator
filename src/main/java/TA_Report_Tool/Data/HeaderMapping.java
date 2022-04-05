package TA_Report_Tool.Data;

import java.util.ArrayList;

import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.ExceptionsPack.MappingFieldDoesNotExist;

public class HeaderMapping {
	private ArrayList<HeaderMappingField> listOfMappings = new ArrayList<HeaderMappingField>();

	public HeaderMapping() {
		this.listOfMappings.add(new HeaderMappingField("Date1",
				new MaskTemplate().addDDay().addSep(".").addMMonth().addSep(".").addYYYYear(), mappingType.Date));
		this.listOfMappings.add(new HeaderMappingField("Time1",
				new MaskTemplate().addhhour().addSep(":").addmminute().addSep(":").addsecond().addSep(" ").markAMPMTime(),
				mappingType.Time));
		this.listOfMappings.add(new HeaderMappingField("Employee Unique ID", new MaskTemplate().addAnyString(),
				mappingType.EmployeeUniqueId));
		this.listOfMappings.add(new HeaderMappingField("Not Set", null,	mappingType.NotSet));
	}

	public int getCount() {
		return this.listOfMappings.size();
	}

	public boolean contains(mappingType type) {
		for (HeaderMappingField mappingField : this.listOfMappings) {
			if (mappingField.getType() == type) {
				return true;
			}
		}
		return false;
	}

	public HeaderMappingField getHeaderMappingFieldByName(String name) throws MappingFieldDoesNotExist {
		for (HeaderMappingField mappingField : this.listOfMappings) {
			if (mappingField.getName().equals(name)) {
				return mappingField;
			}
		}
		throw new ExceptionsPack.MappingFieldDoesNotExist("No mapping field with name " + name + "exists");
	}

	public void addMappingField(String name, MaskTemplate mask, mappingType employeefullname) {
		this.listOfMappings.add(new HeaderMappingField(name, mask, employeefullname));
	}

	public void addMappingField(HeaderMappingField mappingField) {
		this.listOfMappings.add(mappingField);
	}
	
	public HeaderMappingField getHeaderMappingFieldByType(mappingType type) throws MappingFieldDoesNotExist {
		for (HeaderMappingField mappingField : this.listOfMappings) {
			if (mappingField.getType()==type) {
				return mappingField;
			}
		}
		throw new ExceptionsPack.MappingFieldDoesNotExist("No mapping field with type " + type + "exists");
	}

}
