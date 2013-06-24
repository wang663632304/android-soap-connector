package pl.aaugustyniak.stateconnectorclient.helpers;

import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class SoapWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String LIST_METHODS = "getMethodsList";

	private String serviceUrl;
	private String apiKey;
	private Vector<String> methodsList;
	private Object response;

	@SuppressWarnings("unchecked")
	public SoapWrapper(String serviceUrl, String apiKey)
			throws XmlPullParserException, IOException {
		this.serviceUrl = serviceUrl;
		this.apiKey = apiKey;
		Vector<String> res = (Vector<String>) makeSoapCall(LIST_METHODS);
		if (!res.isEmpty()) {
			methodsList = (Vector<String>) res;
		} else {
			throw new IOException();
		}
	}

	public SoapWrapper callMethod(String method) throws IOException,
			XmlPullParserException {
		response = makeSoapCall(method);
		return this;
	}

	private Object makeSoapCall(String method) throws IOException,
			XmlPullParserException {
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		SoapObject request = new SoapObject(serviceUrl, method);
		PropertyInfo pi = new PropertyInfo();
		pi.setName("apiKey");
		pi.setValue(this.apiKey);
		pi.setType(String.class);
		request.addProperty(pi);
		envelope.setOutputSoapObject(request);
		HttpTransportSE httpTransport = new HttpTransportSE(serviceUrl
				+ "?wsdl");
		httpTransport.call(method, envelope);
		return envelope.getResponse();
	}

	/**
	 * @return the methodsList
	 */
	public Vector<String> getMethodsList() {
		return methodsList;
	}

	/**
	 * @return the response
	 */
	public Object getResponse() {
		return response;
	}
}
