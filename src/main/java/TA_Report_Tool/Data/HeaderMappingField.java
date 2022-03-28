package TA_Report_Tool.Data;

import java.util.function.BooleanSupplier;

public class HeaderMappingField {
	private String name;
	private MaskTemplate mask;
	private mappingType type;

	public HeaderMappingField(String name, MaskTemplate mask, mappingType type) {
		this.name = name;
		this.mask = mask;
		this.type = type;
	}

	public mappingType getType() {
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
