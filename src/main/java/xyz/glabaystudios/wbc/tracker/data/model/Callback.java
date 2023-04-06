package xyz.glabaystudios.wbc.tracker.data.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import xyz.glabaystudios.wbc.tracker.data.model.uncached.CallbackType;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@RequiredArgsConstructor
public class Callback {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uid;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String dateOfCallback;
	private String callbackCase;
	private String time;
	private String agent;
	private String callbackType = CallbackType.UNSCHEDULED_IN.getCallbackTypeName();
	private String assigned;
	private String notes;

	private Boolean completed = Boolean.FALSE;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String dateCompleted;
	private String completedBy;
	private String timeCompleted;

	private String volunteered;
	private Boolean voluntold = Boolean.FALSE;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "callbackParentId", cascade = CascadeType.MERGE)
	private Collection<CallbackDetails> details = new ArrayList<>();

}
