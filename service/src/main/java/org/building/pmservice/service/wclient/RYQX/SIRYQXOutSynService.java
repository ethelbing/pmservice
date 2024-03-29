package org.building.pmservice.service.wclient.RYQX;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.2.8
 * 2019-05-16T11:40:36.158+08:00
 * Generated source version: 3.2.8
 *
 */
@WebServiceClient(name = "SI_RYQX_Out_SynService",
                  wsdlLocation = "file:/E:/设备/接口/fwsdl/SI_RYQX_Out_Syn_PR1003.wsdl",
                  targetNamespace = "http://www.anshanmining.com/use_check/")
public class SIRYQXOutSynService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://www.anshanmining.com/use_check/", "SI_RYQX_Out_SynService");
    public final static QName SIRYQXOutSynPort = new QName("http://www.anshanmining.com/use_check/", "SI_RYQX_Out_SynPort");
    static {
        URL url = null;
        try {
            url = new URL("file:/E:/设备/接口/fwsdl/SI_RYQX_Out_Syn_PR1003.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(SIRYQXOutSynService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:/E:/设备/接口/fwsdl/SI_RYQX_Out_Syn_PR1003.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public SIRYQXOutSynService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public SIRYQXOutSynService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SIRYQXOutSynService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public SIRYQXOutSynService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public SIRYQXOutSynService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public SIRYQXOutSynService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns SIRYQXOutSyn
     */
    @WebEndpoint(name = "SI_RYQX_Out_SynPort")
    public SIRYQXOutSyn getSIRYQXOutSynPort() {
        return super.getPort(SIRYQXOutSynPort, SIRYQXOutSyn.class);
    }

    /**
     *
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SIRYQXOutSyn
     */
    @WebEndpoint(name = "SI_RYQX_Out_SynPort")
    public SIRYQXOutSyn getSIRYQXOutSynPort(WebServiceFeature... features) {
        return super.getPort(SIRYQXOutSynPort, SIRYQXOutSyn.class, features);
    }

}
