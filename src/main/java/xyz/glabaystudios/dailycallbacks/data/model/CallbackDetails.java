package xyz.glabaystudios.dailycallbacks.data.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class CallbackDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uid;

	@JsonBackReference
	private Long callbackParentId;

	private String cbDate;
	private String cbTime;
	private String cbDay;
	private String cbAttempt;
	private String cbNote;

}
