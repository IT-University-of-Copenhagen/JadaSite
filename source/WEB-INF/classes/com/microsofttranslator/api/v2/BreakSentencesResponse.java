
package com.microsofttranslator.api.v2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfint;


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
 *         &lt;element name="BreakSentencesResult" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfint" minOccurs="0"/>
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
    "breakSentencesResult"
})
@XmlRootElement(name = "BreakSentencesResponse")
public class BreakSentencesResponse {

    @XmlElementRef(name = "BreakSentencesResult", namespace = "http://api.microsofttranslator.com/V2", type = JAXBElement.class)
    protected JAXBElement<ArrayOfint> breakSentencesResult;

    /**
     * Gets the value of the breakSentencesResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}
     *     
     */
    public JAXBElement<ArrayOfint> getBreakSentencesResult() {
        return breakSentencesResult;
    }

    /**
     * Sets the value of the breakSentencesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}
     *     
     */
    public void setBreakSentencesResult(JAXBElement<ArrayOfint> value) {
        this.breakSentencesResult = ((JAXBElement<ArrayOfint> ) value);
    }

}
