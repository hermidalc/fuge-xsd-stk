package net.sourceforge.fuge.util;

import net.sourceforge.fuge.util.generatedJAXB2.*;
import net.sourceforge.fuge.util.identification.FuGEIdentifier;
import net.sourceforge.fuge.util.identification.FuGEIdentifierFactory;
import org.xml.sax.SAXException;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import javax.xml.bind.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.GregorianCalendar;

/**
 * GenerateRandomXML is an class that will generate 3 basic XML files that fit with the auto-generated FuGE XSD.
 * You will need to modify this class - or create a new one - if you wish to use it for a community extension you
 * built yourself.
 * <p/>
 * Copyright Notice
 * <p/>
 * The MIT License
 * <p/>
 * Copyright (c) 2008 2007-8 Proteomics Standards Initiative / Microarray and Gene Expression Data Society
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * <p/>
 * Acknowledgements
 * The authors wish to thank the Proteomics Standards Initiative for
 * the provision of infrastructure and expertise in the form of the PSI
 * Document Process that has been used to formalise this document.
 * This file is based on code originally part of SyMBA (http://symba.sourceforge.net).
 * SyMBA is covered under the GNU Lesser General Public License (LGPL).
 * Copyright (C) 2007 jointly held by Allyson Lister, Olly Shaw, and their employers.
 * <p/>
 * $LastChangedDate$
 * $LastChangedRevision$
 * $Author$
 * $HeadURL$
 */
public class RandomXmlGenerator {

    private static int ELEMENT_DEPTH = 1;

    /**
     * Generates the random XML files with the default element depth of 1, and without validating against a schema
     *
     * @param xmlFilename the xml file to write out to
     * @throws JAXBException         if there is a problem creating the xml document
     * @throws SAXException          if there is an error adding the schema to use for validating
     * @throws FileNotFoundException @param xmlFilename the name of the file to write to
     */
    public static void generate( String xmlFilename ) throws JAXBException, SAXException, FileNotFoundException {

        generate( null, xmlFilename );

    }

    /**
     * Generates the random XML files with the provided element depth, and without validating against a schema
     *
     * @param xmlFilename the xml file to write out to
     * @param depth       the number of elements to make for each aspect of the XML file (i.e. how deep to go).
     * @throws JAXBException         if there is a problem creating the xml document
     * @throws SAXException          if there is an error adding the schema to use for validating
     * @throws FileNotFoundException @param xmlFilename the name of the file to write to
     */
    public static void generate( String xmlFilename, int depth ) throws
            JAXBException,
            SAXException,
            FileNotFoundException {

        ELEMENT_DEPTH = depth;
        generate( null, xmlFilename );

    }

    /**
     * Generates the random XML files with the provided element depth.
     *
     * @param xmlFilename    the xml file to write out to
     * @param depth          the number of elements to make for each aspect of the XML file (i.e. how deep to go).
     * @param schemaFilename the schema to validate against
     * @throws JAXBException         if there is a problem creating the xml document
     * @throws SAXException          if there is an error adding the schema to use for validating
     * @throws FileNotFoundException @param xmlFilename the name of the file to write to
     */
    public static void generate( String schemaFilename, String xmlFilename, int depth ) throws
            JAXBException,
            SAXException,
            FileNotFoundException {

        ELEMENT_DEPTH = depth;
        generate( schemaFilename, xmlFilename );

    }


    /**
     * Generates the random XML files. Defaults to a element depth of 1.
     *
     * @param xmlFilename    the xml file to write out to
     * @param schemaFilename the schema to validate against
     * @throws JAXBException         if there is a problem creating the xml document
     * @throws SAXException          if there is an error adding the schema to use for validating
     * @throws FileNotFoundException @param xmlFilename the name of the file to write to
     */
    public static void generate( String schemaFilename, String xmlFilename ) throws
            JAXBException,
            SAXException,
            FileNotFoundException {

        FuGECollectionFuGEType rootXML;

        OutputStream os;

        System.err.println( "Schema file is: " + schemaFilename );
        System.err.println( "File for XML output is: " + xmlFilename );

        // create a JAXBContext capable of handling classes generated into the net.sourceforge.fuge.util.generatedJAXB2
        // package
        JAXBContext jc = JAXBContext.newInstance( "net.sourceforge.fuge.util.generatedJAXB2" );

        System.err.println( "JAXB Context created." );

        // create a Marshaller
        Marshaller m = jc.createMarshaller();
        m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
        m.setEventHandler(
                new ValidationEventHandler() {
                    public boolean handleEvent( ValidationEvent e ) {
                        System.out.println( "Validation Error: " + e.getMessage() );
                        return false;
                    }
                } );

        if ( schemaFilename != null ) {
            // set the correct schema
            SchemaFactory sf = SchemaFactory.newInstance( W3C_XML_SCHEMA_NS_URI );
            Schema schema = sf.newSchema( new File( schemaFilename ) );
            m.setSchema( schema );
        }

        System.err.println( "Marshaller initialized." );

        // create a jaxb root object
        System.err.println( "Starting generation..." );

        rootXML = generateRandomFuGEXML();

        os = new FileOutputStream( xmlFilename );
        @SuppressWarnings( "unchecked" )
        JAXBElement element = new JAXBElement(
                new QName( "http://fuge.sourceforge.net/fuge/1.0", "FuGE" ),
                FuGECollectionFuGEType.class,
                rootXML );
        m.marshal( element, os );

        System.err.println( "Generation complete." );

    }

    /**
     * Public container class for all of the random generation methods
     *
     * @return the fuge jaxb2 object containing the randomly-generated information
     */
    private static FuGECollectionFuGEType generateRandomFuGEXML() {

        FuGECollectionFuGEType fugeXML = new FuGECollectionFuGEType();

        // generate identifiable traits
        fugeXML = ( FuGECollectionFuGEType ) generateRandomIdentifiableXML( fugeXML );

        // generate AuditCollection information
        if ( fugeXML.getAuditCollection() == null ) {
            fugeXML = generateRandomAuditCollectionXML( fugeXML );
        }

        // generate OntologyCollection information
        if ( fugeXML.getOntologyCollection() == null ) {
            fugeXML = generateRandomOntologyCollectionXML( fugeXML );
        }

        // generate ReferenceableCollection information
        if ( fugeXML.getReferenceableCollection() == null ) {
            fugeXML = generateRandomReferenceableCollectionXML( fugeXML );
        }

        // Get all MaterialCollection information
        if ( fugeXML.getMaterialCollection() == null ) {
            fugeXML = generateRandomMaterialCollectionXML( fugeXML );
        }

        // Get all data collection information - MUST BE DONE before Protocol and after Material
        if ( fugeXML.getDataCollection() == null ) {
            fugeXML = generateRandomDataCollectionXML( fugeXML );
        }

        // Get all protocol collection information
        if ( fugeXML.getProtocolCollection() == null ) {
            // marshall the fuge object into a jaxb object
            fugeXML = generateRandomProtocolCollectionXML( fugeXML );
        }

        // Get all Provider information
        if ( fugeXML.getProvider() == null ) {
            // marshall the fuge object into a jaxb object
            fugeXML = generateRandomProviderXML( fugeXML );
        }

        // Get an Investigation, if present
        if ( fugeXML.getInvestigationCollection() == null ) {
            // unmarshall the jaxb object into a fuge object, then set the fuge object within the top level fuge root object
            fugeXML = generateRandomInvestigationCollectionXML( fugeXML );
        }

        return fugeXML;
    }

    /**
     * Generates a random JAXB2 InvestigationCollection element and adds it to the rootXML JAXB2 object
     *
     * @param fugeXML the object to add the investigation collection to
     * @return the modified fuge object, having the newly-created investigation collection within it
     */
    private static FuGECollectionFuGEType generateRandomInvestigationCollectionXML( FuGECollectionFuGEType fugeXML ) {

        FuGECollectionInvestigationCollectionType investigationCollectionXML =
                new FuGECollectionInvestigationCollectionType();
        investigationCollectionXML = ( FuGECollectionInvestigationCollectionType ) generateRandomDescribableXML(
                investigationCollectionXML );

        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            FuGEBioInvestigationInvestigationType investigationXML = new FuGEBioInvestigationInvestigationType();

            investigationXML =
                    ( FuGEBioInvestigationInvestigationType ) generateRandomIdentifiableXML( investigationXML );

            // top-level hypothesis
            FuGEBioInvestigationInvestigationType.Hypothesis hypothesisXML =
                    new FuGEBioInvestigationInvestigationType.Hypothesis();
            FuGECommonDescriptionDescriptionType descriptionXML = new FuGECommonDescriptionDescriptionType();
            descriptionXML.setText( "Some Hypothesis " + String.valueOf( Math.random() ) );
            hypothesisXML.setDescription( descriptionXML );
            investigationXML.setHypothesis( hypothesisXML );

            // top-level conclusion
            FuGEBioInvestigationInvestigationType.Conclusion conclusionXML =
                    new FuGEBioInvestigationInvestigationType.Conclusion();
            descriptionXML = new FuGECommonDescriptionDescriptionType();
            descriptionXML.setText( "Some Conclusion " + String.valueOf( Math.random() ) );
            conclusionXML.setDescription( descriptionXML );
            investigationXML.setConclusion( conclusionXML );

            investigationCollectionXML.getInvestigation().add( investigationXML );
            FuGEBioInvestigationFactorType factorXML = new FuGEBioInvestigationFactorType();

            factorXML = ( FuGEBioInvestigationFactorType ) generateRandomIdentifiableXML( factorXML );

            // set the non-identifiable traits

            if ( fugeXML.getOntologyCollection() != null ) {
                FuGEBioInvestigationFactorType.FactorType categoryValueXML =
                        new FuGEBioInvestigationFactorType.FactorType();
                categoryValueXML.setOntologyTermRef(
                        fugeXML.getOntologyCollection().getOntologyTerm().get( i ).getValue().getIdentifier() );
                factorXML.setFactorType( categoryValueXML );
            }

            ObjectFactory factory = new ObjectFactory();
            for ( int ii = 0; ii < ELEMENT_DEPTH; ii++ ) {
                FuGEBioInvestigationFactorValueType factorValueXML = new FuGEBioInvestigationFactorValueType();
                factorValueXML = ( FuGEBioInvestigationFactorValueType ) generateRandomDescribableXML( factorValueXML );
                if ( fugeXML.getOntologyCollection() != null ) {
                    FuGECommonMeasurementBooleanValueType valueXML = ( FuGECommonMeasurementBooleanValueType )
                            generateRandomMeasurementXML( new FuGECommonMeasurementBooleanValueType(), fugeXML );
                    factorValueXML.setMeasurement( factory.createBooleanValue( valueXML ) );
                }

// todo still not sure where datapartitions fit in, so won't make them for now.
//                for ( int iii = 0; iii < ELEMENT_DEPTH; iii++ ) {
//                    FuGEBioInvestigationFactorValueType.DataPartitions dataPartitionXML = new FuGEBioInvestigationFactorValueType.DataPartitions();
//                    if ( fugeXML.getDataCollection() != null ) {
//                        dataPartitionXML.setDataPartitionRef( fugeXML.getDataCollection().getData().get( iii ).getValue().getIdentifier() );
//                        // todo not sure how to set the dimension element here. can't get it from dataPartitionXML
//                        factorValueXML.getDataPartitions().add( dataPartitionXML );
//                    }
//                }
                factorXML.getFactorValue().add( factorValueXML );
            }
            investigationCollectionXML.getFactor().add( factorXML );
        }

        fugeXML.setInvestigationCollection( investigationCollectionXML );
        return fugeXML;

    }

    /**
     * Generates a random JAXB2 Provider element and adds it to the rootXML JAXB2 object
     *
     * @param fugeXML the object to get the software reference from, and add the provider to
     * @return the modified fuge object, having the newly-created provider within it
     */
    private static FuGECollectionFuGEType generateRandomProviderXML( FuGECollectionFuGEType fugeXML ) {
        // create fuge object
        FuGECollectionProviderType providerXML = new FuGECollectionProviderType();

        providerXML = ( FuGECollectionProviderType ) generateRandomIdentifiableXML( providerXML );

        providerXML.setContactRole( generateRandomContactRoleXML( fugeXML ) );

        if ( fugeXML.getProtocolCollection() != null ) {
            providerXML.setSoftwareRef(
                    fugeXML.getProtocolCollection().getSoftware().get( 0 ).getValue().getIdentifier() );
        }
        fugeXML.setProvider( providerXML );
        return fugeXML;
    }

    /**
     * Generates a random JAXB2 ProtocolCollection and adds it to the rootXML JAXB2 object
     *
     * @param fugeXML the object to add the protocol collection to
     * @return the modified fuge object, having the newly-created protocol collection within it
     */
    private static FuGECollectionFuGEType generateRandomProtocolCollectionXML( FuGECollectionFuGEType fugeXML ) {
        FuGECollectionProtocolCollectionType protocolCollectionXML = new FuGECollectionProtocolCollectionType();

        protocolCollectionXML = ( FuGECollectionProtocolCollectionType ) generateRandomDescribableXML(
                protocolCollectionXML );

        if ( protocolCollectionXML.getEquipment().isEmpty() ) {
            // equipment
            protocolCollectionXML = generateRandomEquipmentXML( protocolCollectionXML, fugeXML );
        }

        if ( protocolCollectionXML.getSoftware().isEmpty() ) {
            // software
            for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
                FuGECommonProtocolGenericSoftwareType genericSoftwareXML = new FuGECommonProtocolGenericSoftwareType();
                genericSoftwareXML = ( FuGECommonProtocolGenericSoftwareType ) generateRandomSoftwareXML(
                        genericSoftwareXML, protocolCollectionXML, fugeXML );
                JAXBElement<? extends FuGECommonProtocolGenericSoftwareType> element = ( new ObjectFactory() )
                        .createGenericSoftware( genericSoftwareXML );
                protocolCollectionXML.getSoftware().add( element );
            }
        }

        if ( protocolCollectionXML.getProtocol().isEmpty() ) {
            // protocol
            for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
                FuGECommonProtocolGenericProtocolType genericProtocolXML = new FuGECommonProtocolGenericProtocolType();
                genericProtocolXML = ( FuGECommonProtocolGenericProtocolType ) generateRandomProtocolXML(
                        genericProtocolXML, protocolCollectionXML, fugeXML );
                JAXBElement<? extends FuGECommonProtocolGenericProtocolType> element = ( new ObjectFactory() )
                        .createGenericProtocol( genericProtocolXML );
                protocolCollectionXML.getProtocol().add( element );
            }
        }

        if ( protocolCollectionXML.getProtocolApplication().isEmpty() ) {
            // protocol application
            for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
                JAXBElement<? extends FuGECommonProtocolGenericProtocolApplicationType> element =
                        ( new ObjectFactory() )
                                .createGenericProtocolApplication(
                                        ( FuGECommonProtocolGenericProtocolApplicationType ) generateRandomProtocolApplicationXML(
                                                i, protocolCollectionXML, fugeXML ) );
                protocolCollectionXML.getProtocolApplication().add( element );
            }
        }

        fugeXML.setProtocolCollection( protocolCollectionXML );
        return fugeXML;
    }

    /**
     * At this stage, rootXML may not have the new equipment and software - the protocol collection may be the only one to have it
     *
     * @param protocolXML           the JAXB2 object that is returned with attributes filled
     * @param protocolCollectionXML required to check values from
     * @param fugeXML               the object to get various bits of information to populate the protocol with
     * @return a random JAXB2 Protocol
     */
    private static FuGECommonProtocolProtocolType generateRandomProtocolXML( FuGECommonProtocolProtocolType protocolXML,
                                                                             FuGECollectionProtocolCollectionType protocolCollectionXML,
                                                                             FuGECollectionFuGEType fugeXML ) {
        FuGECommonProtocolGenericProtocolType genericProtocolXML =
                ( FuGECommonProtocolGenericProtocolType ) protocolXML;

        // get protocol attributes
        genericProtocolXML =
                ( FuGECommonProtocolGenericProtocolType ) generateRandomIdentifiableXML( genericProtocolXML );

        genericProtocolXML = ( FuGECommonProtocolGenericProtocolType ) generateRandomParameterizableXML(
                genericProtocolXML, fugeXML );

        if ( fugeXML.getOntologyCollection() != null ) {
            // input types
            for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
                FuGECommonProtocolProtocolType.InputTypes inputTypesXML =
                        new FuGECommonProtocolProtocolType.InputTypes();
                inputTypesXML.setOntologyTermRef(
                        fugeXML.getOntologyCollection().getOntologyTerm().get( i ).getValue().getIdentifier() );
                genericProtocolXML.getInputTypes().add( inputTypesXML );
            }

            // output types
            for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
                FuGECommonProtocolProtocolType.OutputTypes outputTypesXML =
                        new FuGECommonProtocolProtocolType.OutputTypes();
                outputTypesXML.setOntologyTermRef(
                        fugeXML.getOntologyCollection().getOntologyTerm().get( i ).getValue().getIdentifier() );
                genericProtocolXML.getOutputTypes().add( outputTypesXML );
            }
        }

        genericProtocolXML = generateRandomGenericProtocolXML( genericProtocolXML, protocolCollectionXML, fugeXML );
        return genericProtocolXML;
    }

    /**
     * @param genericProtocolXML    the JAXB2 object that is returned with attributes filled
     * @param protocolCollectionXML needed for the information contained within it to make the JAXB2 object correctly
     * @param fugeXML               the object to get information from
     * @return a random JAXB2 GenericProtocol
     */
    private static FuGECommonProtocolGenericProtocolType generateRandomGenericProtocolXML(
            FuGECommonProtocolGenericProtocolType genericProtocolXML,
            FuGECollectionProtocolCollectionType protocolCollectionXML, FuGECollectionFuGEType fugeXML ) {

        ObjectFactory factory = new ObjectFactory();

        // can only have generic actions.
        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            genericProtocolXML.getAction().add(
                    factory.createGenericAction( generateRandomActionXML(
                            new FuGECommonProtocolGenericActionType(), i, protocolCollectionXML, fugeXML ) ) );
        }
        // can only have generic parameters
        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            genericProtocolXML.getGenericParameter().add(
                    ( FuGECommonProtocolGenericParameterType )
                            generateRandomParameterXML( new FuGECommonProtocolGenericParameterType(), fugeXML ) );
        }

        if ( protocolCollectionXML != null ) {
            // protocol to equipment
            for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
                FuGECommonProtocolGenericProtocolType.Equipment equip =
                        new FuGECommonProtocolGenericProtocolType.Equipment();
                equip.setGenericEquipmentRef(
                        protocolCollectionXML.getEquipment().get( i ).getValue().getIdentifier() );
                genericProtocolXML.getEquipment().add( equip );
            }
            // software
            for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
                FuGECommonProtocolGenericProtocolType.Software genSoftware =
                        new FuGECommonProtocolGenericProtocolType.Software();
                genSoftware.setGenericSoftwareRef(
                        protocolCollectionXML.getSoftware().get( i ).getValue().getIdentifier() );
                genericProtocolXML.getSoftware().add( genSoftware );
            }
        }
        return genericProtocolXML;
    }

    /**
     * @param actionXML             the JAXB2 object that is returned with attributes filled
     * @param ordinal               the position in an exterior loop controlling how many elements are generated
     * @param protocolCollectionXML needed for the information contained within it to make the JAXB2 object correctly
     * @param fugeXML               the object to get some information from
     * @return a random JAXB2 Action
     */
    private static FuGECommonProtocolGenericActionType generateRandomActionXML( FuGECommonProtocolActionType actionXML,
                                                                                int ordinal,
                                                                                FuGECollectionProtocolCollectionType protocolCollectionXML,
                                                                                FuGECollectionFuGEType fugeXML ) {

        FuGECommonProtocolGenericActionType genericActionXML = ( FuGECommonProtocolGenericActionType ) actionXML;

        // get action attributes
        genericActionXML = ( FuGECommonProtocolGenericActionType ) generateRandomIdentifiableXML( genericActionXML );

        // action ordinal
        genericActionXML.setActionOrdinal( ordinal );

        // get generic action attributes
        genericActionXML = generateRandomGenericActionXML( genericActionXML, protocolCollectionXML, fugeXML );

        return genericActionXML;

    }

    /**
     * @param genericActionXML      the JAXB2 object that is returned with attributes filled
     * @param protocolCollectionXML needed for the information contained within it to make the JAXB2 object correctly
     * @param fugeXML               the object to get some information from
     * @return a random JAXB2 GenericAction
     */
    private static FuGECommonProtocolGenericActionType generateRandomGenericActionXML(
            FuGECommonProtocolGenericActionType genericActionXML,
            FuGECollectionProtocolCollectionType protocolCollectionXML,
            FuGECollectionFuGEType fugeXML ) {

        genericActionXML.setActionText( String.valueOf( Math.random() ) );

        // action term
        if ( fugeXML.getOntologyCollection() != null ) {
            FuGECommonProtocolGenericActionType.ActionTerm aterm = new FuGECommonProtocolGenericActionType.ActionTerm();
            aterm.setOntologyTermRef(
                    fugeXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
            genericActionXML.setActionTerm( aterm );
        }

        // protocol ref
        if ( protocolCollectionXML.getProtocol().size() > 0 ) {
            genericActionXML.setProtocolRef( protocolCollectionXML.getProtocol().get( 0 ).getValue().getIdentifier() );
        }

        // you can only have a GenericParameter here
        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            genericActionXML.getGenericParameter().add(
                    ( FuGECommonProtocolGenericParameterType ) generateRandomParameterXML(
                            new FuGECommonProtocolGenericParameterType(), fugeXML ) );
        }

        return genericActionXML;
    }

    /**
     * at this stage, rootXML may not have the new equipment and software - the protocol collection may be the only one to have it
     *
     * @param ordinal               the position in an exterior loop controlling how many elements are generated
     * @param protocolCollectionXML needed for the information contained within it to make the JAXB2 object correctly
     * @param fugeXML               the object to get some information from
     * @return a random JAXB2 ProtocolApplication
     */
    private static FuGECommonProtocolProtocolApplicationType generateRandomProtocolApplicationXML( int ordinal,
                                                                                                   FuGECollectionProtocolCollectionType protocolCollectionXML,
                                                                                                   FuGECollectionFuGEType fugeXML ) {
        FuGECommonProtocolGenericProtocolApplicationType genericProtocolApplicationXML =
                new FuGECommonProtocolGenericProtocolApplicationType();

        // get protocol application attributes
        genericProtocolApplicationXML =
                ( FuGECommonProtocolGenericProtocolApplicationType ) generateRandomIdentifiableXML(
                        genericProtocolApplicationXML );
        genericProtocolApplicationXML =
                ( FuGECommonProtocolGenericProtocolApplicationType ) generateRandomParameterizableApplicationXML(
                        genericProtocolApplicationXML, protocolCollectionXML, fugeXML );

        try {
            genericProtocolApplicationXML.setActivityDate(
                    DatatypeFactory.newInstance().newXMLGregorianCalendar( new GregorianCalendar() ) );
        } catch ( DatatypeConfigurationException e ) {
            throw new RuntimeException( "Error creating XMLGregorianCalendar from current date", e );
        }
        if ( !protocolCollectionXML.getSoftware().isEmpty() ) {
            FuGECommonProtocolSoftwareApplicationType type = new FuGECommonProtocolSoftwareApplicationType();
            type = ( FuGECommonProtocolSoftwareApplicationType ) generateRandomIdentifiableXML( type );
            type.setSoftwareRef( protocolCollectionXML.getSoftware().get( ordinal ).getValue().getIdentifier() );
            genericProtocolApplicationXML.getSoftwareApplication().add( type );
        }
        if ( !protocolCollectionXML.getEquipment().isEmpty() ) {
            FuGECommonProtocolEquipmentApplicationType type = new FuGECommonProtocolEquipmentApplicationType();
            type = ( FuGECommonProtocolEquipmentApplicationType ) generateRandomIdentifiableXML( type );
            type.setEquipmentRef( protocolCollectionXML.getEquipment().get( ordinal ).getValue().getIdentifier() );
            genericProtocolApplicationXML.getEquipmentApplication().add( type );
        }
        if ( !protocolCollectionXML.getProtocol().isEmpty() ) {
            FuGECommonProtocolActionApplicationType type = new FuGECommonProtocolActionApplicationType();
            type = ( FuGECommonProtocolActionApplicationType ) generateRandomIdentifiableXML( type );

            FuGECommonProtocolGenericProtocolType gpType =
                    ( FuGECommonProtocolGenericProtocolType ) protocolCollectionXML
                            .getProtocol().get( ordinal ).getValue();
            type.setActionRef( gpType.getAction().get( ordinal ).getValue().getIdentifier() );
            FuGECommonDescriptionDescriptionType descXML = new FuGECommonDescriptionDescriptionType();
            descXML = ( FuGECommonDescriptionDescriptionType ) generateRandomDescribableXML( descXML );
            descXML.setText( String.valueOf( Math.random() ) );
            FuGECommonProtocolActionApplicationType.ActionDeviation deviation =
                    new FuGECommonProtocolActionApplicationType.ActionDeviation();
            deviation.setDescription( descXML );

            type.setActionDeviation( deviation );
            genericProtocolApplicationXML.getActionApplication().add( type );

            // ensure we only add one of these, by only adding when the ordinal is exactly 1. This isn't perfect,
            // but it's hard to ensure that we only ever have 0 or 1 links between action application and its
            // child protocol application
            if ( ordinal == 1 ) {
                type.setProtocolApplicationRef(
                        protocolCollectionXML.getProtocolApplication().get( 0 ).getValue().getIdentifier() );
            }
        }

        FuGECommonProtocolProtocolApplicationType.ProtocolDeviation pdXML =
                new FuGECommonProtocolProtocolApplicationType.ProtocolDeviation();
        FuGECommonDescriptionDescriptionType descXML = new FuGECommonDescriptionDescriptionType();
        descXML = ( FuGECommonDescriptionDescriptionType ) generateRandomDescribableXML( descXML );
        descXML.setText( String.valueOf( Math.random() ) );
        pdXML.setDescription( descXML );
        genericProtocolApplicationXML.setProtocolDeviation( pdXML );

        genericProtocolApplicationXML = generateRandomGenericProtocolApplicationXML( genericProtocolApplicationXML,
                ordinal, protocolCollectionXML, fugeXML );
        return genericProtocolApplicationXML;
    }

    /**
     * @param genericProtocolApplicationXML the JAXB2 object that is returned with attributes filled
     * @param ordinal                       the position in an exterior loop controlling how many elements are generated
     * @param protocolCollectionXML         needed for the information contained within it to make the JAXB2 object correctly
     * @param fugeXML                       the object to get some information from for populating the GPA
     * @return a random JAXB2 GenericProtocolApplication
     */
    private static FuGECommonProtocolGenericProtocolApplicationType generateRandomGenericProtocolApplicationXML(
            FuGECommonProtocolGenericProtocolApplicationType genericProtocolApplicationXML,
            int ordinal,
            FuGECollectionProtocolCollectionType protocolCollectionXML, FuGECollectionFuGEType fugeXML ) {

        if ( !protocolCollectionXML.getProtocol().isEmpty() ) {
            genericProtocolApplicationXML.setProtocolRef(
                    protocolCollectionXML.getProtocol().get( ordinal ).getValue().getIdentifier() );
        }

        int output = ordinal + 1;
        if ( ordinal == ELEMENT_DEPTH - 1 )
            output = 0;

        if ( fugeXML.getDataCollection() != null ) {
            // input data
            FuGECommonProtocolGenericProtocolApplicationType.InputData gidXML =
                    new FuGECommonProtocolGenericProtocolApplicationType.InputData();
            gidXML.setDataRef( fugeXML.getDataCollection().getData().get( ordinal ).getValue().getIdentifier() );
            genericProtocolApplicationXML.getInputData().add( gidXML );

            // output data
            FuGECommonProtocolGenericProtocolApplicationType.OutputData godXML =
                    new FuGECommonProtocolGenericProtocolApplicationType.OutputData();
            godXML.setDataRef( fugeXML.getDataCollection().getData().get( output ).getValue().getIdentifier() );
            genericProtocolApplicationXML.getOutputData().add( godXML );
        }

        if ( fugeXML.getMaterialCollection() != null ) {
            // input complete material
            FuGECommonProtocolGenericProtocolApplicationType.InputCompleteMaterials icmXML =
                    new FuGECommonProtocolGenericProtocolApplicationType.InputCompleteMaterials();
            icmXML.setMaterialRef(
                    fugeXML.getMaterialCollection().getMaterial().get( ordinal ).getValue().getIdentifier() );
            genericProtocolApplicationXML.getInputCompleteMaterials().add( icmXML );

            // input material
            FuGEBioMaterialGenericMaterialMeasurementType gmmXML = new FuGEBioMaterialGenericMaterialMeasurementType();
            gmmXML.setMaterialRef(
                    fugeXML.getMaterialCollection().getMaterial().get( ordinal ).getValue().getIdentifier() );
            ObjectFactory factory = new ObjectFactory();
            FuGECommonMeasurementRangeType valueXML = ( FuGECommonMeasurementRangeType )
                    generateRandomMeasurementXML( new FuGECommonMeasurementRangeType(), fugeXML );
            gmmXML.setMeasurement( factory.createRange( valueXML ) );
            genericProtocolApplicationXML.getGenericMaterialMeasurement().add( gmmXML );

            // output material
            FuGECommonProtocolGenericProtocolApplicationType.OutputMaterials gomXML =
                    new FuGECommonProtocolGenericProtocolApplicationType.OutputMaterials();
            gomXML.setMaterialRef(
                    fugeXML.getMaterialCollection().getMaterial().get( ordinal ).getValue().getIdentifier() );
            genericProtocolApplicationXML.getOutputMaterials().add( gomXML );
        }

        return genericProtocolApplicationXML;
    }

    /**
     * @param parameterizableApplicationXML the JAXB2 object that is returned with attributes filled
     * @param protocolCollectionXML         needed for the information contained within it to make the JAXB2 object correctly
     * @param fugeXML                       the object to get some information from for populating the parameterizable application
     * @return a random JAXB2 ParameterizableApplication
     */
    private static FuGECommonProtocolParameterizableApplicationType generateRandomParameterizableApplicationXML(
            FuGECommonProtocolParameterizableApplicationType parameterizableApplicationXML,
            FuGECollectionProtocolCollectionType protocolCollectionXML, FuGECollectionFuGEType fugeXML ) {

        parameterizableApplicationXML =
                ( FuGECommonProtocolParameterizableApplicationType ) generateRandomIdentifiableXML(
                        parameterizableApplicationXML );

        if ( !protocolCollectionXML.getEquipment().isEmpty() ) {
            FuGECommonProtocolGenericEquipmentType eqXML =
                    ( FuGECommonProtocolGenericEquipmentType ) protocolCollectionXML
                            .getEquipment()
                            .get( 0 )
                            .getValue();
            for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
                FuGECommonProtocolParameterValueType pvalueXML = new FuGECommonProtocolParameterValueType();
                pvalueXML = ( FuGECommonProtocolParameterValueType ) generateRandomDescribableXML( pvalueXML );
                pvalueXML.setParameterRef( eqXML.getGenericParameter().get( 0 ).getIdentifier() );
                pvalueXML.setMeasurement( ( new ObjectFactory() ).createAtomicValue(
                        ( FuGECommonMeasurementAtomicValueType ) generateRandomMeasurementXML(
                                new FuGECommonMeasurementAtomicValueType(), fugeXML ) ) );
                parameterizableApplicationXML.getParameterValue().add( pvalueXML );
            }
        }
        return parameterizableApplicationXML;
    }

    /**
     * this method is different from the others in that it will generate ALL equipment
     * in one go, rather than just one piece of equipment. This is because software may not
     * have been made yet, and so this method needs protocolCollection changeable so that it can add
     * software if necessary.
     *
     * @param protocolCollectionXML the JAXB2 object that is returned with attributes filled
     * @param fugeXML               the object to get some information from for populating the equipment
     * @return a random JAXB2 ProtocolCollection
     */
    private static FuGECollectionProtocolCollectionType generateRandomEquipmentXML(
            FuGECollectionProtocolCollectionType protocolCollectionXML, FuGECollectionFuGEType fugeXML ) {

        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            FuGECommonProtocolGenericEquipmentType genEquipmentXML = new FuGECommonProtocolGenericEquipmentType();

            genEquipmentXML =
                    ( FuGECommonProtocolGenericEquipmentType ) generateRandomIdentifiableXML( genEquipmentXML );


            genEquipmentXML = ( FuGECommonProtocolGenericEquipmentType ) generateRandomParameterizableXML(
                    genEquipmentXML, fugeXML );

            if ( fugeXML.getOntologyCollection() != null ) {
                FuGECommonProtocolEquipmentType.Make make = new FuGECommonProtocolEquipmentType.Make();
                make.setOntologyTermRef(
                        fugeXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
                genEquipmentXML.setMake( make );

                FuGECommonProtocolEquipmentType.Model model = new FuGECommonProtocolEquipmentType.Model();
                model.setOntologyTermRef(
                        fugeXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
                genEquipmentXML.setModel( model );
            }

            // software required for generic equipment attributes
            if ( protocolCollectionXML.getSoftware() == null ) {
                for ( int ii = 0; ii < ELEMENT_DEPTH; ii++ ) {
                    FuGECommonProtocolGenericSoftwareType genericSoftwareXML =
                            new FuGECommonProtocolGenericSoftwareType();
                    genericSoftwareXML = ( FuGECommonProtocolGenericSoftwareType ) generateRandomSoftwareXML(
                            genericSoftwareXML, protocolCollectionXML, fugeXML );
                    JAXBElement<? extends FuGECommonProtocolGenericSoftwareType> element =
                            ( new ObjectFactory() ).createGenericSoftware(
                                    genericSoftwareXML );
                    protocolCollectionXML.getSoftware().add( element );
                }
            }
            // get generic equipment attributes
            if ( i > 0 ) {
                genEquipmentXML = generateRandomGenericEquipmentXML(
                        genEquipmentXML,
                        ( FuGECommonProtocolGenericEquipmentType ) protocolCollectionXML.getEquipment()
                                .get( 0 )
                                .getValue(),
                        fugeXML );
            } else {
                genEquipmentXML = generateRandomGenericEquipmentXML( genEquipmentXML, null, fugeXML );
            }

            JAXBElement<? extends FuGECommonProtocolGenericEquipmentType> element =
                    ( new ObjectFactory() ).createGenericEquipment(
                            genEquipmentXML );
            protocolCollectionXML.getEquipment().add( element );
        }
        return protocolCollectionXML;

    }

    /**
     * @param genericEquipmentXML the JAXB2 object that is returned with attributes filled
     * @param partXML             the parts list for this equipment
     * @param fugeXML             the object to get some information from for populating the generic equipment
     * @return a random JAXB2 GenericEquipment
     */
    private static FuGECommonProtocolGenericEquipmentType generateRandomGenericEquipmentXML(
            FuGECommonProtocolGenericEquipmentType genericEquipmentXML,
            FuGECommonProtocolGenericEquipmentType partXML, FuGECollectionFuGEType fugeXML ) {

        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            FuGECommonProtocolGenericParameterType parameterXML = new FuGECommonProtocolGenericParameterType();
            genericEquipmentXML.getGenericParameter()
                    .add( ( FuGECommonProtocolGenericParameterType ) generateRandomParameterXML( parameterXML,
                            fugeXML ) );
        }

        if ( partXML != null ) {
            // parts list of one
            FuGECommonProtocolGenericEquipmentType.EquipmentParts parts =
                    new FuGECommonProtocolGenericEquipmentType.EquipmentParts();
            parts.setGenericEquipmentRef( partXML.getIdentifier() );
            genericEquipmentXML.getEquipmentParts().add( parts );
        }

        if ( fugeXML.getProtocolCollection() != null ) {
            for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
                FuGECommonProtocolGenericEquipmentType.Software softwareXML =
                        new FuGECommonProtocolGenericEquipmentType.Software();
                softwareXML.setGenericSoftwareRef(
                        fugeXML.getProtocolCollection().getSoftware().get( i ).getValue().getIdentifier() );
                genericEquipmentXML.getSoftware().add( softwareXML );
            }
        }
        return genericEquipmentXML;

    }

    /**
     * at this stage, rootXML may not have the new equipment and software - the protocol collection may be the only one to have it
     *
     * @param softwareXML           the JAXB2 object that is returned with attributes filled
     * @param protocolCollectionXML needed for the information contained within it to make the JAXB2 object correctly
     * @param fugeXML               the object to get some information from for populating the software
     * @return a random JAXB2 Software Object
     */
    private static FuGECommonProtocolSoftwareType generateRandomSoftwareXML( FuGECommonProtocolSoftwareType softwareXML,
                                                                             FuGECollectionProtocolCollectionType protocolCollectionXML,
                                                                             FuGECollectionFuGEType fugeXML ) {
        // make software attributes

        if ( softwareXML instanceof FuGECommonProtocolGenericSoftwareType ) {
            FuGECommonProtocolGenericSoftwareType genericSoftwareXML =
                    ( FuGECommonProtocolGenericSoftwareType ) softwareXML;
            genericSoftwareXML = ( FuGECommonProtocolGenericSoftwareType ) generateRandomIdentifiableXML(
                    genericSoftwareXML );
            genericSoftwareXML = ( FuGECommonProtocolGenericSoftwareType ) generateRandomParameterizableXML(
                    genericSoftwareXML, fugeXML );

            genericSoftwareXML.setVersion( String.valueOf( Math.random() ) );

            // get generic software attributes
            genericSoftwareXML = generateRandomGenericSoftwareXML( genericSoftwareXML, protocolCollectionXML, fugeXML );

            return genericSoftwareXML;
        }

        return softwareXML;

    }

    /**
     * @param genericSoftwareXML    the JAXB2 object that is returned with attributes filled
     * @param protocolCollectionXML needed for the information contained within it to make the JAXB2 object correctly
     * @param fugeXML               the object to get some information from for populating the software
     * @return a random JAXB2 GenericSoftware
     */
    private static FuGECommonProtocolGenericSoftwareType generateRandomGenericSoftwareXML(
            FuGECommonProtocolGenericSoftwareType genericSoftwareXML,
            FuGECollectionProtocolCollectionType protocolCollectionXML, FuGECollectionFuGEType fugeXML ) {

        // you can only have a GenericParameter here
        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            genericSoftwareXML.getGenericParameter().add(
                    ( FuGECommonProtocolGenericParameterType ) generateRandomParameterXML(
                            new FuGECommonProtocolGenericParameterType(), fugeXML ) );
        }

        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            FuGECommonProtocolGenericSoftwareType.Equipment parts =
                    new FuGECommonProtocolGenericSoftwareType.Equipment();
            if ( !protocolCollectionXML.getEquipment().isEmpty() && protocolCollectionXML.getEquipment().size() > i ) {
                parts.setGenericEquipmentRef(
                        protocolCollectionXML.getEquipment().get( i ).getValue().getIdentifier() );
                genericSoftwareXML.getEquipment().add( parts );
            }
        }
        return genericSoftwareXML;

    }

    /**
     * @param parameterXML the JAXB2 object that is returned with attributes filled
     * @param fugeXML      the object to get some information from for populating the parameter
     * @return a random JAXB2 Parameter
     */
    private static FuGECommonProtocolParameterType generateRandomParameterXML( FuGECommonProtocolParameterType parameterXML,
                                                                               FuGECollectionFuGEType fugeXML ) {

        // get parameter attributes
        parameterXML = ( FuGECommonProtocolParameterType ) generateRandomIdentifiableXML( parameterXML );

        parameterXML.setIsInputParam( true );

        // measurement
        FuGECommonMeasurementComplexValueType measurementXML =
                ( FuGECommonMeasurementComplexValueType ) generateRandomMeasurementXML(
                        new FuGECommonMeasurementComplexValueType(), fugeXML );
        parameterXML.setMeasurement( ( new ObjectFactory() ).createComplexValue( measurementXML ) );

        // get generic parameter attributes
        parameterXML =
                generateRandomGenericParameterXML( ( FuGECommonProtocolGenericParameterType ) parameterXML, fugeXML );
        return parameterXML;

    }

    /**
     * @param genericParameterXML the JAXB2 object that is returned with attributes filled
     * @param fugeXML             the object to get some information from for populating the parameter
     * @return a random JAXB2 GenericParameter
     */
    private static FuGECommonProtocolGenericParameterType generateRandomGenericParameterXML(
            FuGECommonProtocolGenericParameterType genericParameterXML, FuGECollectionFuGEType fugeXML ) {

        if ( fugeXML.getOntologyCollection() != null ) {
            FuGECommonProtocolGenericParameterType.ParameterType ptXML =
                    new FuGECommonProtocolGenericParameterType.ParameterType();
            ptXML.setOntologyTermRef(
                    fugeXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
            genericParameterXML.setParameterType( ptXML );
        }
        return genericParameterXML;
    }

    /**
     * todo deal with propertySets
     *
     * @param defaultXML the measurement object to populate with random information
     * @param fugeXML    the object to get some information from for populating the measurement
     * @return an AtomicValue Measurement wrapped in a JAXB2 element
     */
    private static FuGECommonMeasurementMeasurementType generateRandomMeasurementXML(
            FuGECommonMeasurementMeasurementType defaultXML,
            FuGECollectionFuGEType fugeXML ) {

        defaultXML = ( FuGECommonMeasurementMeasurementType ) generateRandomDescribableXML( defaultXML );

        // add a unit and a data type
        if ( fugeXML.getOntologyCollection().getOntologyTerm().size() >= 2 ) {
            FuGECommonMeasurementMeasurementType.Unit unitXML = new FuGECommonMeasurementMeasurementType.Unit();
            unitXML.setOntologyTermRef(
                    fugeXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
            defaultXML.setUnit( unitXML );

            FuGECommonMeasurementMeasurementType.DataType dataTypeXML =
                    new FuGECommonMeasurementMeasurementType.DataType();
            dataTypeXML.setOntologyTermRef(
                    fugeXML.getOntologyCollection().getOntologyTerm().get( 1 ).getValue().getIdentifier() );
            defaultXML.setDataType( dataTypeXML );
        }

        if ( defaultXML instanceof FuGECommonMeasurementAtomicValueType ) {
            // get atomic value attributes
            defaultXML = generateRandomAtomicValueXML( ( FuGECommonMeasurementAtomicValueType ) defaultXML );
            return ( defaultXML );
        } else if ( defaultXML instanceof FuGECommonMeasurementBooleanValueType ) {
            // get boolean value attributes
            defaultXML = generateRandomBooleanValueXML( ( FuGECommonMeasurementBooleanValueType ) defaultXML );
            return ( defaultXML );
        }
        if ( defaultXML instanceof FuGECommonMeasurementComplexValueType ) {
            // get complex value attributes
            defaultXML = generateRandomComplexValueXML( ( FuGECommonMeasurementComplexValueType ) defaultXML, fugeXML );
            return ( defaultXML );
        } else if ( defaultXML instanceof FuGECommonMeasurementRangeType ) {
            // get Range attributes
            // todo:implement with the proper random xml, with ranges and ontology references.
            defaultXML = generateRandomRangeXML( ( FuGECommonMeasurementRangeType ) defaultXML );
            return ( defaultXML );
        }

        return defaultXML;

    }

    private static FuGECommonMeasurementRangeType generateRandomRangeXML( FuGECommonMeasurementRangeType valueXML ) {
        valueXML.setLowerLimit( "some lower limit" );
        valueXML.setUpperLimit( "some upper limit" );
        return valueXML;
    }

    private static FuGECommonMeasurementComplexValueType generateRandomComplexValueXML(
            FuGECommonMeasurementComplexValueType valueXML,
            FuGECollectionFuGEType fugeXML ) {
        if ( fugeXML.getOntologyCollection() != null ) {
            FuGECommonMeasurementComplexValueType.Value defaultValueXML =
                    new FuGECommonMeasurementComplexValueType.Value();
            defaultValueXML.setOntologyTermRef(
                    fugeXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
            valueXML.setValue( defaultValueXML );
        }
        return valueXML;
    }

    private static FuGECommonMeasurementBooleanValueType generateRandomBooleanValueXML(
            FuGECommonMeasurementBooleanValueType valueXML ) {
        valueXML.setValue( true );
        return valueXML;
    }

    private static FuGECommonMeasurementAtomicValueType generateRandomAtomicValueXML(
            FuGECommonMeasurementAtomicValueType valueXML ) {
        valueXML.setValue( "5" );
        return valueXML;
    }

    /**
     * @param parameterizableXML the JAXB2 object that is returned with attributes filled
     * @param fugeXML            the object to get some information from for populating the parameterizable object
     * @return a random JAXB2 Parameterizable
     */
    private static FuGECommonProtocolParameterizableType generateRandomParameterizableXML(
            FuGECommonProtocolParameterizableType parameterizableXML, FuGECollectionFuGEType fugeXML ) {

        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            parameterizableXML.getContactRole().add( generateRandomContactRoleXML( fugeXML ) );
        }

        if ( fugeXML.getOntologyCollection() != null ) {
            for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
                FuGECommonProtocolParameterizableType.Types parameterizableTypesXML =
                        new FuGECommonProtocolParameterizableType.Types();
                parameterizableTypesXML.setOntologyTermRef(
                        fugeXML.getOntologyCollection().getOntologyTerm().get( i ).getValue().getIdentifier() );
                parameterizableXML.getTypes().add( parameterizableTypesXML );
            }
        }
        return parameterizableXML;
    }

    /**
     * todo deal with InternalData
     * <p/>
     * Generates a random JAXB2 DataCollection element and adds it to the rootXML JAXB2 object
     *
     * @param fugeXML the object to add the data collection to
     * @return the modified fuge object
     */
    private static FuGECollectionFuGEType generateRandomDataCollectionXML( FuGECollectionFuGEType fugeXML ) {

        // create the jaxb Data collection object
        FuGECollectionDataCollectionType datCollXML = new FuGECollectionDataCollectionType();

        // set describable information
        datCollXML = ( FuGECollectionDataCollectionType ) generateRandomDescribableXML( datCollXML );

        ObjectFactory factory = new ObjectFactory();

        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {

            // As RawData objects do not appear in the XML, there is no need to code it here.
            datCollXML.getData().add(
                    factory.createExternalData(
                            ( FuGEBioDataExternalDataType ) generateRandomDataXML(
                                    new FuGEBioDataExternalDataType(), fugeXML ) ) );
        }

        fugeXML.setDataCollection( datCollXML );
        return fugeXML;
    }

    /**
     * @param dataXML the JAXB2 object that is returned with attributes filled
     * @param fugeXML the object to get some information from for populating the data
     * @return a random JAXB2 Data object
     */
    private static FuGEBioDataDataType generateRandomDataXML( FuGEBioDataDataType dataXML,
                                                              FuGECollectionFuGEType fugeXML ) {

        FuGEBioDataExternalDataType externalDataXML = ( FuGEBioDataExternalDataType ) dataXML;

        // set the data attributes
        externalDataXML = ( FuGEBioDataExternalDataType ) generateRandomIdentifiableXML( externalDataXML );

        // set the externaldata attributes
        externalDataXML = generateRandomExternalDataXML( externalDataXML, fugeXML );

        return externalDataXML;

    }

    /**
     * Creates a random JAXB2 ExternalData object
     *
     * @param externalDataXML the JAXB2 object that is returned with attributes filled
     * @param fugeXML         the object to get some information from for populating the external data
     * @return a random JAXB2 ExternalData object
     */
    private static FuGEBioDataExternalDataType generateRandomExternalDataXML( FuGEBioDataExternalDataType externalDataXML,
                                                                              FuGECollectionFuGEType fugeXML ) {

        // Location
        externalDataXML.setLocation( "http://some.random.url/" + String.valueOf( Math.random() ) );

        // external format documentation
        FuGEBioDataExternalDataType.ExternalFormatDocumentation efdXML =
                new FuGEBioDataExternalDataType.ExternalFormatDocumentation();
        FuGECommonDescriptionURIType uriXML = new FuGECommonDescriptionURIType();

        // set jaxb object
        uriXML = ( FuGECommonDescriptionURIType ) generateRandomDescribableXML( uriXML );
        uriXML.setLocation( "http://some.sortof.string/" + String.valueOf( Math.random() ) );

        // load jaxb object into describableXML
        efdXML.setURI( uriXML );
        externalDataXML.setExternalFormatDocumentation( efdXML );

        // FileFormat
        if ( fugeXML.getOntologyCollection() != null ) {
            FuGEBioDataExternalDataType.FileFormat fileformatXML = new FuGEBioDataExternalDataType.FileFormat();
            fileformatXML.setOntologyTermRef(
                    fugeXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
            externalDataXML.setFileFormat( fileformatXML );
        }
        return externalDataXML;
    }

    /**
     * Generates a random JAXB2 MaterialCollection element and adds it to the rootXML JAXB2 object
     *
     * @param fugeXML the object to fill with random materials
     * @return the modified fuge object with the material collection added
     */
    private static FuGECollectionFuGEType generateRandomMaterialCollectionXML( FuGECollectionFuGEType fugeXML ) {

        // create the jaxb material collection object
        FuGECollectionMaterialCollectionType matCollXML = new FuGECollectionMaterialCollectionType();

        // set describable information
        matCollXML = ( FuGECollectionMaterialCollectionType ) generateRandomDescribableXML( matCollXML );

        // set up the converter and the factory
        ObjectFactory factory = new ObjectFactory();

        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            // set jaxb object
            if ( i > 0 ) {
                matCollXML.getMaterial().add(
                        factory.createGenericMaterial(
                                ( FuGEBioMaterialGenericMaterialType ) generateRandomMaterialXML(
                                        fugeXML, ( FuGEBioMaterialGenericMaterialType ) matCollXML.getMaterial()
                                        .get( 0 )
                                        .getValue() ) ) );
            } else {
                matCollXML.getMaterial().add(
                        factory.createGenericMaterial(
                                ( FuGEBioMaterialGenericMaterialType ) generateRandomMaterialXML(
                                        fugeXML, null ) ) );
            }
        }

        fugeXML.setMaterialCollection( matCollXML );
        return fugeXML;

    }

    /**
     * Creates a random JAXB2 Material
     *
     * @param fugeXML the object to get some information from for populating the material
     * @param genXML  the JAXB2 object that is returned with attributes filled @return a random JAXB2 Material
     * @return the randomly-generated material type
     */
    private static FuGEBioMaterialMaterialType generateRandomMaterialXML( FuGECollectionFuGEType fugeXML,
                                                                          FuGEBioMaterialGenericMaterialType genXML ) {
        // create fuge object
        FuGEBioMaterialGenericMaterialType genericMaterialXML = new FuGEBioMaterialGenericMaterialType();

        // set the material attributes
        genericMaterialXML = ( FuGEBioMaterialGenericMaterialType ) generateRandomSpecificXML(
                genericMaterialXML, fugeXML );

        // set the generic material attributes
        genericMaterialXML = generateRandomGenericMaterialXML( genericMaterialXML, genXML );

        return genericMaterialXML;
    }

    /**
     * Creates a random JAXB2 GenericMaterial
     *
     * @param genericMaterialXML the JAXB2 object that is returned with attributes filled
     * @param componentXML       the Material component that is a component of genericMaterialXML
     * @return a random JAXB2 GenericMaterial
     */
    private static FuGEBioMaterialGenericMaterialType generateRandomGenericMaterialXML(
            FuGEBioMaterialGenericMaterialType genericMaterialXML,
            FuGEBioMaterialGenericMaterialType componentXML ) {
        // Components. These elements are references to GenericMaterial. Only generate one reference.
        if ( componentXML != null ) {
            FuGEBioMaterialGenericMaterialType.Components componentsXML =
                    new FuGEBioMaterialGenericMaterialType.Components();
            componentsXML.setGenericMaterialRef( componentXML.getIdentifier() );
            genericMaterialXML.getComponents().add( componentsXML );
        }
        return genericMaterialXML;
    }


    /**
     * This should be run at a time where the ontology collection and audit collection have already been run.
     *
     * @param materialXML the JAXB2 object that is returned with attributes filled
     * @param fugeXML     the object to get some information from for populating the material
     * @return the passed Material JAXB2 object with the attributes filled that are specific to the abstract Material class
     */
    private static FuGEBioMaterialMaterialType generateRandomSpecificXML( FuGEBioMaterialMaterialType materialXML,
                                                                          FuGECollectionFuGEType fugeXML ) {
        materialXML = ( FuGEBioMaterialMaterialType ) generateRandomIdentifiableXML( materialXML );

        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            materialXML.getContactRole().add( generateRandomContactRoleXML( fugeXML ) );
        }

        if ( fugeXML.getOntologyCollection() != null ) {
            FuGEBioMaterialMaterialType.MaterialType materialTypeXML = new FuGEBioMaterialMaterialType.MaterialType();
            materialTypeXML.setOntologyTermRef(
                    fugeXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
            materialXML.setMaterialType( materialTypeXML );

            for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
                FuGEBioMaterialMaterialType.Characteristics characteristicXML =
                        new FuGEBioMaterialMaterialType.Characteristics();
                characteristicXML.setOntologyTermRef(
                        fugeXML.getOntologyCollection().getOntologyTerm().get( i ).getValue().getIdentifier() );
                materialXML.getCharacteristics().add( characteristicXML );
            }

            for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
                FuGEBioMaterialMaterialType.QualityControlStatistics qcsXML =
                        new FuGEBioMaterialMaterialType.QualityControlStatistics();
                qcsXML.setOntologyTermRef(
                        fugeXML.getOntologyCollection().getOntologyTerm().get( i ).getValue().getIdentifier() );
                materialXML.getQualityControlStatistics().add( qcsXML );
            }
        }
        return materialXML;
    }

    /**
     * @param identifiableXML the JAXB2 object that is returned with attributes filled
     * @return a randomly-filled Identifiable JAXB2 object
     */
    private static FuGECommonIdentifiableType generateRandomIdentifiableXML( FuGECommonIdentifiableType identifiableXML ) {

        FuGEIdentifier identifierMaker = FuGEIdentifierFactory.createFuGEIdentifier( null, null );

        identifiableXML = ( FuGECommonIdentifiableType ) generateRandomDescribableXML( identifiableXML );
        identifiableXML.setIdentifier( identifierMaker.create( "random.class.name" ) );
        identifiableXML.setName( String.valueOf( Math.random() ) );

        // this ensures that if smaller objects (like DatabaseReference) are being created, there is no unneccessary attempt
        //  to create sub-objects, and additionally there will be no infinite recursion
        if ( identifiableXML instanceof FuGECollectionFuGEType ) {
            FuGECollectionFuGEType fugeXML = ( FuGECollectionFuGEType ) identifiableXML;
            for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
                FuGECommonReferencesDatabaseReferenceType DatabaseReferenceXML =
                        new FuGECommonReferencesDatabaseReferenceType();
                DatabaseReferenceXML = ( FuGECommonReferencesDatabaseReferenceType )
                        generateRandomDescribableXML( DatabaseReferenceXML );
                DatabaseReferenceXML.setAccession( String.valueOf( Math.random() ) );
                DatabaseReferenceXML.setAccessionVersion( String.valueOf( Math.random() ) );

                // This is a reference to another object, so create that object before setting the reference
                if ( fugeXML.getReferenceableCollection() == null ) {
                    fugeXML = generateRandomReferenceableCollectionXML( fugeXML );
                }
                // get the first object and make it what is referred.
                DatabaseReferenceXML
                        .setDatabaseRef( fugeXML.getReferenceableCollection().getDatabase().get( i ).getIdentifier() );
                fugeXML.getDatabaseReference().add( DatabaseReferenceXML );
            }

            for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
                FuGECommonIdentifiableType.BibliographicReferences brRefXML =
                        new FuGECommonIdentifiableType.BibliographicReferences();
                // This is a reference to another object, so create that object before setting the reference
                if ( fugeXML.getReferenceableCollection() == null ) {
                    fugeXML = generateRandomReferenceableCollectionXML( fugeXML );
                }
                // get the first object and make it what is referred.
                brRefXML.setBibliographicReferenceRef(
                        fugeXML.getReferenceableCollection().getBibliographicReference().get( i ).getIdentifier() );
                fugeXML.getBibliographicReferences().add( brRefXML );
            }
            return fugeXML;
        }
        return identifiableXML;
    }

    /**
     * Generates a random JAXB2 ReferenceableCollection element and adds it to the rootXML JAXB2 object
     *
     * @param fugeXML the object to which the referenceable collection is added
     * @return the modified fuge object
     */
    private static FuGECollectionFuGEType generateRandomReferenceableCollectionXML( FuGECollectionFuGEType fugeXML ) {

        FuGECollectionReferenceableCollectionType refCollXML = new FuGECollectionReferenceableCollectionType();

        refCollXML = ( FuGECollectionReferenceableCollectionType ) generateRandomDescribableXML( refCollXML );

        refCollXML = ( FuGECollectionReferenceableCollectionType ) generateRandomDescribableXML( refCollXML );

        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            FuGECommonReferencesBibliographicReferenceType bibRefXML =
                    new FuGECommonReferencesBibliographicReferenceType();
            bibRefXML = ( FuGECommonReferencesBibliographicReferenceType ) generateRandomIdentifiableXML( bibRefXML );

            bibRefXML.setAuthors( String.valueOf( Math.random() ) );
            bibRefXML.setEditor( String.valueOf( Math.random() ) );
            bibRefXML.setIssue( String.valueOf( Math.random() ) );
            bibRefXML.setPages( String.valueOf( Math.random() ) );
            bibRefXML.setPublication( String.valueOf( Math.random() ) );
            bibRefXML.setPublisher( String.valueOf( Math.random() ) );
            bibRefXML.setTitle( String.valueOf( Math.random() ) );
            bibRefXML.setVolume( String.valueOf( Math.random() ) );
            bibRefXML.setYear(
                    ( int ) ( Math.random() *
                              1000 ) ); // int will not be null

            refCollXML.getBibliographicReference().add( bibRefXML );
        }
        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            FuGECommonReferencesDatabaseType dbXML = new FuGECommonReferencesDatabaseType();

            dbXML = ( FuGECommonReferencesDatabaseType ) generateRandomIdentifiableXML( dbXML );
            dbXML.setURI( String.valueOf( Math.random() ) );
            dbXML.setVersion( String.valueOf( Math.random() ) );
            for ( int ii = 0; ii < ELEMENT_DEPTH; ii++ ) {
                if ( fugeXML.getAuditCollection() == null ) {
                    fugeXML = generateRandomAuditCollectionXML( fugeXML );
                }
                if ( fugeXML.getOntologyCollection() == null ) {
                    fugeXML = generateRandomOntologyCollectionXML( fugeXML );
                }
                dbXML.getContactRole().add( generateRandomContactRoleXML( fugeXML ) );
            }
            refCollXML.getDatabase().add( dbXML );
        }
        fugeXML.setReferenceableCollection( refCollXML );

        return fugeXML;
    }

    /**
     * this method is a litte different from the other generateRandomXML, in that it needs to return
     * a contactRole type, so all creation of audit and ontology terms must have already happened outside
     * this method.
     *
     * @param fugeXML the fuge object to get an example audit person from
     * @return a randomly-generated ContactRole JAXB2 object
     */
    private static FuGECommonAuditContactRoleType generateRandomContactRoleXML( FuGECollectionFuGEType fugeXML ) {

        FuGECommonAuditContactRoleType contactRoleXML = new FuGECommonAuditContactRoleType();
        contactRoleXML = ( FuGECommonAuditContactRoleType ) generateRandomDescribableXML( contactRoleXML );

        contactRoleXML.setContactRef( fugeXML.getAuditCollection().getContact().get( 0 ).getValue().getIdentifier() );

        FuGECommonAuditContactRoleType.Role roleXML = new FuGECommonAuditContactRoleType.Role();
        roleXML.setOntologyTermRef(
                fugeXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
        contactRoleXML.setRole( roleXML );

        return contactRoleXML;
    }

    /**
     * specifically for generating random values for use in testing. Only FuGE objects will get the full
     * generated XML, as this prevents infinite recursion.
     *
     * @param describableXML the JAXB2 object that is returned with attributes filled
     * @return a randomly-generated Describable JAXB2 object
     */
    private static FuGECommonDescribableType generateRandomDescribableXML( FuGECommonDescribableType describableXML ) {

        // at the moment there is nothing outside the class check if-statement.

        // this ensures that if smaller objects (like DatabaseEntry) are being created, there is no unneccessary attempt
        //  to create sub-objects, and additionally there will be no infinite recursion

        // create jaxb object
        FuGECommonDescribableType.AuditTrail auditsXML = new FuGECommonDescribableType.AuditTrail();

        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            // create jaxb object
            FuGECommonAuditAuditType auditXML = new FuGECommonAuditAuditType();

            // set jaxb object
            if ( describableXML instanceof FuGECollectionFuGEType )
                auditXML = ( FuGECommonAuditAuditType ) generateRandomDescribableXML( auditXML );

            // in addition to the standard describables, it also has date, action and contact ref, of which
            // the first two are required.
            try {
                auditXML.setDate( DatatypeFactory.newInstance().newXMLGregorianCalendar( new GregorianCalendar() ) );
            } catch ( DatatypeConfigurationException e ) {
                throw new RuntimeException( "Error creating new date for random xml generation", e );
            }

            // @todo options are hardcoded: is this really the only/best way?
            auditXML.setAction( "creation" );
            if ( describableXML instanceof FuGECollectionFuGEType ) {
                FuGECollectionFuGEType fugeXML = ( FuGECollectionFuGEType ) describableXML;
                if ( fugeXML.getAuditCollection() == null ) {
                    fugeXML = generateRandomAuditCollectionXML( fugeXML );
                }
                auditXML.setContactRef(
                        fugeXML.getAuditCollection().getContact().get( i ).getValue().getIdentifier() );
                describableXML = fugeXML;
            }

            // add to collection
            auditsXML.getAudit().add( auditXML );
        }

        // load jaxb object into fugeXML
        describableXML.setAuditTrail( auditsXML );

        // create fuge object for 0 or 1 descriptions (optional), which contain 1 to many Description elements.

        // create jaxb objects
        FuGECommonDescribableType.Descriptions descriptionsXML = new FuGECommonDescribableType.Descriptions();

        // set jaxb object
        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {

            // create singular jaxb object
            FuGECommonDescriptionDescriptionType descriptionXML = new FuGECommonDescriptionDescriptionType();

            // set jaxb object
            if ( describableXML instanceof FuGECollectionFuGEType )
                descriptionXML =
                        ( FuGECommonDescriptionDescriptionType ) generateRandomDescribableXML( descriptionXML );
            descriptionXML.setText( String.valueOf( Math.random() ) );

            // add to collection of objects
            descriptionsXML.getDescription().add( descriptionXML );
        }
        // load jaxb object into fugeXML
        describableXML.setDescriptions( descriptionsXML );

        // create fuge object for any number of annotations (optional), which contains one required OntologyTerm_ref
        if ( describableXML instanceof FuGECollectionFuGEType ) {
            FuGECollectionFuGEType fugeXML = ( FuGECollectionFuGEType ) describableXML;
            for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
                FuGECommonDescribableType.Annotations annotationXML = new FuGECommonDescribableType.Annotations();
                if ( fugeXML.getOntologyCollection() == null ) {
                    fugeXML = generateRandomOntologyCollectionXML( fugeXML );
                }
                annotationXML.setOntologyTermRef(
                        fugeXML.getOntologyCollection().getOntologyTerm().get( i ).getValue().getIdentifier() );
                fugeXML.getAnnotations().add( annotationXML );
                describableXML = fugeXML;
            }
        }

        FuGECommonDescribableType.Uri uriElementXML = new FuGECommonDescribableType.Uri();
        FuGECommonDescriptionURIType uriXML = new FuGECommonDescriptionURIType();

        // add describable information to the URI only if it is a fuge object to prevent lots of recursion
        // (URIs have URIs have URIs...)
        if ( describableXML instanceof FuGECollectionFuGEType )
            uriXML = ( FuGECommonDescriptionURIType ) generateRandomDescribableXML( uriXML );
        uriXML.setLocation( "http://some.random.url/" + String.valueOf( Math.random() ) );

        // load jaxb object into fugeXML
        uriElementXML.setURI( uriXML );
        describableXML.setUri( uriElementXML );

        // create fuge object for 0 or 1 propertySets, which contain at least 1 NameValueType element
        // create jaxb objects
        FuGECommonDescribableType.PropertySets propertySetsXML = new FuGECommonDescribableType.PropertySets();

        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            // create singular jaxb object
            FuGECommonDescriptionNameValueTypeType nameValueTypeXML = new FuGECommonDescriptionNameValueTypeType();

            // set jaxb object
            if ( describableXML instanceof FuGECollectionFuGEType )
                nameValueTypeXML =
                        ( FuGECommonDescriptionNameValueTypeType ) generateRandomDescribableXML( nameValueTypeXML );
            nameValueTypeXML.setName( String.valueOf( Math.random() ) );
            nameValueTypeXML.setType( String.valueOf( Math.random() ) );
            nameValueTypeXML.setValue( String.valueOf( Math.random() ) );

            // load jaxb object into collection
            propertySetsXML.getNameValueType().add( nameValueTypeXML );
        }

        // load jaxb object into describable
        describableXML.setPropertySets( propertySetsXML );

        // load jaxb security object reference into fugeXML
        if ( describableXML instanceof FuGECollectionFuGEType ) {
            FuGECollectionFuGEType fugeXML = ( FuGECollectionFuGEType ) describableXML;
            if ( fugeXML.getAuditCollection() == null ) {
                fugeXML = generateRandomAuditCollectionXML( fugeXML );
            }
            fugeXML.setSecurityRef( fugeXML.getAuditCollection().getSecurity().get( 0 ).getIdentifier() );
            describableXML = fugeXML;
        }
        return describableXML;
    }

    /**
     * Generates a random JAXB2 AuditCollection element and adds it to the rootXML JAXB2 object
     *
     * @param fugeXML the FuGE object to add the audit collection to
     * @return the modified version of fugeXML
     */
    private static FuGECollectionFuGEType generateRandomAuditCollectionXML( FuGECollectionFuGEType fugeXML ) {
        // create jaxb object
        FuGECollectionAuditCollectionType auditCollXML = new FuGECollectionAuditCollectionType();

        // set describable information
        auditCollXML = ( FuGECollectionAuditCollectionType ) generateRandomDescribableXML( auditCollXML );

        // set describable information
        auditCollXML = ( FuGECollectionAuditCollectionType ) generateRandomDescribableXML( auditCollXML );

        // Contacts
        String firstOrg = null;
        ObjectFactory factory = new ObjectFactory();
        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            // create jaxb object
            FuGECommonAuditOrganizationType organizationXML = new FuGECommonAuditOrganizationType();

            // set jaxb object
            organizationXML = ( FuGECommonAuditOrganizationType ) generateRandomContactXML( organizationXML );

            // set organization traits - only set a parent if i > 0.

            if ( i > 0 ) {
                FuGECommonAuditOrganizationType.Parent parentOrganizationXML =
                        new FuGECommonAuditOrganizationType.Parent();
                parentOrganizationXML.setOrganizationRef( firstOrg );
                organizationXML.setParent( parentOrganizationXML );
            } else {
                firstOrg = organizationXML.getIdentifier();
            }

            // add jaxb object into collection of objects
            auditCollXML.getContact().add( factory.createOrganization( organizationXML ) );

            // create jaxb object
            FuGECommonAuditPersonType personXML = new FuGECommonAuditPersonType();

            personXML = ( FuGECommonAuditPersonType ) generateRandomContactXML( personXML );

            // set jaxb object
            personXML = generateRandomPersonXML( personXML, organizationXML );

            // add jaxb object into collection of objects
            auditCollXML.getContact().add( factory.createPerson( personXML ) );
        }

        // set the security and security group
        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            FuGECommonAuditSecurityGroupType sgXML = new FuGECommonAuditSecurityGroupType();
            sgXML = ( FuGECommonAuditSecurityGroupType ) generateRandomIdentifiableXML( sgXML );

            for ( int ii = 0; ii < ELEMENT_DEPTH; ii++ ) {
                FuGECommonAuditSecurityGroupType.Members memXML = new FuGECommonAuditSecurityGroupType.Members();
                memXML.setContactRef( auditCollXML.getContact().get( ii ).getValue().getIdentifier() );
                sgXML.getMembers().add( memXML );
            }
            auditCollXML.getSecurityGroup().add( sgXML );
        }

        // fixme should owner really be a collection??
        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            FuGECommonAuditSecurityType securityXML = new FuGECommonAuditSecurityType();
            securityXML = ( FuGECommonAuditSecurityType ) generateRandomIdentifiableXML( securityXML );

            for ( int ii = 0; ii < ELEMENT_DEPTH; ii++ ) {
                FuGECommonAuditSecurityType.Owners ownerXML = new FuGECommonAuditSecurityType.Owners();
                ownerXML.setContactRef( auditCollXML.getContact().get( ii ).getValue().getIdentifier() );
                securityXML.getOwners().add( ownerXML );
            }

            for ( int ii = 0; ii < ELEMENT_DEPTH; ii++ ) {
                FuGECommonAuditSecurityAccessType accessXML = new FuGECommonAuditSecurityAccessType();
                accessXML = ( FuGECommonAuditSecurityAccessType ) generateRandomDescribableXML( accessXML );

                FuGECommonAuditSecurityAccessType.AccessRight accessRightXML =
                        new FuGECommonAuditSecurityAccessType.AccessRight();
                if ( fugeXML.getOntologyCollection() == null ) {
                    fugeXML = generateRandomOntologyCollectionXML( fugeXML );
                }
                accessRightXML.setOntologyTermRef(
                        fugeXML.getOntologyCollection().getOntologyTerm().get( ii ).getValue().getIdentifier() );
                accessXML.setAccessRight( accessRightXML );

                accessXML.setSecurityGroupRef( auditCollXML.getSecurityGroup().get( ii ).getIdentifier() );
                securityXML.getSecurityAccess().add( accessXML );
            }
            auditCollXML.getSecurity().add( securityXML );
        }

        fugeXML.setAuditCollection( auditCollXML );

        return fugeXML;
    }

    /**
     * Generates a random JAXB2 OntologyCollection element and adds it to the rootXML JAXB2 object
     *
     * @param fugeXML the object to fill with ontology collection information
     * @return the filled fuge object
     */
    private static FuGECollectionFuGEType generateRandomOntologyCollectionXML( FuGECollectionFuGEType fugeXML ) {
        FuGECollectionOntologyCollectionType ontoCollXML = new FuGECollectionOntologyCollectionType();

        ontoCollXML = ( FuGECollectionOntologyCollectionType ) generateRandomDescribableXML( ontoCollXML );

        // set ontology sources
        FuGECommonOntologyOntologySourceType ontoSourceXML = null;
        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            ontoSourceXML = new FuGECommonOntologyOntologySourceType();
            ontoSourceXML = ( FuGECommonOntologyOntologySourceType ) generateRandomIdentifiableXML( ontoSourceXML );
            ontoSourceXML.setOntologyURI( String.valueOf( Math.random() ) );
            ontoCollXML.getOntologySource().add( ontoSourceXML );
        }

        // set ontology terms
        ObjectFactory factory = new ObjectFactory();
        for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {
            ontoCollXML.getOntologyTerm()
                    .add( factory.createOntologyIndividual( generateRandomOntologyIndividualXML( ontoSourceXML ) ) );
        }

        fugeXML.setOntologyCollection( ontoCollXML );

        return fugeXML;
    }

    /**
     * Calls the other generateRandomOntologyIndividualXML method with a default boolean value of false, to
     * say that it isn't being generated inside the for-loop that's counting the number of repeated XML elements to make
     *
     * @param ontoSourceXML the OntologySource JAXB2 object to associate with this OntologyIndividual
     * @return a randomly-generated OntologyIndividual JAXB2 object
     */
    private static FuGECommonOntologyOntologyIndividualType generateRandomOntologyIndividualXML(
            FuGECommonOntologyOntologySourceType ontoSourceXML ) {
        return generateRandomOntologyIndividualXML( ontoSourceXML, false );
    }

    /**
     * @param ontoSourceXML the OntologySource JAXB2 object to associate with this OntologyIndividual
     * @param inner         whether or not this is being called from within this method
     * @return a randomly-generated OntologyIndividual JAXB2 object
     */
    private static FuGECommonOntologyOntologyIndividualType generateRandomOntologyIndividualXML(
            FuGECommonOntologyOntologySourceType ontoSourceXML, boolean inner ) {

        FuGECommonOntologyOntologyIndividualType ontologyIndividualXML = new FuGECommonOntologyOntologyIndividualType();
        ontologyIndividualXML = ( FuGECommonOntologyOntologyIndividualType ) generateRandomOntologyTermXML(
                ontologyIndividualXML, ontoSourceXML );

        if ( !inner ) {
            ObjectFactory factory = new ObjectFactory();
            for ( int i = 0; i < ELEMENT_DEPTH; i++ ) {

                FuGECommonOntologyObjectPropertyType objectPropertyXML = new FuGECommonOntologyObjectPropertyType();
                objectPropertyXML = ( FuGECommonOntologyObjectPropertyType ) generateRandomIdentifiableXML(
                        objectPropertyXML );
                objectPropertyXML = ( FuGECommonOntologyObjectPropertyType ) generateRandomOntologyTermXML(
                        objectPropertyXML, ontoSourceXML );
                for ( int ii = 0; ii < ELEMENT_DEPTH; ii++ ) {
                    objectPropertyXML.getOntologyIndividual()
                            .add( generateRandomOntologyIndividualXML( ontoSourceXML, true ) );
                }
                ontologyIndividualXML.getOntologyProperty().add( factory.createObjectProperty( objectPropertyXML ) );

                FuGECommonOntologyDataPropertyType dataPropertyXML = new FuGECommonOntologyDataPropertyType();
                dataPropertyXML =
                        ( FuGECommonOntologyDataPropertyType ) generateRandomIdentifiableXML( dataPropertyXML );
                dataPropertyXML = ( FuGECommonOntologyDataPropertyType ) generateRandomOntologyTermXML(
                        dataPropertyXML, ontoSourceXML );
                dataPropertyXML.setDataType( String.valueOf( Math.random() ) );
                ontologyIndividualXML.getOntologyProperty().add( factory.createDataProperty( dataPropertyXML ) );
            }
        }
        return ontologyIndividualXML;
    }

    /**
     * @param ontologyTermXML the JAXB2 object that is returned with attributes filled
     * @param ontoSourceXML   the OntologySource JAXB2 object to associate with this OntologyTerm
     * @return a randomly-generated OntologyTerm JAXB2 object
     */
    private static FuGECommonOntologyOntologyTermType generateRandomOntologyTermXML(
            FuGECommonOntologyOntologyTermType ontologyTermXML,
            FuGECommonOntologyOntologySourceType ontoSourceXML ) {

        ontologyTermXML = ( FuGECommonOntologyOntologyTermType ) generateRandomIdentifiableXML( ontologyTermXML );

        ontologyTermXML.setTerm( String.valueOf( Math.random() ) );
        ontologyTermXML.setTermAccession( String.valueOf( Math.random() ) );
        ontologyTermXML.setOntologySourceRef( ontoSourceXML.getIdentifier() );

        return ontologyTermXML;
    }

    /**
     * @param contactXML the JAXB2 object that is returned with attributes filled
     * @return a randomly-generated Contact JAXB2 object
     */
    private static FuGECommonAuditContactType generateRandomContactXML( FuGECommonAuditContactType contactXML ) {
        // set all identifiable traits in the jaxb object
        contactXML = ( FuGECommonAuditContactType ) generateRandomIdentifiableXML( contactXML );

        // set all non-identifiable contact traits
        contactXML.setAddress( String.valueOf( Math.random() ) );
        contactXML.setEmail( String.valueOf( Math.random() ) );
        contactXML.setFax( String.valueOf( Math.random() ) );
        contactXML.setPhone( String.valueOf( Math.random() ) );
        contactXML.setTollFreePhone( String.valueOf( Math.random() ) );

        return contactXML;
    }

    /**
     * @param personXML       the JAXB2 object that is returned with attributes filled
     * @param organizationXML the Organization JAXB2 object to associate with this Persn
     * @return a randomly-generated Person JAXB2 object
     */
    private static FuGECommonAuditPersonType generateRandomPersonXML(
            FuGECommonAuditPersonType personXML,
            FuGECommonAuditOrganizationType organizationXML ) {

        // set person traits
        personXML.setFirstName( String.valueOf( Math.random() ) );
        personXML.setLastName( String.valueOf( Math.random() ) );
        personXML.setMidInitials( String.valueOf( Math.random() ) );

        FuGECommonAuditPersonType.Affiliations affiliationXML = new FuGECommonAuditPersonType.Affiliations();

        affiliationXML.setOrganizationRef( organizationXML.getIdentifier() );
        personXML.getAffiliations().add( affiliationXML );

        return personXML;
    }

}
