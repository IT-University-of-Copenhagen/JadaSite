
package org.datacontract.schemas._2004._07.microsoft_mt_web_service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfGetTranslationsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfGetTranslationsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetTranslationsResponse" type="{http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2}GetTranslationsResponse" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfGetTranslationsResponse", propOrder = {
    "getTranslationsResponse"
})
public class ArrayOfGetTranslationsResponse {

    @XmlElement(name = "GetTranslationsResponse", nillable = true)
    protected List<GetTranslationsResponse> getTranslationsResponse;

    /**
     * Gets the value of the getTranslationsResponse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the getTranslationsResponse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGetTranslationsResponse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GetTranslationsResponse }
     * 
     * 
     */
    public List<GetTranslationsResponse> getGetTranslationsResponse() {
        if (getTranslationsResponse == null) {
            getTranslationsResponse = new ArrayList<GetTranslationsResponse>();
        }
        return this.getTranslationsResponse;
    }

}
