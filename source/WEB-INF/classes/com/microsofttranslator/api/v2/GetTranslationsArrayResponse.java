
package com.microsofttranslator.api.v2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.datacontract.schemas._2004._07.microsoft_mt_web_service.ArrayOfGetTranslationsResponse;


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
 *         &lt;element name="GetTranslationsArrayResult" type="{http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2}ArrayOfGetTranslationsResponse" minOccurs="0"/>
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
    "getTranslationsArrayResult"
})
@XmlRootElement(name = "GetTranslationsArrayResponse")
public class GetTranslationsArrayResponse {

    @XmlElementRef(name = "GetTranslationsArrayResult", namespace = "http://api.microsofttranslator.com/V2", type = JAXBElement.class)
    protected JAXBElement<ArrayOfGetTranslationsResponse> getTranslationsArrayResult;

    /**
     * Gets the value of the getTranslationsArrayResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfGetTranslationsResponse }{@code >}
     *     
     */
    public JAXBElement<ArrayOfGetTranslationsResponse> getGetTranslationsArrayResult() {
        return getTranslationsArrayResult;
    }

    /**
     * Sets the value of the getTranslationsArrayResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfGetTranslationsResponse }{@code >}
     *     
     */
    public void setGetTranslationsArrayResult(JAXBElement<ArrayOfGetTranslationsResponse> value) {
        this.getTranslationsArrayResult = ((JAXBElement<ArrayOfGetTranslationsResponse> ) value);
    }

}
