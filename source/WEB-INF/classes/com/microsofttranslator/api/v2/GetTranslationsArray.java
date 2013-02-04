
package com.microsofttranslator.api.v2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring;
import org.datacontract.schemas._2004._07.microsoft_mt_web_service.TranslateOptions;


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
 *         &lt;element name="appId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="texts" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfstring" minOccurs="0"/>
 *         &lt;element name="from" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="to" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="maxTranslations" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="options" type="{http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2}TranslateOptions" minOccurs="0"/>
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
    "appId",
    "texts",
    "from",
    "to",
    "maxTranslations",
    "options"
})
@XmlRootElement(name = "GetTranslationsArray")
public class GetTranslationsArray {

    @XmlElementRef(name = "appId", namespace = "http://api.microsofttranslator.com/V2", type = JAXBElement.class)
    protected JAXBElement<String> appId;
    @XmlElementRef(name = "texts", namespace = "http://api.microsofttranslator.com/V2", type = JAXBElement.class)
    protected JAXBElement<ArrayOfstring> texts;
    @XmlElementRef(name = "from", namespace = "http://api.microsofttranslator.com/V2", type = JAXBElement.class)
    protected JAXBElement<String> from;
    @XmlElementRef(name = "to", namespace = "http://api.microsofttranslator.com/V2", type = JAXBElement.class)
    protected JAXBElement<String> to;
    protected Integer maxTranslations;
    @XmlElementRef(name = "options", namespace = "http://api.microsofttranslator.com/V2", type = JAXBElement.class)
    protected JAXBElement<TranslateOptions> options;

    /**
     * Gets the value of the appId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAppId() {
        return appId;
    }

    /**
     * Sets the value of the appId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAppId(JAXBElement<String> value) {
        this.appId = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the texts property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}
     *     
     */
    public JAXBElement<ArrayOfstring> getTexts() {
        return texts;
    }

    /**
     * Sets the value of the texts property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}
     *     
     */
    public void setTexts(JAXBElement<ArrayOfstring> value) {
        this.texts = ((JAXBElement<ArrayOfstring> ) value);
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
     * Gets the value of the to property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTo() {
        return to;
    }

    /**
     * Sets the value of the to property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTo(JAXBElement<String> value) {
        this.to = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the maxTranslations property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxTranslations() {
        return maxTranslations;
    }

    /**
     * Sets the value of the maxTranslations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxTranslations(Integer value) {
        this.maxTranslations = value;
    }

    /**
     * Gets the value of the options property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link TranslateOptions }{@code >}
     *     
     */
    public JAXBElement<TranslateOptions> getOptions() {
        return options;
    }

    /**
     * Sets the value of the options property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link TranslateOptions }{@code >}
     *     
     */
    public void setOptions(JAXBElement<TranslateOptions> value) {
        this.options = ((JAXBElement<TranslateOptions> ) value);
    }

}
