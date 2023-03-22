package xyz.glabaystudios.dailycallbacks.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import xyz.glabaystudios.dailycallbacks.data.model.Callback;
import xyz.glabaystudios.dailycallbacks.data.model.CallbackDetails;

import java.util.List;

public interface CallbackDetailsRepository extends JpaRepository<CallbackDetails, Long> {

	@Query("select c from CallbackDetails c where c.callbackParentId = ?1")
	List<CallbackDetails> findByCallbackParent(Long parentId);

}