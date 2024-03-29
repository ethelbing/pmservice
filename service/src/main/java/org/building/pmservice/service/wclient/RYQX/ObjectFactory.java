
package org.building.pmservice.service.wclient.RYQX;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.anshanmining.use_check package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _MTRYQXRet_QNAME = new QName("http://www.anshanmining.com/use_check/", "MT_RYQXRet");
    private final static QName _MTRYQX_QNAME = new QName("http://www.anshanmining.com/use_check/", "MT_RYQX");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.anshanmining.use_check
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTRYQX }
     * 
     */
    public DTRYQX createDTRYQX() {
        return new DTRYQX();
    }

    /**
     * Create an instance of {@link DTRYQXRet }
     * 
     */
    public DTRYQXRet createDTRYQXRet() {
        return new DTRYQXRet();
    }

    /**
     * Create an instance of {@link DTRYQX.Items }
     * 
     */
    public DTRYQX.Items createDTRYQXItems() {
        return new DTRYQX.Items();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTRYQXRet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.anshanmining.com/use_check/", name = "MT_RYQXRet")
    public JAXBElement<DTRYQXRet> createMTRYQXRet(DTRYQXRet value) {
        return new JAXBElement<DTRYQXRet>(_MTRYQXRet_QNAME, DTRYQXRet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTRYQX }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.anshanmining.com/use_check/", name = "MT_RYQX")
    public JAXBElement<DTRYQX> createMTRYQX(DTRYQX value) {
        return new JAXBElement<DTRYQX>(_MTRYQX_QNAME, DTRYQX.class, null, value);
    }

}
