
package org.datacontract.schemas._2004._07.microsoft_mt_web_service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetTranslationsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetTranslationsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="From" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Translations" type="{http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2}ArrayOfTranslationMatch" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetTranslationsResponse", propOrder = {
    "from",
    "state",
    "translations"
})
public class GetTranslationsResponse {

    @XmlElementRef(name = "From", namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", type = JAXBElement.class)
    protected JAXBElement<String> from;
    @XmlElementRef(name = "State", namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", type = JAXBElement.class)
    protected JAXBElement<String> state;
    @XmlElementRef(name = "Translations", namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", type = JAXBElement.class)
    protected JAXBElement<ArrayOfTranslationMatch> translations;

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
     * Gets the value of the translations property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTranslationMatch }{@code >}
     *     
     */
    public JAXBElement<ArrayOfTranslationMatch> getTranslations() {
        return translations;
    }

    /**
     * Sets the value of the translations property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTranslationMatch }{@code >}
     *     
     */
    public void setTranslations(JAXBElement<ArrayOfTranslationMatch> value) {
        this.translations = ((JAXBElement<ArrayOfTranslationMatch> ) value);
    }

}
