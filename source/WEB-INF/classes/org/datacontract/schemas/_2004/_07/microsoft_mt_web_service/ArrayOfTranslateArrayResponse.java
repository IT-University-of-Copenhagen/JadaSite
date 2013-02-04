
package org.datacontract.schemas._2004._07.microsoft_mt_web_service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfTranslateArrayResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfTranslateArrayResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TranslateArrayResponse" type="{http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2}TranslateArrayResponse" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfTranslateArrayResponse", propOrder = {
    "translateArrayResponse"
})
public class ArrayOfTranslateArrayResponse {

    @XmlElement(name = "TranslateArrayResponse", nillable = true)
    protected List<TranslateArrayResponse> translateArrayResponse;

    /**
     * Gets the value of the translateArrayResponse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the translateArrayResponse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTranslateArrayResponse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TranslateArrayResponse }
     * 
     * 
     */
    public List<TranslateArrayResponse> getTranslateArrayResponse() {
        if (translateArrayResponse == null) {
            translateArrayResponse = new ArrayList<TranslateArrayResponse>();
        }
        return this.translateArrayResponse;
    }

}
