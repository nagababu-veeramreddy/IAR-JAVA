package com.fb.in.app.reporting.generated;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.7.0 2019-07-05T18:44:44.519+05:30
 * Generated source version: 2.7.0
 * 
 */

//@WebServiceClient(name = "FishbowlSSO", wsdlLocation = "file:/D:/Projects/Fishbowl/FbInsights_Repository/IAR-JAVA/InAppReporting/src/main/resources/wsdl/fishbowlsso.wsdl", targetNamespace = "http://tempuri.org/")
@WebServiceClient(name = "FishbowlSSO", wsdlLocation = "file:/home/fbpoweruser/IAR-JAVA/src/main/resources/wsdl/fishbowlsso.wsdl", targetNamespace = "http://tempuri.org/")
public class FishbowlSSO extends Service {

	public final static URL WSDL_LOCATION;

	public final static QName SERVICE = new QName("http://tempuri.org/", "FishbowlSSO");
	public final static QName FishbowlSSOSoap12 = new QName("http://tempuri.org/", "FishbowlSSOSoap12");
	public final static QName FishbowlSSOSoap = new QName("http://tempuri.org/", "FishbowlSSOSoap");
	static {
		URL url = null;
		try {

			url = new URL("file:/home/fbpoweruser/IAR-JAVA/src/main/resources/wsdl/fishbowlsso.wsdl");
			/*
			 * url = new URL(
			 * "file:/D:/Projects/Fishbowl/FbInsights_Repository/IAR-JAVA/InAppReporting/src/main/resources/wsdl/fishbowlsso.wsdl"
			 * );
			 */ } catch (MalformedURLException e) {
			java.util.logging.Logger.getLogger(FishbowlSSO.class.getName()).log(java.util.logging.Level.INFO,
					"Can not initialize the default wsdl from {0}",
					"file:/home/fbpoweruser/IAR-JAVA/InAppReporting/src/main/resources/wsdl/fishbowlsso.wsdl");
		}
		WSDL_LOCATION = url;
	}

	public FishbowlSSO(URL wsdlLocation) {
		super(wsdlLocation, SERVICE);
	}

	public FishbowlSSO(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	public FishbowlSSO() {
		super(WSDL_LOCATION, SERVICE);
	}

	// This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
	// API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
	// compliant code instead.
	public FishbowlSSO(WebServiceFeature... features) {
		super(WSDL_LOCATION, SERVICE, features);
	}

	// This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
	// API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
	// compliant code instead.
	public FishbowlSSO(URL wsdlLocation, WebServiceFeature... features) {
		super(wsdlLocation, SERVICE, features);
	}

	// This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
	// API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
	// compliant code instead.
	public FishbowlSSO(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
		super(wsdlLocation, serviceName, features);
	}

	/**
	 *
	 * @return returns FishbowlSSOSoap
	 */
	@WebEndpoint(name = "FishbowlSSOSoap12")
	public FishbowlSSOSoap getFishbowlSSOSoap12() {
		return super.getPort(FishbowlSSOSoap12, FishbowlSSOSoap.class);
	}

	/**
	 * 
	 * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure
	 *                 on the proxy. Supported features not in the
	 *                 <code>features</code> parameter will have their default
	 *                 values.
	 * @return returns FishbowlSSOSoap
	 */
	@WebEndpoint(name = "FishbowlSSOSoap12")
	public FishbowlSSOSoap getFishbowlSSOSoap12(WebServiceFeature... features) {
		return super.getPort(FishbowlSSOSoap12, FishbowlSSOSoap.class, features);
	}

	/**
	 *
	 * @return returns FishbowlSSOSoap
	 */
	@WebEndpoint(name = "FishbowlSSOSoap")
	public FishbowlSSOSoap getFishbowlSSOSoap() {
		return super.getPort(FishbowlSSOSoap, FishbowlSSOSoap.class);
	}

	/**
	 * 
	 * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure
	 *                 on the proxy. Supported features not in the
	 *                 <code>features</code> parameter will have their default
	 *                 values.
	 * @return returns FishbowlSSOSoap
	 */
	@WebEndpoint(name = "FishbowlSSOSoap")
	public FishbowlSSOSoap getFishbowlSSOSoap(WebServiceFeature... features) {
		return super.getPort(FishbowlSSOSoap, FishbowlSSOSoap.class, features);
	}

}