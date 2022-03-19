package time_and_attendance_report_consolidator.Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import time_and_attendance_report_consolidator.Profile;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Testing_the_TA_report_events_filters {

	@Test
	void Testing_the_events_filtering_section() {
		Profile testProfile = Profile.getActive();
		assertTrue(testProfile.isAllEventsActive());

		testProfile.addEvent("Test Event");
		assertFalse(testProfile.isAllEventsActive());
		assertTrue(testProfile.containsEvent("Test Event"));
		testProfile.removeEvent("Test Event");
		assertFalse(testProfile.containsEvent("Test Event"));

		Tools_Random_Generator rand = new Tools_Random_Generator();
		String event = new String();
		Set<String> filteringEvents = new HashSet<String>();

		event = rand.randomTextAlphaNumeric(20);
		filteringEvents.add(event);
		assertFalse(testProfile.containsEvent(event));
		testProfile.addEvent(event);
		assertTrue(testProfile.containsEvent(event));
		testProfile.removeEvent(event);
		assertFalse(testProfile.containsEvent(event));

		event = rand.randomTextAlphaNumericAndBeginWithSymbols(20);
		filteringEvents.add(event);
		assertFalse(testProfile.containsEvent(event));
		testProfile.addEvent(event);
		assertTrue(testProfile.containsEvent(event));
		testProfile.removeEvent(event);
		assertFalse(testProfile.containsEvent(event));

		event = rand.randomTextAlphaNumericAndEndWithSymbols(20);
		filteringEvents.add(event);
		assertFalse(testProfile.containsEvent(event));
		testProfile.addEvent(event);
		assertTrue(testProfile.containsEvent(event));
		testProfile.removeEvent(event);
		assertFalse(testProfile.containsEvent(event));

		event = rand.randomTextAlphaNumericAndSymbols(20);
		filteringEvents.add(event);
		assertFalse(testProfile.containsEvent(event));
		testProfile.addEvent(event);
		assertTrue(testProfile.containsEvent(event));
		testProfile.removeEvent(event);
		assertFalse(testProfile.containsEvent(event));

		event = rand.randomTextAlphaNumericAndSymbolsBalancedDistribution(20);
		filteringEvents.add(event);
		assertFalse(testProfile.containsEvent(event));
		testProfile.addEvent(event);
		assertTrue(testProfile.containsEvent(event));
		testProfile.removeEvent(event);
		assertFalse(testProfile.containsEvent(event));

		event = null;
		filteringEvents.add(event);
		assertFalse(testProfile.containsEvent(event));
		testProfile.addEvent(event);
		assertTrue(testProfile.containsEvent(event));
		assertFalse(testProfile.isAllEventsActive());
		testProfile.removeEvent(event);
		assertFalse(testProfile.containsEvent(event));
		assertTrue(testProfile.isAllEventsActive());

		Iterator<String> iteratorEvent = filteringEvents.iterator();
		while (iteratorEvent.hasNext()) {
			testProfile.addEvent(iteratorEvent.next());
		}

		Tools_Array_Equality_Test compareTool = new Tools_Array_Equality_Test();
		assertTrue(compareTool.TestStringArrayUnorderedEquality(filteringEvents.toArray(new String[0]),
				testProfile.eventsToArray()));
		testProfile.useAllEvents();
		assertTrue(testProfile.isAllEventsActive());
		assertFalse(compareTool.TestStringArrayUnorderedEquality(filteringEvents.toArray(new String[0]),
				testProfile.eventsToArray()));
	}

}
