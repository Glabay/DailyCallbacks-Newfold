package xyz.glabaystudios.dailycallbacks.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.glabaystudios.dailycallbacks.data.model.Callback;
import xyz.glabaystudios.dailycallbacks.data.model.CallbackDetails;
import xyz.glabaystudios.dailycallbacks.data.model.uncached.CallbackType;
import xyz.glabaystudios.dailycallbacks.data.model.uncached.YearToDateStats;
import xyz.glabaystudios.dailycallbacks.data.repo.CallbackDetailsRepository;
import xyz.glabaystudios.dailycallbacks.data.repo.CallbackRepository;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@AllArgsConstructor
@Service
public class CallbackService {

	private final CallbackRepository callbackRepository;
	private final CallbackDetailsRepository callbackDetailsRepository;


	/**
	 * Find all Callbacks in the database
	 * @return a List of callbacks
	 */
	public List<Callback> findAll() {
		return callbackRepository.findAll();
	}

	/**
	 * Find all Callbacks in the database
	 * @return a List of callbacks
	 */
	public List<Callback> findAllOpenCallbacks() {
		return callbackRepository.findByCompletedFalse();
	}

	/**
	 * Find a callback by its unique ID
	 * @param id the unique id of the {@link Callback}
	 * @return a {@link Callback}
	 */
	public Callback findCallbackById(Long id) {
		return callbackRepository.findCallbackByUid(id).orElse(null);
	}

	/**
	 * Fetch the list of notes/details for a given callback
	 *
	 * @param callback the requesting Callback
	 * @return A list of Callback Details
	 */
	public List<CallbackDetails> findAllDetailsRelatedToCallback(Callback callback) {
		Callback parent = findCallbackById(callback.getUid());
		if (Objects.isNull(parent)) return null;
		return callbackDetailsRepository.findByCallbackParent(parent.getUid());
	}

	public void save(Callback callback) {
		callbackRepository.save(callback);
		callbackDetailsRepository.saveAll(callback.getDetails());
	}

	public void save(List<CallbackDetails> details) {
		callbackDetailsRepository.saveAll(details);
	}

	public YearToDateStats fetchCallbackStatsForYearToDate(int year) {
		YearToDateStats yearToDateStats = new YearToDateStats();
		// Get a complete List of all callbacks
		List<Callback> totalCallbackList = findAll();
		// pre-checks to make sure we have a list with content
		if (Objects.nonNull(totalCallbackList) && !totalCallbackList.isEmpty()) {
			// loop over each of the callbacks within the list
			totalCallbackList.forEach(callback -> {
				String[] cbDateData = callback.getDateOfCallback().split("-");
				int cbYear = Integer.parseInt(cbDateData[0]); // year of this CB
				// if the callback is from the requested year
				if (Objects.equals(cbYear, year)) {
					// get the type of the callback
					String cbType = callback.getCallbackType();
					// parse the callback type
					CallbackType type = CallbackType.valueOf(cbType);
					// parse the Month of this callback
					int month = Integer.parseInt(cbDateData[1].startsWith("0") ? cbDateData[1].replace("0", "") : cbDateData[1]);
					String monthSr = Month.of(month).getDisplayName(TextStyle.SHORT_STANDALONE, Locale.CANADA).replace(".", "");
					// update the record
					yearToDateStats.updateCallType(type.getCallbackTypeName(), monthSr);
				}
				// else the callback was not of the requested year
			});
			return yearToDateStats;
		}
		return null;
	}

	public List<Callback> findOpenCallbacksForProvidedDate(String formattedDate) {
		// start out with the list of callbacks that are not completed, that match the provided date
		List<Callback> totalDailyCallbacks = callbackRepository.findByDateOfCallbackAndCompletedFalse(formattedDate);
		// Next, grab the complete list of callbacks and loop over them to check them out
		findAllOpenCallbacks().stream()
				// We are only looking for callbacks with details
				.filter(callback -> Objects.nonNull(callback.getDetails()) && !callback.getDetails().isEmpty())
				// for each of the open callbacks with details, we want to add them to the daily total, as these may have been on going
				.forEach(totalDailyCallbacks::add);
		return totalDailyCallbacks;
	}
}
