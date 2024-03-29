package xyz.glabaystudios.wbc.tracker.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.glabaystudios.wbc.tracker.data.model.Callback;

import java.util.List;
import java.util.Optional;

public interface CallbackRepository extends JpaRepository<Callback, Long> {
	List<Callback> findByDateOfCallbackAndCompletedFalse(String dateOfCallback);

	List<Callback> findByCompletedFalse();

	Optional<Callback> findCallbackByUid(Long uid);
}