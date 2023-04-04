package xyz.glabaystudios.exceptions;

import java.net.http.HttpClient;
import java.time.Duration;

public class NetworkExceptionHandler {

	public static void handleException(String errorDesc, Exception thrownException) {
		System.out.println(errorDesc);
		if (thrownException == null) {
			System.out.println("Error message was null.");
			return;
		}
		thrownException.printStackTrace();
	}

	private static final HttpClient httpClient = HttpClient.newBuilder()
			.version(HttpClient.Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(10))
			.build();

}
