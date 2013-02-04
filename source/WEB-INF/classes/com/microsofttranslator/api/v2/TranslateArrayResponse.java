
package com.microsofttranslator.api.v2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.datacontract.schemas._2004._07.microsoft_mt_web_service.ArrayOfTranslateArrayResponse;


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
 *         &lt;element name="TranslateArrayResult" type="{http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2}ArrayOfTranslateArrayResponse" minOccurs="0"/>
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
    "translateArrayResult"
})
@XmlRootElement(name = "TranslateArrayResponse")
public class TranslateArrayResponse {

    @XmlElementRef(name = "TranslateArrayResult", namespace = "http://api.microsofttranslator.com/V2", type = JAXBElement.class)
    protected JAXBElement<ArrayOfTranslateArrayResponse> translateArrayResult;

    /**
     * Gets the value of the translateArrayResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTranslateArrayResponse }{@code >}
     *     
     */
    public JAXBElement<ArrayOfTranslateArrayResponse> getTranslateArrayResult() {
        return translateArrayResult;
    }

    /**
     * Sets the value of the translateArrayResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTranslateArrayResponse }{@code >}
     *     
     */
    public void setTranslateArrayResult(JAXBElement<ArrayOfTranslateArrayResponse> value) {
        this.translateArrayResult = ((JAXBElement<ArrayOfTranslateArrayResponse> ) value);
    }

}
