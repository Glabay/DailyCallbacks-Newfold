package xyz.glabaystudios.dailycallbacks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.glabaystudios.dailycallbacks.data.model.Agent;
import xyz.glabaystudios.dailycallbacks.data.model.Callback;
import xyz.glabaystudios.dailycallbacks.data.model.CallbackDetails;
import xyz.glabaystudios.dailycallbacks.data.repo.CallbackDetailsRepository;
import xyz.glabaystudios.dailycallbacks.data.repo.CallbackRepository;

import java.util.List;
import java.util.Objects;

@Service
public class CallbackService {

	private final CallbackRepository callbackRepository;

	private final CallbackDetailsRepository callbackDetailsRepository;

	@Autowired
	public CallbackService(CallbackRepository callbackRepository, CallbackDetailsRepository callbackDetailsRepository) {
		this.callbackRepository = callbackRepository;
		this.callbackDetailsRepository = callbackDetailsRepository;
	}

	/**
	 * Find all Callbacks in the database
	 * @return a List of callbacks
	 */
	public List<Callback> findAll() {
		return callbackRepository.findAll();
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
}
