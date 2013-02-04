
package org.datacontract.schemas._2004._07.microsoft_mt_web_service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfint;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.datacontract.schemas._2004._07.microsoft_mt_web_service package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Translation_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "Translation");
    private final static QName _GetTranslationsResponse_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "GetTranslationsResponse");
    private final static QName _TranslateArrayResponse_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "TranslateArrayResponse");
    private final static QName _ArrayOfTranslateArrayResponse_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "ArrayOfTranslateArrayResponse");
    private final static QName _TranslateOptions_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "TranslateOptions");
    private final static QName _ArrayOfTranslation_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "ArrayOfTranslation");
    private final static QName _ArrayOfGetTranslationsResponse_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "ArrayOfGetTranslationsResponse");
    private final static QName _TranslationMatch_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "TranslationMatch");
    private final static QName _ArrayOfTranslationMatch_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "ArrayOfTranslationMatch");
    private final static QName _GetTranslationsResponseFrom_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "From");
    private final static QName _GetTranslationsResponseTranslations_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "Translations");
    private final static QName _GetTranslationsResponseState_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "State");
    private final static QName _TranslationMatchError_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "Error");
    private final static QName _TranslationMatchMatchedOriginalText_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "MatchedOriginalText");
    private final static QName _TranslateOptionsReservedFlags_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "ReservedFlags");
    private final static QName _TranslateOptionsContentType_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "ContentType");
    private final static QName _TranslateOptionsUser_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "User");
    private final static QName _TranslateOptionsCategory_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "Category");
    private final static QName _TranslateOptionsUri_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "Uri");
    private final static QName _TranslateArrayResponseTranslatedText_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "TranslatedText");
    private final static QName _TranslateArrayResponseOriginalTextSentenceLengths_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "OriginalTextSentenceLengths");
    private final static QName _TranslateArrayResponseTranslatedTextSentenceLengths_QNAME = new QName("http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", "TranslatedTextSentenceLengths");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.datacontract.schemas._2004._07.microsoft_mt_web_service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetTranslationsResponse }
     * 
     */
    public GetTranslationsResponse createGetTranslationsResponse() {
        return new GetTranslationsResponse();
    }

    /**
     * Create an instance of {@link ArrayOfTranslation }
     * 
     */
    public ArrayOfTranslation createArrayOfTranslation() {
        return new ArrayOfTranslation();
    }

    /**
     * Create an instance of {@link TranslationMatch }
     * 
     */
    public TranslationMatch createTranslationMatch() {
        return new TranslationMatch();
    }

    /**
     * Create an instance of {@link TranslateOptions }
     * 
     */
    public TranslateOptions createTranslateOptions() {
        return new TranslateOptions();
    }

    /**
     * Create an instance of {@link Translation }
     * 
     */
    public Translation createTranslation() {
        return new Translation();
    }

    /**
     * Create an instance of {@link ArrayOfGetTranslationsResponse }
     * 
     */
    public ArrayOfGetTranslationsResponse createArrayOfGetTranslationsResponse() {
        return new ArrayOfGetTranslationsResponse();
    }

    /**
     * Create an instance of {@link ArrayOfTranslationMatch }
     * 
     */
    public ArrayOfTranslationMatch createArrayOfTranslationMatch() {
        return new ArrayOfTranslationMatch();
    }

    /**
     * Create an instance of {@link TranslateArrayResponse }
     * 
     */
    public TranslateArrayResponse createTranslateArrayResponse() {
        return new TranslateArrayResponse();
    }

    /**
     * Create an instance of {@link ArrayOfTranslateArrayResponse }
     * 
     */
    public ArrayOfTranslateArrayResponse createArrayOfTranslateArrayResponse() {
        return new ArrayOfTranslateArrayResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Translation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "Translation")
    public JAXBElement<Translation> createTranslation(Translation value) {
        return new JAXBElement<Translation>(_Translation_QNAME, Translation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTranslationsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "GetTranslationsResponse")
    public JAXBElement<GetTranslationsResponse> createGetTranslationsResponse(GetTranslationsResponse value) {
        return new JAXBElement<GetTranslationsResponse>(_GetTranslationsResponse_QNAME, GetTranslationsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TranslateArrayResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "TranslateArrayResponse")
    public JAXBElement<TranslateArrayResponse> createTranslateArrayResponse(TranslateArrayResponse value) {
        return new JAXBElement<TranslateArrayResponse>(_TranslateArrayResponse_QNAME, TranslateArrayResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTranslateArrayResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "ArrayOfTranslateArrayResponse")
    public JAXBElement<ArrayOfTranslateArrayResponse> createArrayOfTranslateArrayResponse(ArrayOfTranslateArrayResponse value) {
        return new JAXBElement<ArrayOfTranslateArrayResponse>(_ArrayOfTranslateArrayResponse_QNAME, ArrayOfTranslateArrayResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TranslateOptions }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "TranslateOptions")
    public JAXBElement<TranslateOptions> createTranslateOptions(TranslateOptions value) {
        return new JAXBElement<TranslateOptions>(_TranslateOptions_QNAME, TranslateOptions.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTranslation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "ArrayOfTranslation")
    public JAXBElement<ArrayOfTranslation> createArrayOfTranslation(ArrayOfTranslation value) {
        return new JAXBElement<ArrayOfTranslation>(_ArrayOfTranslation_QNAME, ArrayOfTranslation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfGetTranslationsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "ArrayOfGetTranslationsResponse")
    public JAXBElement<ArrayOfGetTranslationsResponse> createArrayOfGetTranslationsResponse(ArrayOfGetTranslationsResponse value) {
        return new JAXBElement<ArrayOfGetTranslationsResponse>(_ArrayOfGetTranslationsResponse_QNAME, ArrayOfGetTranslationsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TranslationMatch }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "TranslationMatch")
    public JAXBElement<TranslationMatch> createTranslationMatch(TranslationMatch value) {
        return new JAXBElement<TranslationMatch>(_TranslationMatch_QNAME, TranslationMatch.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTranslationMatch }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "ArrayOfTranslationMatch")
    public JAXBElement<ArrayOfTranslationMatch> createArrayOfTranslationMatch(ArrayOfTranslationMatch value) {
        return new JAXBElement<ArrayOfTranslationMatch>(_ArrayOfTranslationMatch_QNAME, ArrayOfTranslationMatch.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "From", scope = GetTranslationsResponse.class)
    public JAXBElement<String> createGetTranslationsResponseFrom(String value) {
        return new JAXBElement<String>(_GetTranslationsResponseFrom_QNAME, String.class, GetTranslationsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTranslationMatch }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "Translations", scope = GetTranslationsResponse.class)
    public JAXBElement<ArrayOfTranslationMatch> createGetTranslationsResponseTranslations(ArrayOfTranslationMatch value) {
        return new JAXBElement<ArrayOfTranslationMatch>(_GetTranslationsResponseTranslations_QNAME, ArrayOfTranslationMatch.class, GetTranslationsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "State", scope = GetTranslationsResponse.class)
    public JAXBElement<String> createGetTranslationsResponseState(String value) {
        return new JAXBElement<String>(_GetTranslationsResponseState_QNAME, String.class, GetTranslationsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "Error", scope = TranslationMatch.class)
    public JAXBElement<String> createTranslationMatchError(String value) {
        return new JAXBElement<String>(_TranslationMatchError_QNAME, String.class, TranslationMatch.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "MatchedOriginalText", scope = TranslationMatch.class)
    public JAXBElement<String> createTranslationMatchMatchedOriginalText(String value) {
        return new JAXBElement<String>(_TranslationMatchMatchedOriginalText_QNAME, String.class, TranslationMatch.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "ReservedFlags", scope = TranslateOptions.class)
    public JAXBElement<String> createTranslateOptionsReservedFlags(String value) {
        return new JAXBElement<String>(_TranslateOptionsReservedFlags_QNAME, String.class, TranslateOptions.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "ContentType", scope = TranslateOptions.class)
    public JAXBElement<String> createTranslateOptionsContentType(String value) {
        return new JAXBElement<String>(_TranslateOptionsContentType_QNAME, String.class, TranslateOptions.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "User", scope = TranslateOptions.class)
    public JAXBElement<String> createTranslateOptionsUser(String value) {
        return new JAXBElement<String>(_TranslateOptionsUser_QNAME, String.class, TranslateOptions.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "Category", scope = TranslateOptions.class)
    public JAXBElement<String> createTranslateOptionsCategory(String value) {
        return new JAXBElement<String>(_TranslateOptionsCategory_QNAME, String.class, TranslateOptions.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "State", scope = TranslateOptions.class)
    public JAXBElement<String> createTranslateOptionsState(String value) {
        return new JAXBElement<String>(_GetTranslationsResponseState_QNAME, String.class, TranslateOptions.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "Uri", scope = TranslateOptions.class)
    public JAXBElement<String> createTranslateOptionsUri(String value) {
        return new JAXBElement<String>(_TranslateOptionsUri_QNAME, String.class, TranslateOptions.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "TranslatedText", scope = TranslateArrayResponse.class)
    public JAXBElement<String> createTranslateArrayResponseTranslatedText(String value) {
        return new JAXBElement<String>(_TranslateArrayResponseTranslatedText_QNAME, String.class, TranslateArrayResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "OriginalTextSentenceLengths", scope = TranslateArrayResponse.class)
    public JAXBElement<ArrayOfint> createTranslateArrayResponseOriginalTextSentenceLengths(ArrayOfint value) {
        return new JAXBElement<ArrayOfint>(_TranslateArrayResponseOriginalTextSentenceLengths_QNAME, ArrayOfint.class, TranslateArrayResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "From", scope = TranslateArrayResponse.class)
    public JAXBElement<String> createTranslateArrayResponseFrom(String value) {
        return new JAXBElement<String>(_GetTranslationsResponseFrom_QNAME, String.class, TranslateArrayResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "Error", scope = TranslateArrayResponse.class)
    public JAXBElement<String> createTranslateArrayResponseError(String value) {
        return new JAXBElement<String>(_TranslationMatchError_QNAME, String.class, TranslateArrayResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "State", scope = TranslateArrayResponse.class)
    public JAXBElement<String> createTranslateArrayResponseState(String value) {
        return new JAXBElement<String>(_GetTranslationsResponseState_QNAME, String.class, TranslateArrayResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Microsoft.MT.Web.Service.V2", name = "TranslatedTextSentenceLengths", scope = TranslateArrayResponse.class)
    public JAXBElement<ArrayOfint> createTranslateArrayResponseTranslatedTextSentenceLengths(ArrayOfint value) {
        return new JAXBElement<ArrayOfint>(_TranslateArrayResponseTranslatedTextSentenceLengths_QNAME, ArrayOfint.class, TranslateArrayResponse.class, value);
    }

}
