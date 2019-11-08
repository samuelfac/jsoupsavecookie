package samuelfac.jsoupsavecookie;

import org.jsoup.nodes.Document;

import junit.framework.TestCase;
import samuelfac.jsoupsavecookie.util.JsoupParameters;

/**
 * Unit test for simple App.
 */
public class JsoupSaveCookieTest extends TestCase {

	JsoupSaveCookie jsoup;
	JsoupSaveCookie jsoupIgnoreSsl;

	protected void setUp() {
		jsoup = new JsoupSaveCookie();
		jsoupIgnoreSsl = new JsoupSaveCookie(true);
	}

	public void testGoogleGetParamBuilder() throws Exception {
		Document doc = jsoupIgnoreSsl.executeDoc(JsoupParameters.builder().url("https://google.com").build());
		assertNotNull(doc);
		assertTrue(doc.title().equalsIgnoreCase("google"));
	}

	public void testGoogleGetParamNew() throws Exception {
		Document doc = jsoup.executeDoc(new JsoupParameters("https://google.com"));
		assertNotNull(doc);
		assertTrue(doc.title().equalsIgnoreCase("google"));
	}
	
	public void testGoogleGetParamNewAddHeader() throws Exception {
		JsoupParameters param = new JsoupParameters("https://google.com");
		param.getHeaders().put("key1", "value1");
		param.getHeaders().put("key2", "value2");
		Document doc = jsoup.executeDoc(param);
		assertNotNull(doc);
		assertTrue(doc.title().equalsIgnoreCase("google"));
	}
}
