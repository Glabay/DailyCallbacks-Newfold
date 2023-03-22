package xyz.glabaystudios.dailycallbacks.data.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
