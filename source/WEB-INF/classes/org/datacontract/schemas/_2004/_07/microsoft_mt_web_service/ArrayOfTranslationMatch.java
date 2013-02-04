
package org.datacontract.schemas._2004._07.microsoft_mt_web_service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfTranslationMatch complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfTranslationMatch">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TranslationMatch" type="{http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2}TranslationMatch" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfTranslationMatch", propOrder = {
    "translationMatch"
})
public class ArrayOfTranslationMatch {

    @XmlElement(name = "TranslationMatch", nillable = true)
    protected List<TranslationMatch> translationMatch;

    /**
     * Gets the value of the translationMatch property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the translationMatch property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTranslationMatch().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TranslationMatch }
     * 
     * 
     */
    public List<TranslationMatch> getTranslationMatch() {
        if (translationMatch == null) {
            translationMatch = new ArrayList<TranslationMatch>();
        }
        return this.translationMatch;
    }

}
