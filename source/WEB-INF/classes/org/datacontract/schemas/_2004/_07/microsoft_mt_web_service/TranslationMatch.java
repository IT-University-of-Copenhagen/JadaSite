
package org.datacontract.schemas._2004._07.microsoft_mt_web_service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TranslationMatch complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TranslationMatch">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Count" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Error" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MatchDegree" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="MatchedOriginalText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Rating" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TranslatedText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TranslationMatch", propOrder = {
    "count",
    "error",
    "matchDegree",
    "matchedOriginalText",
    "rating",
    "translatedText"
})
public class TranslationMatch {

    @XmlElement(name = "Count")
    protected int count;
    @XmlElementRef(name = "Error", namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", type = JAXBElement.class)
    protected JAXBElement<String> error;
    @XmlElement(name = "MatchDegree")
    protected int matchDegree;
    @XmlElementRef(name = "MatchedOriginalText", namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", type = JAXBElement.class)
    protected JAXBElement<String> matchedOriginalText;
    @XmlElement(name = "Rating")
    protected int rating;
    @XmlElement(name = "TranslatedText", required = true, nillable = true)
    protected String translatedText;

    /**
     * Gets the value of the count property.
     * 
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     * 
     */
    public void setCount(int value) {
        this.count = value;
    }

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
     * Gets the value of the matchDegree property.
     * 
     */
    public int getMatchDegree() {
        return matchDegree;
    }

    /**
     * Sets the value of the matchDegree property.
     * 
     */
    public void setMatchDegree(int value) {
        this.matchDegree = value;
    }

    /**
     * Gets the value of the matchedOriginalText property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMatchedOriginalText() {
        return matchedOriginalText;
    }

    /**
     * Sets the value of the matchedOriginalText property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMatchedOriginalText(JAXBElement<String> value) {
        this.matchedOriginalText = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the rating property.
     * 
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the value of the rating property.
     * 
     */
    public void setRating(int value) {
        this.rating = value;
    }

    /**
     * Gets the value of the translatedText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranslatedText() {
        return translatedText;
    }

    /**
     * Sets the value of the translatedText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranslatedText(String value) {
        this.translatedText = value;
    }

}
