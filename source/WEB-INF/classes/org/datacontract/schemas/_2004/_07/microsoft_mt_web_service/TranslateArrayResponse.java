
package org.datacontract.schemas._2004._07.microsoft_mt_web_service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfint;


/**
 * <p>Java class for TranslateArrayResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TranslateArrayResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Error" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="From" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OriginalTextSentenceLengths" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfint" minOccurs="0"/>
 *         &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TranslatedText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TranslatedTextSentenceLengths" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfint" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TranslateArrayResponse", propOrder = {
    "error",
    "from",
    "originalTextSentenceLengths",
    "state",
    "translatedText",
    "translatedTextSentenceLengths"
})
public class TranslateArrayResponse {

    @XmlElementRef(name = "Error", namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", type = JAXBElement.class)
    protected JAXBElement<String> error;
    @XmlElementRef(name = "From", namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", type = JAXBElement.class)
    protected JAXBElement<String> from;
    @XmlElementRef(name = "OriginalTextSentenceLengths", namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", type = JAXBElement.class)
    protected JAXBElement<ArrayOfint> originalTextSentenceLengths;
    @XmlElementRef(name = "State", namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", type = JAXBElement.class)
    protected JAXBElement<String> state;
    @XmlElementRef(name = "TranslatedText", namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", type = JAXBElement.class)
    protected JAXBElement<String> translatedText;
    @XmlElementRef(name = "TranslatedTextSentenceLengths", namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", type = JAXBElement.class)
    protected JAXBElement<ArrayOfint> translatedTextSentenceLengths;

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setError(JAXBElement<String> value) {
        this.error = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the from property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFrom() {
        return from;
    }

    /**
     * Sets the value of the from property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFrom(JAXBElement<String> value) {
        this.from = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the originalTextSentenceLengths property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}
     *     
     */
    public JAXBElement<ArrayOfint> getOriginalTextSentenceLengths() {
        return originalTextSentenceLengths;
    }

    /**
     * Sets the value of the originalTextSentenceLengths property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}
     *     
     */
    public void setOriginalTextSentenceLengths(JAXBElement<ArrayOfint> value) {
        this.originalTextSentenceLengths = ((JAXBElement<ArrayOfint> ) value);
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setState(JAXBElement<String> value) {
        this.state = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the translatedText property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTranslatedText() {
        return translatedText;
    }

    /**
     * Sets the value of the translatedText property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTranslatedText(JAXBElement<String> value) {
        this.translatedText = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the translatedTextSentenceLengths property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}
     *     
     */
    public JAXBElement<ArrayOfint> getTranslatedTextSentenceLengths() {
        return translatedTextSentenceLengths;
    }

    /**
     * Sets the value of the translatedTextSentenceLengths property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}
     *     
     */
    public void setTranslatedTextSentenceLengths(JAXBElement<ArrayOfint> value) {
        this.translatedTextSentenceLengths = ((JAXBElement<ArrayOfint> ) value);
    }

}
