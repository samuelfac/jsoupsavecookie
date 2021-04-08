package samuelfac.jsoupsavecookie;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import samuelfac.jsoupsavecookie.util.JsoupParameters;

/**
 * Connect with a URL with Jsoup saving the cookies and reutilizes then in the next execution.</br>
 * </br><b>Example of use:</b></br>
 * </br> JsoupSaveCookie jsoup = new JsoupSaveCookie();
 * </br> Document doc = jsoup.executeDoc(new JsoupParameters("https://www.google.com"));
 * </br> or:
 * </br> Document doc = jsoup.executeDoc(JsoupParameters.builder().url("https://www.google.com").build());
 * 
 * @author samuelfac
 */
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JsoupSaveCookie {

	/**
	 * DO NOT USE THIS!
	 */
	@NonNull
	Boolean ignoreSsl;
	
	/**
	 * Set HTTP proxy
	 * <br>Ex: <b>new InetSocketAddress("http://proxy.server.net", 3131);</b>
	 */
	public InetSocketAddress proxy = null;

	@Getter
	final Map<String, String> savedCookies = new HashMap<String, String>();

	/**
	 * The same of execute, but parses return to Document
	 * 
	 * @param param
	 * @return Document of url
	 * @throws Exception
	 */
	@SneakyThrows
	public Document executeDoc(@NonNull JsoupParameters param) {
		return execute(param).parse();
	}

	/**
	 * Connect with a URL with Jsoup saving the cookies and reutilizes then in the
	 * next execution
	 * 
	 * @param param
	 * @return Response of url
	 * @throws Exception
	 */
	@SneakyThrows
	public Response execute(@NonNull JsoupParameters param) {
		Connection conn = Jsoup.connect(param.getUrl()).userAgent(org.jsoup.helper.HttpConnection.DEFAULT_UA);
		conn.method(param.getMethod());
		if (param.getData() != null) {
			conn.data(param.getData());
		}
		if (param.getHeaders() != null) {
			conn.headers(param.getHeaders());
		}
		if (param.getRequestBody() != null) {
			conn.requestBody(param.getRequestBody());
		}
		if (proxy != null) {
			conn.proxy(new Proxy(Type.HTTP, proxy));
		}
		if (!savedCookies.isEmpty()) {
			conn.cookies(savedCookies);
		}
		if (ignoreSsl != null && ignoreSsl.booleanValue() == true) {
			conn.sslSocketFactory(returnIgnoreSsl());
		}
		conn.ignoreContentType(true);
		conn.followRedirects(true);

		Response resp = conn.execute();
		if (!resp.cookies().isEmpty()) {
			savedCookies.clear();
			savedCookies.putAll(resp.cookies());
		}

		return resp;
	}

	/**
	 * DO NOT USE THIS! Ignore SSL verification
	 * 
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	@SneakyThrows
	private SSLSocketFactory returnIgnoreSsl() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}
		} };

		SSLContext sc = null;
		sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		return sc.getSocketFactory();
	}

}
