
package com.microsofttranslator.api.v2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfint;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring;
import org.datacontract.schemas._2004._07.microsoft_mt_web_service.ArrayOfGetTranslationsResponse;
import org.datacontract.schemas._2004._07.microsoft_mt_web_service.ArrayOfTranslateArrayResponse;
import org.datacontract.schemas._2004._07.microsoft_mt_web_service.ArrayOfTranslation;
import org.datacontract.schemas._2004._07.microsoft_mt_web_service.TranslateOptions;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.microsofttranslator.api.v2 package. 
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

    private final static QName _BreakSentencesLanguage_QNAME = new QName("http://api.microsofttranslator.com/V2", "language");
    private final static QName _BreakSentencesAppId_QNAME = new QName("http://api.microsofttranslator.com/V2", "appId");
    private final static QName _BreakSentencesText_QNAME = new QName("http://api.microsofttranslator.com/V2", "text");
    private final static QName _GetLanguagesForSpeakResponseGetLanguagesForSpeakResult_QNAME = new QName("http://api.microsofttranslator.com/V2", "GetLanguagesForSpeakResult");
    private final static QName _SpeakResponseSpeakResult_QNAME = new QName("http://api.microsofttranslator.com/V2", "SpeakResult");
    private final static QName _GetTranslationsArrayResponseGetTranslationsArrayResult_QNAME = new QName("http://api.microsofttranslator.com/V2", "GetTranslationsArrayResult");
    private final static QName _AddTranslationUri_QNAME = new QName("http://api.microsofttranslator.com/V2", "uri");
    private final static QName _AddTranslationUser_QNAME = new QName("http://api.microsofttranslator.com/V2", "user");
    private final static QName _AddTranslationTranslatedText_QNAME = new QName("http://api.microsofttranslator.com/V2", "translatedText");
    private final static QName _AddTranslationContentType_QNAME = new QName("http://api.microsofttranslator.com/V2", "contentType");
    private final static QName _AddTranslationFrom_QNAME = new QName("http://api.microsofttranslator.com/V2", "from");
    private final static QName _AddTranslationOriginalText_QNAME = new QName("http://api.microsofttranslator.com/V2", "originalText");
    private final static QName _AddTranslationCategory_QNAME = new QName("http://api.microsofttranslator.com/V2", "category");
    private final static QName _AddTranslationTo_QNAME = new QName("http://api.microsofttranslator.com/V2", "to");
    private final static QName _TranslateResponseTranslateResult_QNAME = new QName("http://api.microsofttranslator.com/V2", "TranslateResult");
    private final static QName _GetLanguagesForTranslateResponseGetLanguagesForTranslateResult_QNAME = new QName("http://api.microsofttranslator.com/V2", "GetLanguagesForTranslateResult");
    private final static QName _BreakSentencesResponseBreakSentencesResult_QNAME = new QName("http://api.microsofttranslator.com/V2", "BreakSentencesResult");
    private final static QName _GetLanguageNamesResponseGetLanguageNamesResult_QNAME = new QName("http://api.microsofttranslator.com/V2", "GetLanguageNamesResult");
    private final static QName _DetectArrayResponseDetectArrayResult_QNAME = new QName("http://api.microsofttranslator.com/V2", "DetectArrayResult");
    private final static QName _DetectArrayTexts_QNAME = new QName("http://api.microsofttranslator.com/V2", "texts");
    private final static QName _GetLanguageNamesLanguageCodes_QNAME = new QName("http://api.microsofttranslator.com/V2", "languageCodes");
    private final static QName _GetLanguageNamesLocale_QNAME = new QName("http://api.microsofttranslator.com/V2", "locale");
    private final static QName _GetAppIdTokenResponseGetAppIdTokenResult_QNAME = new QName("http://api.microsofttranslator.com/V2", "GetAppIdTokenResult");
    private final static QName _SpeakOptions_QNAME = new QName("http://api.microsofttranslator.com/V2", "options");
    private final static QName _SpeakFormat_QNAME = new QName("http://api.microsofttranslator.com/V2", "format");
    private final static QName _DetectResponseDetectResult_QNAME = new QName("http://api.microsofttranslator.com/V2", "DetectResult");
    private final static QName _TranslateArrayResponseTranslateArrayResult_QNAME = new QName("http://api.microsofttranslator.com/V2", "TranslateArrayResult");
    private final static QName _AddTranslationArrayTranslations_QNAME = new QName("http://api.microsofttranslator.com/V2", "translations");
    private final static QName _GetTranslationsResponseGetTranslationsResult_QNAME = new QName("http://api.microsofttranslator.com/V2", "GetTranslationsResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.microsofttranslator.api.v2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BreakSentences }
     * 
     */
    public BreakSentences createBreakSentences() {
        return new BreakSentences();
    }

    /**
     * Create an instance of {@link GetLanguagesForSpeakResponse }
     * 
     */
    public GetLanguagesForSpeakResponse createGetLanguagesForSpeakResponse() {
        return new GetLanguagesForSpeakResponse();
    }

    /**
     * Create an instance of {@link SpeakResponse }
     * 
     */
    public SpeakResponse createSpeakResponse() {
        return new SpeakResponse();
    }

    /**
     * Create an instance of {@link GetTranslationsArrayResponse }
     * 
     */
    public GetTranslationsArrayResponse createGetTranslationsArrayResponse() {
        return new GetTranslationsArrayResponse();
    }

    /**
     * Create an instance of {@link AddTranslation }
     * 
     */
    public AddTranslation createAddTranslation() {
        return new AddTranslation();
    }

    /**
     * Create an instance of {@link TranslateResponse }
     * 
     */
    public TranslateResponse createTranslateResponse() {
        return new TranslateResponse();
    }

    /**
     * Create an instance of {@link AddTranslationResponse }
     * 
     */
    public AddTranslationResponse createAddTranslationResponse() {
        return new AddTranslationResponse();
    }

    /**
     * Create an instance of {@link GetLanguagesForSpeak }
     * 
     */
    public GetLanguagesForSpeak createGetLanguagesForSpeak() {
        return new GetLanguagesForSpeak();
    }

    /**
     * Create an instance of {@link GetLanguagesForTranslateResponse }
     * 
     */
    public GetLanguagesForTranslateResponse createGetLanguagesForTranslateResponse() {
        return new GetLanguagesForTranslateResponse();
    }

    /**
     * Create an instance of {@link GetLanguagesForTranslate }
     * 
     */
    public GetLanguagesForTranslate createGetLanguagesForTranslate() {
        return new GetLanguagesForTranslate();
    }

    /**
     * Create an instance of {@link BreakSentencesResponse }
     * 
     */
    public BreakSentencesResponse createBreakSentencesResponse() {
        return new BreakSentencesResponse();
    }

    /**
     * Create an instance of {@link GetLanguageNamesResponse }
     * 
     */
    public GetLanguageNamesResponse createGetLanguageNamesResponse() {
        return new GetLanguageNamesResponse();
    }

    /**
     * Create an instance of {@link DetectArrayResponse }
     * 
     */
    public DetectArrayResponse createDetectArrayResponse() {
        return new DetectArrayResponse();
    }

    /**
     * Create an instance of {@link Detect }
     * 
     */
    public Detect createDetect() {
        return new Detect();
    }

    /**
     * Create an instance of {@link DetectArray }
     * 
     */
    public DetectArray createDetectArray() {
        return new DetectArray();
    }

    /**
     * Create an instance of {@link GetLanguageNames }
     * 
     */
    public GetLanguageNames createGetLanguageNames() {
        return new GetLanguageNames();
    }

    /**
     * Create an instance of {@link GetAppIdTokenResponse }
     * 
     */
    public GetAppIdTokenResponse createGetAppIdTokenResponse() {
        return new GetAppIdTokenResponse();
    }

    /**
     * Create an instance of {@link Translate }
     * 
     */
    public Translate createTranslate() {
        return new Translate();
    }

    /**
     * Create an instance of {@link Speak }
     * 
     */
    public Speak createSpeak() {
        return new Speak();
    }

    /**
     * Create an instance of {@link DetectResponse }
     * 
     */
    public DetectResponse createDetectResponse() {
        return new DetectResponse();
    }

    /**
     * Create an instance of {@link TranslateArray }
     * 
     */
    public TranslateArray createTranslateArray() {
        return new TranslateArray();
    }

    /**
     * Create an instance of {@link GetAppIdToken }
     * 
     */
    public GetAppIdToken createGetAppIdToken() {
        return new GetAppIdToken();
    }

    /**
     * Create an instance of {@link TranslateArrayResponse }
     * 
     */
    public TranslateArrayResponse createTranslateArrayResponse() {
        return new TranslateArrayResponse();
    }

    /**
     * Create an instance of {@link AddTranslationArrayResponse }
     * 
     */
    public AddTranslationArrayResponse createAddTranslationArrayResponse() {
        return new AddTranslationArrayResponse();
    }

    /**
     * Create an instance of {@link AddTranslationArray }
     * 
     */
    public AddTranslationArray createAddTranslationArray() {
        return new AddTranslationArray();
    }

    /**
     * Create an instance of {@link com.microsofttranslator.api.v2.GetTranslationsResponse }
     * 
     */
    public com.microsofttranslator.api.v2.GetTranslationsResponse createGetTranslationsResponse() {
        return new com.microsofttranslator.api.v2.GetTranslationsResponse();
    }

    /**
     * Create an instance of {@link GetTranslationsArray }
     * 
     */
    public GetTranslationsArray createGetTranslationsArray() {
        return new GetTranslationsArray();
    }

    /**
     * Create an instance of {@link GetTranslations }
     * 
     */
    public GetTranslations createGetTranslations() {
        return new GetTranslations();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "language", scope = BreakSentences.class)
    public JAXBElement<String> createBreakSentencesLanguage(String value) {
        return new JAXBElement<String>(_BreakSentencesLanguage_QNAME, String.class, BreakSentences.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "appId", scope = BreakSentences.class)
    public JAXBElement<String> createBreakSentencesAppId(String value) {
        return new JAXBElement<String>(_BreakSentencesAppId_QNAME, String.class, BreakSentences.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "text", scope = BreakSentences.class)
    public JAXBElement<String> createBreakSentencesText(String value) {
        return new JAXBElement<String>(_BreakSentencesText_QNAME, String.class, BreakSentences.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "GetLanguagesForSpeakResult", scope = GetLanguagesForSpeakResponse.class)
    public JAXBElement<ArrayOfstring> createGetLanguagesForSpeakResponseGetLanguagesForSpeakResult(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_GetLanguagesForSpeakResponseGetLanguagesForSpeakResult_QNAME, ArrayOfstring.class, GetLanguagesForSpeakResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "SpeakResult", scope = SpeakResponse.class)
    public JAXBElement<String> createSpeakResponseSpeakResult(String value) {
        return new JAXBElement<String>(_SpeakResponseSpeakResult_QNAME, String.class, SpeakResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfGetTranslationsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "GetTranslationsArrayResult", scope = GetTranslationsArrayResponse.class)
    public JAXBElement<ArrayOfGetTranslationsResponse> createGetTranslationsArrayResponseGetTranslationsArrayResult(ArrayOfGetTranslationsResponse value) {
        return new JAXBElement<ArrayOfGetTranslationsResponse>(_GetTranslationsArrayResponseGetTranslationsArrayResult_QNAME, ArrayOfGetTranslationsResponse.class, GetTranslationsArrayResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "uri", scope = AddTranslation.class)
    public JAXBElement<String> createAddTranslationUri(String value) {
        return new JAXBElement<String>(_AddTranslationUri_QNAME, String.class, AddTranslation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "user", scope = AddTranslation.class)
    public JAXBElement<String> createAddTranslationUser(String value) {
        return new JAXBElement<String>(_AddTranslationUser_QNAME, String.class, AddTranslation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "translatedText", scope = AddTranslation.class)
    public JAXBElement<String> createAddTranslationTranslatedText(String value) {
        return new JAXBElement<String>(_AddTranslationTranslatedText_QNAME, String.class, AddTranslation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "contentType", scope = AddTranslation.class)
    public JAXBElement<String> createAddTranslationContentType(String value) {
        return new JAXBElement<String>(_AddTranslationContentType_QNAME, String.class, AddTranslation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "from", scope = AddTranslation.class)
    public JAXBElement<String> createAddTranslationFrom(String value) {
        return new JAXBElement<String>(_AddTranslationFrom_QNAME, String.class, AddTranslation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "originalText", scope = AddTranslation.class)
    public JAXBElement<String> createAddTranslationOriginalText(String value) {
        return new JAXBElement<String>(_AddTranslationOriginalText_QNAME, String.class, AddTranslation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "appId", scope = AddTranslation.class)
    public JAXBElement<String> createAddTranslationAppId(String value) {
        return new JAXBElement<String>(_BreakSentencesAppId_QNAME, String.class, AddTranslation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "category", scope = AddTranslation.class)
    public JAXBElement<String> createAddTranslationCategory(String value) {
        return new JAXBElement<String>(_AddTranslationCategory_QNAME, String.class, AddTranslation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "to", scope = AddTranslation.class)
    public JAXBElement<String> createAddTranslationTo(String value) {
        return new JAXBElement<String>(_AddTranslationTo_QNAME, String.class, AddTranslation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "TranslateResult", scope = TranslateResponse.class)
    public JAXBElement<String> createTranslateResponseTranslateResult(String value) {
        return new JAXBElement<String>(_TranslateResponseTranslateResult_QNAME, String.class, TranslateResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "appId", scope = GetLanguagesForSpeak.class)
    public JAXBElement<String> createGetLanguagesForSpeakAppId(String value) {
        return new JAXBElement<String>(_BreakSentencesAppId_QNAME, String.class, GetLanguagesForSpeak.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "GetLanguagesForTranslateResult", scope = GetLanguagesForTranslateResponse.class)
    public JAXBElement<ArrayOfstring> createGetLanguagesForTranslateResponseGetLanguagesForTranslateResult(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_GetLanguagesForTranslateResponseGetLanguagesForTranslateResult_QNAME, ArrayOfstring.class, GetLanguagesForTranslateResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "appId", scope = GetLanguagesForTranslate.class)
    public JAXBElement<String> createGetLanguagesForTranslateAppId(String value) {
        return new JAXBElement<String>(_BreakSentencesAppId_QNAME, String.class, GetLanguagesForTranslate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "BreakSentencesResult", scope = BreakSentencesResponse.class)
    public JAXBElement<ArrayOfint> createBreakSentencesResponseBreakSentencesResult(ArrayOfint value) {
        return new JAXBElement<ArrayOfint>(_BreakSentencesResponseBreakSentencesResult_QNAME, ArrayOfint.class, BreakSentencesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "GetLanguageNamesResult", scope = GetLanguageNamesResponse.class)
    public JAXBElement<ArrayOfstring> createGetLanguageNamesResponseGetLanguageNamesResult(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_GetLanguageNamesResponseGetLanguageNamesResult_QNAME, ArrayOfstring.class, GetLanguageNamesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "DetectArrayResult", scope = DetectArrayResponse.class)
    public JAXBElement<ArrayOfstring> createDetectArrayResponseDetectArrayResult(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_DetectArrayResponseDetectArrayResult_QNAME, ArrayOfstring.class, DetectArrayResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "appId", scope = Detect.class)
    public JAXBElement<String> createDetectAppId(String value) {
        return new JAXBElement<String>(_BreakSentencesAppId_QNAME, String.class, Detect.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "text", scope = Detect.class)
    public JAXBElement<String> createDetectText(String value) {
        return new JAXBElement<String>(_BreakSentencesText_QNAME, String.class, Detect.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "appId", scope = DetectArray.class)
    public JAXBElement<String> createDetectArrayAppId(String value) {
        return new JAXBElement<String>(_BreakSentencesAppId_QNAME, String.class, DetectArray.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "texts", scope = DetectArray.class)
    public JAXBElement<ArrayOfstring> createDetectArrayTexts(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_DetectArrayTexts_QNAME, ArrayOfstring.class, DetectArray.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "languageCodes", scope = GetLanguageNames.class)
    public JAXBElement<ArrayOfstring> createGetLanguageNamesLanguageCodes(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_GetLanguageNamesLanguageCodes_QNAME, ArrayOfstring.class, GetLanguageNames.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "locale", scope = GetLanguageNames.class)
    public JAXBElement<String> createGetLanguageNamesLocale(String value) {
        return new JAXBElement<String>(_GetLanguageNamesLocale_QNAME, String.class, GetLanguageNames.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "appId", scope = GetLanguageNames.class)
    public JAXBElement<String> createGetLanguageNamesAppId(String value) {
        return new JAXBElement<String>(_BreakSentencesAppId_QNAME, String.class, GetLanguageNames.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "GetAppIdTokenResult", scope = GetAppIdTokenResponse.class)
    public JAXBElement<String> createGetAppIdTokenResponseGetAppIdTokenResult(String value) {
        return new JAXBElement<String>(_GetAppIdTokenResponseGetAppIdTokenResult_QNAME, String.class, GetAppIdTokenResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "contentType", scope = Translate.class)
    public JAXBElement<String> createTranslateContentType(String value) {
        return new JAXBElement<String>(_AddTranslationContentType_QNAME, String.class, Translate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "from", scope = Translate.class)
    public JAXBElement<String> createTranslateFrom(String value) {
        return new JAXBElement<String>(_AddTranslationFrom_QNAME, String.class, Translate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "appId", scope = Translate.class)
    public JAXBElement<String> createTranslateAppId(String value) {
        return new JAXBElement<String>(_BreakSentencesAppId_QNAME, String.class, Translate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "category", scope = Translate.class)
    public JAXBElement<String> createTranslateCategory(String value) {
        return new JAXBElement<String>(_AddTranslationCategory_QNAME, String.class, Translate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "text", scope = Translate.class)
    public JAXBElement<String> createTranslateText(String value) {
        return new JAXBElement<String>(_BreakSentencesText_QNAME, String.class, Translate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "to", scope = Translate.class)
    public JAXBElement<String> createTranslateTo(String value) {
        return new JAXBElement<String>(_AddTranslationTo_QNAME, String.class, Translate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "options", scope = Speak.class)
    public JAXBElement<String> createSpeakOptions(String value) {
        return new JAXBElement<String>(_SpeakOptions_QNAME, String.class, Speak.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "format", scope = Speak.class)
    public JAXBElement<String> createSpeakFormat(String value) {
        return new JAXBElement<String>(_SpeakFormat_QNAME, String.class, Speak.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "language", scope = Speak.class)
    public JAXBElement<String> createSpeakLanguage(String value) {
        return new JAXBElement<String>(_BreakSentencesLanguage_QNAME, String.class, Speak.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "appId", scope = Speak.class)
    public JAXBElement<String> createSpeakAppId(String value) {
        return new JAXBElement<String>(_BreakSentencesAppId_QNAME, String.class, Speak.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "text", scope = Speak.class)
    public JAXBElement<String> createSpeakText(String value) {
        return new JAXBElement<String>(_BreakSentencesText_QNAME, String.class, Speak.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "DetectResult", scope = DetectResponse.class)
    public JAXBElement<String> createDetectResponseDetectResult(String value) {
        return new JAXBElement<String>(_DetectResponseDetectResult_QNAME, String.class, DetectResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TranslateOptions }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "options", scope = TranslateArray.class)
    public JAXBElement<TranslateOptions> createTranslateArrayOptions(TranslateOptions value) {
        return new JAXBElement<TranslateOptions>(_SpeakOptions_QNAME, TranslateOptions.class, TranslateArray.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "from", scope = TranslateArray.class)
    public JAXBElement<String> createTranslateArrayFrom(String value) {
        return new JAXBElement<String>(_AddTranslationFrom_QNAME, String.class, TranslateArray.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "appId", scope = TranslateArray.class)
    public JAXBElement<String> createTranslateArrayAppId(String value) {
        return new JAXBElement<String>(_BreakSentencesAppId_QNAME, String.class, TranslateArray.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "to", scope = TranslateArray.class)
    public JAXBElement<String> createTranslateArrayTo(String value) {
        return new JAXBElement<String>(_AddTranslationTo_QNAME, String.class, TranslateArray.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "texts", scope = TranslateArray.class)
    public JAXBElement<ArrayOfstring> createTranslateArrayTexts(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_DetectArrayTexts_QNAME, ArrayOfstring.class, TranslateArray.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "appId", scope = GetAppIdToken.class)
    public JAXBElement<String> createGetAppIdTokenAppId(String value) {
        return new JAXBElement<String>(_BreakSentencesAppId_QNAME, String.class, GetAppIdToken.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTranslateArrayResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "TranslateArrayResult", scope = TranslateArrayResponse.class)
    public JAXBElement<ArrayOfTranslateArrayResponse> createTranslateArrayResponseTranslateArrayResult(ArrayOfTranslateArrayResponse value) {
        return new JAXBElement<ArrayOfTranslateArrayResponse>(_TranslateArrayResponseTranslateArrayResult_QNAME, ArrayOfTranslateArrayResponse.class, TranslateArrayResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TranslateOptions }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "options", scope = AddTranslationArray.class)
    public JAXBElement<TranslateOptions> createAddTranslationArrayOptions(TranslateOptions value) {
        return new JAXBElement<TranslateOptions>(_SpeakOptions_QNAME, TranslateOptions.class, AddTranslationArray.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "from", scope = AddTranslationArray.class)
    public JAXBElement<String> createAddTranslationArrayFrom(String value) {
        return new JAXBElement<String>(_AddTranslationFrom_QNAME, String.class, AddTranslationArray.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTranslation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "translations", scope = AddTranslationArray.class)
    public JAXBElement<ArrayOfTranslation> createAddTranslationArrayTranslations(ArrayOfTranslation value) {
        return new JAXBElement<ArrayOfTranslation>(_AddTranslationArrayTranslations_QNAME, ArrayOfTranslation.class, AddTranslationArray.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "appId", scope = AddTranslationArray.class)
    public JAXBElement<String> createAddTranslationArrayAppId(String value) {
        return new JAXBElement<String>(_BreakSentencesAppId_QNAME, String.class, AddTranslationArray.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "to", scope = AddTranslationArray.class)
    public JAXBElement<String> createAddTranslationArrayTo(String value) {
        return new JAXBElement<String>(_AddTranslationTo_QNAME, String.class, AddTranslationArray.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.datacontract.schemas._2004._07.microsoft_mt_web_service.GetTranslationsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "GetTranslationsResult", scope = com.microsofttranslator.api.v2.GetTranslationsResponse.class)
    public JAXBElement<org.datacontract.schemas._2004._07.microsoft_mt_web_service.GetTranslationsResponse> createGetTranslationsResponseGetTranslationsResult(org.datacontract.schemas._2004._07.microsoft_mt_web_service.GetTranslationsResponse value) {
        return new JAXBElement<org.datacontract.schemas._2004._07.microsoft_mt_web_service.GetTranslationsResponse>(_GetTranslationsResponseGetTranslationsResult_QNAME, org.datacontract.schemas._2004._07.microsoft_mt_web_service.GetTranslationsResponse.class, com.microsofttranslator.api.v2.GetTranslationsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TranslateOptions }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "options", scope = GetTranslationsArray.class)
    public JAXBElement<TranslateOptions> createGetTranslationsArrayOptions(TranslateOptions value) {
        return new JAXBElement<TranslateOptions>(_SpeakOptions_QNAME, TranslateOptions.class, GetTranslationsArray.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "from", scope = GetTranslationsArray.class)
    public JAXBElement<String> createGetTranslationsArrayFrom(String value) {
        return new JAXBElement<String>(_AddTranslationFrom_QNAME, String.class, GetTranslationsArray.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "appId", scope = GetTranslationsArray.class)
    public JAXBElement<String> createGetTranslationsArrayAppId(String value) {
        return new JAXBElement<String>(_BreakSentencesAppId_QNAME, String.class, GetTranslationsArray.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "to", scope = GetTranslationsArray.class)
    public JAXBElement<String> createGetTranslationsArrayTo(String value) {
        return new JAXBElement<String>(_AddTranslationTo_QNAME, String.class, GetTranslationsArray.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "texts", scope = GetTranslationsArray.class)
    public JAXBElement<ArrayOfstring> createGetTranslationsArrayTexts(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_DetectArrayTexts_QNAME, ArrayOfstring.class, GetTranslationsArray.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TranslateOptions }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "options", scope = GetTranslations.class)
    public JAXBElement<TranslateOptions> createGetTranslationsOptions(TranslateOptions value) {
        return new JAXBElement<TranslateOptions>(_SpeakOptions_QNAME, TranslateOptions.class, GetTranslations.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "from", scope = GetTranslations.class)
    public JAXBElement<String> createGetTranslationsFrom(String value) {
        return new JAXBElement<String>(_AddTranslationFrom_QNAME, String.class, GetTranslations.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "appId", scope = GetTranslations.class)
    public JAXBElement<String> createGetTranslationsAppId(String value) {
        return new JAXBElement<String>(_BreakSentencesAppId_QNAME, String.class, GetTranslations.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "text", scope = GetTranslations.class)
    public JAXBElement<String> createGetTranslationsText(String value) {
        return new JAXBElement<String>(_BreakSentencesText_QNAME, String.class, GetTranslations.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.microsofttranslator.com/V2", name = "to", scope = GetTranslations.class)
    public JAXBElement<String> createGetTranslationsTo(String value) {
        return new JAXBElement<String>(_AddTranslationTo_QNAME, String.class, GetTranslations.class, value);
    }

}
