package TA_Report_Tool.Data;

import java.util.ArrayList;

public class MaskTemplate {
	private ArrayList<MaskingItem> maskTemplate = new ArrayList<MaskingItem>();

	public MaskTemplate() {
		this.maskTemplate.add(MaskingItem.NotSet);
	}

	public MaskTemplate addDDay() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.DoubleDigitDay);
		return this;
	}

	public MaskTemplate addDay() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.SingleOrDoubleDigitDay);
		return this;
	}

	public MaskTemplate addSep(String string) {
		if (NotSet()) {
			reset();
		}
		switch (string) {
		case ":":
			this.maskTemplate.add(MaskingItem.SepColons);
			break;
		case " ":
			this.maskTemplate.add(MaskingItem.SpaceSep);
			break;
		case ";":
			this.maskTemplate.add(MaskingItem.SepSemiColons);
			break;
		case ".":
			this.maskTemplate.add(MaskingItem.SepDot);
			break;
		case ",":
			this.maskTemplate.add(MaskingItem.SepComma);
			break;
		case "!":
			this.maskTemplate.add(MaskingItem.SepExclamationMark);
			break;
		case "-":
			this.maskTemplate.add(MaskingItem.SepMinus);
			break;
		}
		return this;
	}

	public MaskTemplate addMMonth() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.DoubleDigitMonth);
		return this;
	}

	public MaskTemplate addMonth() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.SingleOrDoubleDigitMonth);
		return this;
	}

	public MaskTemplate addYYYYear() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.FourDigitYear);
		return this;
	}

	public MaskTemplate addYYear() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.DoubleDigitYear);
		return this;
	}

	public MaskTemplate addminute() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.SingleOrDoubleDigitMinute);
		return this;
	}

	public MaskTemplate addmminute() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.DoubleDigitMinute);
		return this;
	}

	public MaskTemplate addhour() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.SingleOrDoubleDigitHour);
		return this;
	}

	public MaskTemplate addhhour() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.DoubleDigitHour);
		return this;
	}

	public MaskTemplate addsecond() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.SingleOrDoubleDigitSecond);
		return this;
	}

	public MaskTemplate addssecond() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.DoubleDigitSecond);
		return this;
	}

	public MaskTemplate add12h() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.AMorPMMark);
		return this;
	}

	public MaskTemplate add24h() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.MilitaryHour);
		return this;
	}

	public MaskTemplate addAnyString() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.AnyString);
		return this;
	}

	public MaskTemplate addSingleInt() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.SingleDigitInt);
		return this;
	}

	public MaskTemplate addNumber() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.Number);
		return this;
	}

	public MaskTemplate addSingleCh() {
		if (NotSet()) {
			reset();
		}
		this.maskTemplate.add(MaskingItem.SingleCharacter);
		return this;
	}

	private boolean NotSet() {
		if (this.maskTemplate.get(0) == MaskingItem.NotSet) {
			return true;
		}
		return false;
	}

	private void reset() {
		this.maskTemplate.clear();
	}

	public MaskTemplate getObj() {
		return this;
	}

	public boolean isSet() {
		if ((this.maskTemplate.get(0) == MaskingItem.NotSet) && (this.maskTemplate.size() == 1)) {
			return false;
		}
		return true;
	}
}
