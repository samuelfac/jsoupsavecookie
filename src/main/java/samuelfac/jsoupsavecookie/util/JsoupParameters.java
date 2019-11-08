package samuelfac.jsoupsavecookie.util;

import java.util.HashMap;

import org.jsoup.Connection.Method;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class JsoupParameters {

	@Getter
	@Setter
	@NonNull
	String url;

	@Getter
	@Setter
	@NonNull
	@Builder.Default
	Method method = Method.GET;

	/**
	 * You can use: param.getHeaders().put("Content-Type", "multipart/form-data");
	 */
	@Getter
	@Builder.Default
	HashMap<String, String> headers = new HashMap<String, String>();

	@Getter
	@Builder.Default
	HashMap<String, String> data = new HashMap<String, String>();

	@Getter
	@Setter
	String requestBody;

	/**
	 * with Method.GET
	 * 
	 * @param url
	 */
	public JsoupParameters(String url) {
		super();
		this.url = url;
		this.method = Method.GET;
	}
}
