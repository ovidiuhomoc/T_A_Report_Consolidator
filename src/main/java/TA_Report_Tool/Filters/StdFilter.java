package TA_Report_Tool.Filters;

import java.util.HashSet;
import static TA_Report_Tool.Tools.debug.*;
import java.util.Iterator;

import TA_Report_Tool.Data.ColumnProperties;
import TA_Report_Tool.Data.MappingType;

import static TA_Report_Tool.Tools.check.*;

public class StdFilter {

	private ColumnProperties column;
	private FilterType type = null;

	private HashSet<String> txtInclusionList;
	private HashSet<String> txtExclusionList;
	private HashSet<String> containsSubtextList;
	private HashSet<String> excludesSubtextList;

	private HashSet<Integer> noInclusionList;
	private HashSet<Integer> noExclusionList;
	private int minLimit;
	private int maxLimit;

	public StdFilter(ColumnProperties column) {
		this.column = column;
		setFilterType(this.column);
		initialize();
	}

	public <T> void addToExclusionList(T element) {
		if (typeIsText()) {
			this.txtExclusionList.add((String) element);
		} else {
			this.noExclusionList.add((Integer) element);
		}
	}

	public <T> void addToInclusionList(T element) {
		if (typeIsText()) {
			this.txtInclusionList.add((String) element);
		} else {
			this.noInclusionList.add((Integer) element);
		}
	}

	public <T> void removeFromExclusionList(T element) {
		if (typeIsText()) {
			this.txtExclusionList.remove((String) element);
		} else {
			this.noExclusionList.remove((Integer) element);
		}
	}

	public <T> void removeFromInclusionList(T element) {
		if (typeIsText()) {
			this.txtInclusionList.remove((String) element);
		} else {
			this.noInclusionList.remove((Integer) element);
		}
	}

	public void clearAllListsAndLimits() {
		if (isNotNull(this.txtInclusionList)) {
			this.txtInclusionList.clear();
		}
		if (isNotNull(this.txtExclusionList)) {
			this.txtExclusionList.clear();
		}
		if (isNotNull(this.noInclusionList)) {
			this.noInclusionList.clear();
		}
		if (isNotNull(this.noExclusionList)) {
			this.noExclusionList.clear();
		}
		if (isNotNull(this.containsSubtextList)) {
			this.containsSubtextList.clear();
		}
		if (isNotNull(this.excludesSubtextList)) {
			this.excludesSubtextList.clear();
		}
		
		this.setMaxLimit(Integer.MAX_VALUE);
		this.setMinLimit(Integer.MIN_VALUE);
		display(this.getClass().getSimpleName(),"\t All lists and limits that were not null, were cleared");
	}

	public void clearExclusionList() {
		if (typeIsText()) {
			this.txtExclusionList.clear();
		} else {
			this.noExclusionList.clear();
		}
	}

	public void clearInclusionList() {
		if (typeIsText()) {
			this.txtInclusionList.clear();
		} else {
			this.noInclusionList.clear();
		}
	}

	public void setMinLimit(int minLimit) {
		if (typeIsText()) {
			return;
		}
		this.minLimit = minLimit;
		display(this.getClass().getSimpleName(),"\t Minimum limit set");
	}

	public void setMaxLimit(int maxLimit) {
		if (typeIsText()) {
			return;
		}
		this.maxLimit = maxLimit;
	}

	/**
	 * Method used to add subtext that is needed to be found within each line, in
	 * order to pass. if 2 or more subtext are added, the filter will be even more
	 * restrictive requiring all subtext to be included
	 * 
	 * @param subtext of String type
	 */
	public void addSubtextInclusion(String subtext) {
		if (typeIsText()) {
			this.containsSubtextList.add(subtext);
		}
	}

	/**
	 * Method used to add subtext that needs to be filtered. Any line that will
	 * contain at least one subtext, will not pass.
	 * 
	 * @param subtext of String type
	 */
	public void addSubtextExclusion(String subtext) {
		if (typeIsText()) {
			this.excludesSubtextList.add(subtext);
		}
	}

	public void removeSubtextInclusion(String subtext) {
		if (typeIsText()) {
			this.containsSubtextList.remove(subtext);
		}
	}

	public void removeSubtextExclusion(String subtext) {
		if (typeIsText()) {
			this.excludesSubtextList.remove(subtext);
		}
	}

	public void clearSubtextInclusion() {
		if (typeIsText()) {
			this.containsSubtextList.clear();
		}
	}

	public void clearSubtextExclusion() {
		if (typeIsText()) {
			this.excludesSubtextList.clear();
		}
	}

	public <T> boolean passFilterCriterias(T dataToPassFilters) {
		display(this.getClass().getSimpleName(),
				"\t Analyzing standard filter pass for " + String.valueOf(dataToPassFilters));
		if (typeIsText()) {
			String txt = (String) dataToPassFilters;

			if (isFalse(passTxtExclusionList(txt))) {
				display(this.getClass().getSimpleName(), "\t passTxtExclusionList(" + txt + ") failed");
				return false;
			}

			if (isFalse(passTxtInclusionList(txt))) {
				display(this.getClass().getSimpleName(), "\t passTxtInclusionList(" + txt + ") failed");
				return false;
			}

			if (isFalse(passTxtContainsSubtext(txt))) {
				display(this.getClass().getSimpleName(), "\t passTxtContainsSubtext(" + txt + ") failed");
				return false;
			}

			if (isFalse(passTxtExcludesSubtext(txt))) {
				display(this.getClass().getSimpleName(), "\t passTxtExcludesSubtext(" + txt + ") failed");
				return false;
			}

			return true;
		} else {
			int no = (Integer) dataToPassFilters;

			if (isFalse(passNoEqualOrOverMinLimit(no))) {
				display(this.getClass().getSimpleName(), "\t passNoEqualOrOverMinLimit(" + no + ") failed");
				return false;
			}

			if (isFalse(passNoEqualOrBellowMaxLimit(no))) {
				display(this.getClass().getSimpleName(), "\t passNoEqualOrBellowMaxLimit(" + no + ") failed");
				return false;
			}

			if (isFalse(passNoOnInclusionList(no))) {
				display(this.getClass().getSimpleName(), "\t passNoOnInclusionList(" + no + ") failed");
				return false;
			}

			if (isFalse(passNoNotOnExclusionList(no))) {
				display(this.getClass().getSimpleName(), "\t passNoNotOnExclusionList(" + no + ") failed");
				return false;
			}

			return true;
		}
	}

	public String[] txtFilterListToArray(FilterListType list) {
		if (typeIsText()) {
			switch (list) {
			case txtInclusionList:
				return this.txtInclusionList.toArray(new String[0]);
			case txtExclusionList:
				return this.txtExclusionList.toArray(new String[0]);
			case containsSubtextList:
				return this.containsSubtextList.toArray(new String[0]);
			case excludesSubtextList:
				return this.excludesSubtextList.toArray(new String[0]);
			default:
				return null;
			}
		}
		return null;
	}

	public Integer[] numberFilterListToArray(FilterListType list) {
		if (isFalse(typeIsText())) {
			switch (list) {
			case numberInclusionList:
				return this.noInclusionList.toArray(new Integer[0]);
			case numberExclusionList:
				return this.noExclusionList.toArray(new Integer[0]);
			default:
				return null;
			}
		}
		return null;
	}

	private void initialize() {
		if (typeIsText()) {
			this.txtInclusionList = new HashSet<String>();
			this.txtExclusionList = new HashSet<String>();
			this.containsSubtextList = new HashSet<String>();
			this.excludesSubtextList = new HashSet<String>();
		} else {
			// type is number
			this.noInclusionList = new HashSet<Integer>();
			this.noExclusionList = new HashSet<Integer>();
			this.containsSubtextList = null;
			this.excludesSubtextList = null;
			this.minLimit = Integer.MIN_VALUE;
			this.maxLimit = Integer.MAX_VALUE;
		}
	}

	private boolean typeIsText() {
		if (this.type == FilterType.text) {
			return true;
		}
		return false;
	}

	private void setFilterType(ColumnProperties column) {
		switch (column.getMappingUnit().getType()) {
		case Number:
			this.type = FilterType.number;
			break;
		case FixedDigitNumber:
			this.type = FilterType.number;
			break;
		case SignalingDevice:
			this.type = FilterType.text;
			break;
		case EmployeeUniqueId:
			this.type = FilterType.text;
			break;
		case EmployeeFirstName:
			this.type = FilterType.text;
			break;
		case EmployeeMiddleName:
			this.type = FilterType.text;
			break;
		case EmployeeLastName:
			this.type = FilterType.text;
			break;
		case EmployeeFullName:
			this.type = FilterType.text;
			break;
		case Event:
			this.type = FilterType.text;
			break;
		case CustomFieldText:
			this.type = FilterType.text;
			break;
		case NotSet:
			this.type = FilterType.text;
			break;
		default:
			this.type = FilterType.text;
			break;
		}
	}

	private boolean passNoNotOnExclusionList(int no) {
		display(this.getClass().getSimpleName(), "\t Analyzing if no " + no + " is on exclusion list");
		return !this.noExclusionList.contains(no);
	}

	private boolean passNoOnInclusionList(int no) {
		display(this.getClass().getSimpleName(), "\t Analyzing if no " + no + " is on inclusion list");
		if (this.noInclusionList.isEmpty()) {
			return true;
		}

		return this.noInclusionList.contains(no);
	}

	private boolean passNoEqualOrBellowMaxLimit(int no) {
		display(this.getClass().getSimpleName(), "\t Analyzing if no " + no + " <= max limit");
		if (no <= this.maxLimit) {
			display(this.getClass().getSimpleName(), "\t\t it is");
			return true;
		}
		display(this.getClass().getSimpleName(), "\t\t it is not");
		return false;
	}

	private boolean passNoEqualOrOverMinLimit(int no) {
		display(this.getClass().getSimpleName(), "\t Analyzing if no " + no + " >= min limit");
		if (no >= this.minLimit) {
			display(this.getClass().getSimpleName(), "\t\t it is");
			return true;
		}
		display(this.getClass().getSimpleName(), "\t\t it is not");
		return false;
	}

	private boolean passTxtExcludesSubtext(String txt) {
		display(this.getClass().getSimpleName(), "\t Analyzing if text " + txt + " excludes all subtexts from list");
		Iterator<String> it = excludesSubtextList.iterator();
		while (it.hasNext()) {
			String subTxt = it.next();
			display(this.getClass().getSimpleName(), "\t Does text " + txt + " contains subtext " + subTxt + " ?");
			if (txt.contains(subTxt)) {
				display(this.getClass().getSimpleName(), "\t\t It contains");
				return false;
			}
			display(this.getClass().getSimpleName(), "\t\t It does not contains");
		}
		return true;
	}

	private boolean passTxtContainsSubtext(String txt) {
		display(this.getClass().getSimpleName(), "\t Analyzing if text " + txt + " contains all subtexts from list");
		if (containsSubtextList.isEmpty()) {
			return true;
		}
		Iterator<String> it = containsSubtextList.iterator();
		while (it.hasNext()) {
			String subTxt = it.next();
			display(this.getClass().getSimpleName(), "\t Does text " + txt + " contains subtext " + subTxt + " ?");
			if (isFalse(txt.contains(subTxt))) {
				display(this.getClass().getSimpleName(), "\t\t It does not contains");
				return false;
			}
			display(this.getClass().getSimpleName(), "\t\t It contains");
		}
		return true;
	}

	private boolean passTxtInclusionList(String txt) {
		display(this.getClass().getSimpleName(), "\t Analyzing passing of text " + txt + " versus inclusion list");
		if (txtInclusionList.isEmpty()) {
			return true;
		}
		return txtInclusionList.contains(txt);
	}

	private boolean passTxtExclusionList(String txt) {
		display(this.getClass().getSimpleName(), "\t Analyzing passing of text " + txt + " versus exclusion list");
		return !txtExclusionList.contains(txt);
	}
}
