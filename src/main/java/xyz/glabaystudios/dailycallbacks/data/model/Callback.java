package xyz.glabaystudios.dailycallbacks.data.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Callback {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uid;

	private String dateOfCallback;
	private String callbackCase;
	private String time;
	private String agent;
	private String notes;

	private Boolean completed = Boolean.FALSE;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "callbackParentId", cascade = CascadeType.MERGE)
	private Collection<CallbackDetails> details = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Callback callback = (Callback) o;
		return uid != null && Objects.equals(uid, callback.uid);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
