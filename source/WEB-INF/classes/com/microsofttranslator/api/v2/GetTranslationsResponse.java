
package com.microsofttranslator.api.v2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetTranslationsResult" type="{http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2}GetTranslationsResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getTranslationsResult"
})
@XmlRootElement(name = "GetTranslationsResponse")
public class GetTranslationsResponse {

    @XmlElementRef(name = "GetTranslationsResult", namespace = "http://api.microsofttranslator.com/V2", type = JAXBElement.class)
    protected JAXBElement<org.datacontract.schemas._2004._07.microsoft_mt_web_service.GetTranslationsResponse> getTranslationsResult;

    /**
     * Gets the value of the getTranslationsResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link org.datacontract.schemas._2004._07.microsoft_mt_web_service.GetTranslationsResponse }{@code >}
     *     
     */
    public JAXBElement<org.datacontract.schemas._2004._07.microsoft_mt_web_service.GetTranslationsResponse> getGetTranslationsResult() {
        return getTranslationsResult;
    }

    /**
     * Sets the value of the getTranslationsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link org.datacontract.schemas._2004._07.microsoft_mt_web_service.GetTranslationsResponse }{@code >}
     *     
     */
    public void setGetTranslationsResult(JAXBElement<org.datacontract.schemas._2004._07.microsoft_mt_web_service.GetTranslationsResponse> value) {
        this.getTranslationsResult = ((JAXBElement<org.datacontract.schemas._2004._07.microsoft_mt_web_service.GetTranslationsResponse> ) value);
    }

}
