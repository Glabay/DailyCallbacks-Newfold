package xyz.glabaystudios.wbc.tracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.glabaystudios.wbc.tracker.data.model.Callback;
import xyz.glabaystudios.wbc.tracker.data.model.CallbackDetails;
import xyz.glabaystudios.wbc.tracker.services.CallbackService;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DailyCallbacksApplicationTests {

	@Autowired
    CallbackService callbackService;

	@Test
	void contextLoads() {
		Callback dummy = getDummyCallback();

		callbackService.save(dummy);

		List<CallbackDetails> callbackDetails = getMockDatabaseCallForCollectionOfDetails(dummy);
		dummy.setDetails(callbackDetails); // details

		callbackService.save(dummy.getDetails().stream().toList());

		List<Callback> callbacks = callbackService.findAll();

		System.out.println(callbacks);

	}

	Callback getDummyCallback() {
		Callback mockup = new Callback();

		mockup.setDateOfCallback("08/22/2022"); // Date
		mockup.setCallbackCase("IN-0064"); // case number
		mockup.setTime("10:00 AM (ADT)"); // Time
		mockup.setAgent("m.glabay"); // WBC Agent
		mockup.setNotes("Interactive Interview Due: August 22nd, 2022"); // Pega Notes
		return mockup;
	}

	List<CallbackDetails> getMockDatabaseCallForCollectionOfDetails(Callback parent) {

		CallbackDetails cb1 = new CallbackDetails();

		cb1.setCbDate("08/22/2022"); // Date
		cb1.setCbDay("1"); // Day
		cb1.setCbAttempt("1"); // Attempt
		cb1.setCbTime("10:00 AM"); // Time of the attempt
		cb1.setCbNote("No answer, left VM"); // notes from the WBC
		cb1.setCallbackParentId(parent.getUid()); // Parent Callback

		CallbackDetails cb2 = new CallbackDetails();

		cb2.setCbDate("08/22/2022"); // Date
		cb2.setCbDay("1"); // Day
		cb2.setCbAttempt("2"); // Attempt
		cb2.setCbTime("11:15 AM"); // Time of the attempt
		cb2.setCbNote("No answer, left VM, Email sent"); // notes from the WBC
		cb2.setCallbackParentId(parent.getUid()); // Parent Callback

		List<CallbackDetails> list = new ArrayList<>();
		list.add(cb1);
		list.add(cb2);

		System.out.println(list);
		return list;
	}

}
