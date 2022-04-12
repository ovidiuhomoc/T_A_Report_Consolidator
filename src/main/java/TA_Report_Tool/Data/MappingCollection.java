package TA_Report_Tool.Data;

import java.util.ArrayList;

import TA_Report_Tool.MainApp.ExceptionsPack;
import TA_Report_Tool.MainApp.ExceptionsPack.mappingUnitDoesNotExist;
import TA_Report_Tool.MainApp.ExceptionsPack.nullArgument;

public class MappingCollection {
	private ArrayList<MappingUnit> listOfMappings = new ArrayList<MappingUnit>();

	public MappingCollection() throws nullArgument {
		this.listOfMappings.add(new MappingUnit("Date1",
				new MaskTemplate().addDDay().addSep(".").addMMonth().addSep(".").addYYYYear(), MappingType.Date));
		this.listOfMappings.add(new MappingUnit("Time1",
				new MaskTemplate().addhhour().addSep(":").addmminute().addSep(":").addsecond().addSep(" ").markAMPMTime(),
				MappingType.Time));
		this.listOfMappings.add(new MappingUnit("DateAndTime1",new MaskTemplate().addDay().addSep(".").addMonth().addSep(".").addYYYYear().addSep(" ").addhour().addSep(":").addminute().addSep(":").addsecond().addSep(" ").markAMPMTime(),MappingType.DateAndTime));
		this.listOfMappings.add(new MappingUnit("Employee Unique ID", new MaskTemplate().addAnyString(),
				MappingType.EmployeeUniqueId));
		this.listOfMappings.add(new MappingUnit("Not Set", new MaskTemplate(),MappingType.NotSet));
	}

	public int getCount() {
		return this.listOfMappings.size();
	}

	public boolean contains(MappingType type) {
		for (MappingUnit mappingUnit : this.listOfMappings) {
			if (mappingUnit.getType() == type) {
				return true;
			}
		}
		return false;
	}

	public MappingUnit getMappingUnitdByName(String name) throws mappingUnitDoesNotExist {
		for (MappingUnit mappingUnit : this.listOfMappings) {
			if (mappingUnit.getName().equals(name)) {
				return mappingUnit;
			}
		}
		throw new ExceptionsPack.mappingUnitDoesNotExist("No mapping field with name " + name + "exists");
	}

	public void addMappingUnit(String name, MaskTemplate mask, MappingType employeefullname) throws nullArgument {
		this.listOfMappings.add(new MappingUnit(name, mask, employeefullname));
	}

	public void addMappingUnit(MappingUnit mappingField) {
		this.listOfMappings.add(mappingField);
	}
	
	public MappingUnit getMappingUnitdByType(MappingType type) throws mappingUnitDoesNotExist {
		for (MappingUnit mappingUnit : this.listOfMappings) {
			if (mappingUnit.getType()==type) {
				return mappingUnit;
			}
		}
		throw new ExceptionsPack.mappingUnitDoesNotExist("No mapping field with type " + type + "exists");
	}

}
