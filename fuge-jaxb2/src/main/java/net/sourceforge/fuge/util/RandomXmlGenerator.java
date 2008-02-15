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
 * copyright 2007-8 Proteomics Standards Initiative / Microarray and Gene Expression
 * Data Society
 * <p/>
 * This is a document distributed under the terms of the
 * Creative Commons Attribution License
 * (http://creativecommons.org/licenses/by/3.0/), which permits
 * unrestricted use, distribution, and reproduction in any medium,
 * provided the original work is properly cited.
 * <p/>
 * Acknowledgements
 * The authors wish to thank the Proteomics Standards Initiative for
 * the provision of infrastructure and expertise in the form of the PSI
 * Document Process that has been used to formalise this document.
 * <p/>
 * <p/>
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

    private final String schemaFilename;
    private final String XMLFilename;
    private final int NUMBER_ELEMENTS = 3;

    private FuGECollectionFuGEType rootXML;


    /**
     * Constructor
     *
     * @param sf the XSD file used to validate the created XML
     * @param xf the base name used to generate the output filenames. For example, "output.xml" would produce output0.xml, output1.xml, and output2.xml
     */
    public RandomXmlGenerator( String sf, String xf ) {
        this.schemaFilename = sf;
        this.XMLFilename = xf;
        rootXML = new FuGECollectionFuGEType();
    }

    /**
     * Generates the random XML files.
     *
     * @throws JAXBException
     * @throws SAXException
     * @throws FileNotFoundException
     */
    public void generate() throws JAXBException, SAXException, FileNotFoundException {
        OutputStream os;

        System.err.println( "Schema file is: " + schemaFilename );
        System.err.println( "Base name for XML output is: " + XMLFilename );

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

        // set the correct schema
        SchemaFactory sf = SchemaFactory.newInstance( W3C_XML_SCHEMA_NS_URI );
        Schema schema = sf.newSchema( new File( schemaFilename ) );
        m.setSchema( schema );

        System.err.println( "Marshaller initialized." );

        // create a jaxb root object
        System.err.println( "Starting generation..." );

        String name = XMLFilename.substring( 0, XMLFilename.lastIndexOf( "." ) );
        String ext = XMLFilename.substring( XMLFilename.lastIndexOf( "." ) );

        // make 3 versions of the file
        for ( int i = 0; i < 3; i++ ) {

            generateRandomFuGEXML();

            os = new FileOutputStream( name + String.valueOf( i ) + ext );
            m.marshal(
                    new JAXBElement(
                            new QName( "http://fuge.sourceforge.net/fuge/1.0", "FuGE" ),
                            FuGECollectionFuGEType.class,
                            rootXML ),
                    os );
        }

        System.err.println( "Generation complete." );

    }

    /**
     * Public container class for all of the random generation methods
     */
    private void generateRandomFuGEXML() {

        // generate identifiable traits
        rootXML = ( FuGECollectionFuGEType ) generateRandomIdentifiableXML( rootXML );

        // generate AuditCollection information
        if ( rootXML.getAuditCollection() == null ) {
            generateRandomAuditCollectionXML();
        }

        // generate OntologyCollection information
        if ( rootXML.getOntologyCollection() == null ) {
            generateRandomOntologyCollectionXML();
        }

        // generate ReferenceableCollection information
        if ( rootXML.getReferenceableCollection() == null ) {
            generateRandomReferenceableCollectionXML();
        }

        // Get all MaterialCollection information
        if ( rootXML.getMaterialCollection() == null ) {
            generateRandomMaterialCollectionXML();
        }

        // Get all data collection information - MUST BE DONE before Protocol and after Material
        if ( rootXML.getDataCollection() == null ) {
            generateRandomDataCollectionXML();
        }

        // Get all protocol collection information
        if ( rootXML.getProtocolCollection() == null ) {
            // marshall the fuge object into a jaxb object
            generateRandomProtocolCollectionXML();
        }

        // Get all Provider information
        if ( rootXML.getProvider() == null ) {
            // marshall the fuge object into a jaxb object
            generateRandomProviderXML();
        }

        // Get an Investigation, if present
        if ( rootXML.getInvestigationCollection() == null ) {
            // unmarshall the jaxb object into a fuge object, then set the fuge object within the top level fuge root object
            generateRandomInvestigationCollectionXML();
        }

    }

    /**
     * todo investigation incomplete (only has identifiable elements)
     * <p/>
     * Generates a random JAXB2 InvestigationCollection element and adds it to the rootXML JAXB2 object
     */
    private void generateRandomInvestigationCollectionXML() {

        FuGECollectionInvestigationCollectionType investigationCollectionXML = new FuGECollectionInvestigationCollectionType();
        investigationCollectionXML = ( FuGECollectionInvestigationCollectionType ) generateRandomDescribableXML(
                investigationCollectionXML );
        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            FuGEBioInvestigationFactorType factorXML = new FuGEBioInvestigationFactorType();

            factorXML = ( FuGEBioInvestigationFactorType ) generateRandomIdentifiableXML( factorXML );

            // set the non-identifiable traits

            if ( rootXML.getOntologyCollection() != null ) {
                FuGEBioInvestigationFactorType.FactorType factorTypeXML = new FuGEBioInvestigationFactorType.FactorType();
                factorTypeXML.setOntologyTermRef(
                        rootXML.getOntologyCollection().getOntologyTerm().get( i ).getValue().getIdentifier() );
                factorXML.setFactorType( factorTypeXML );
            }

            for ( int ii = 0; ii < NUMBER_ELEMENTS; ii++ ) {
                FuGEBioInvestigationFactorValueType factorValueXML = new FuGEBioInvestigationFactorValueType();
                factorValueXML = ( FuGEBioInvestigationFactorValueType ) generateRandomDescribableXML( factorValueXML );
                factorValueXML.setMeasurement( generateRandomMeasurementXML() );

// todo still not sure where datapartitions fit in, so won't make them for now.
//                for ( int iii = 0; iii < NUMBER_ELEMENTS; iii++ ) {
//                    FuGEBioInvestigationFactorValueType.DataPartitions dataPartitionXML = new FuGEBioInvestigationFactorValueType.DataPartitions();
//                    if ( rootXML.getDataCollection() != null ) {
//                        dataPartitionXML.setDataPartitionRef( rootXML.getDataCollection().getData().get( iii ).getValue().getIdentifier() );
//                        // todo not sure how to set the dimension element here. can't get it from dataPartitionXML
//                        factorValueXML.getDataPartitions().add( dataPartitionXML );
//                    }
//                }
                factorXML.getFactorValue().add( factorValueXML );
            }
            investigationCollectionXML.getFactor().add( factorXML );
        }
        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            FuGEBioInvestigationInvestigationType investigationXML = new FuGEBioInvestigationInvestigationType();

            investigationXML = ( FuGEBioInvestigationInvestigationType ) generateRandomIdentifiableXML( investigationXML );
            investigationCollectionXML.getInvestigation().add( investigationXML );
        }

        rootXML.setInvestigationCollection( investigationCollectionXML );

    }

    /**
     * Generates a random JAXB2 Provider element and adds it to the rootXML JAXB2 object
     */
    private void generateRandomProviderXML() {
        // create fuge object
        FuGECollectionProviderType providerXML = new FuGECollectionProviderType();

        providerXML = ( FuGECollectionProviderType ) generateRandomIdentifiableXML( providerXML );

        providerXML.setContactRole( generateRandomContactRoleXML() );

        if ( rootXML.getProtocolCollection() != null ) {
            providerXML.setSoftwareRef(
                    rootXML.getProtocolCollection().getSoftware().get( 0 ).getValue().getIdentifier() );
        }
        rootXML.setProvider( providerXML );

    }

    /**
     * Generates a random JAXB2 ProtocolCollection and adds it to the rootXML JAXB2 object
     */
    private void generateRandomProtocolCollectionXML() {
        FuGECollectionProtocolCollectionType protocolCollectionXML = new FuGECollectionProtocolCollectionType();

        protocolCollectionXML = ( FuGECollectionProtocolCollectionType ) generateRandomDescribableXML(
                protocolCollectionXML );

        if ( protocolCollectionXML.getEquipment().isEmpty() ) {
            // equipment
            protocolCollectionXML = generateRandomEquipmentXML( protocolCollectionXML );
        }

        if ( protocolCollectionXML.getSoftware().isEmpty() ) {
            // software
            for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
                FuGECommonProtocolGenericSoftwareType genericSoftwareXML = new FuGECommonProtocolGenericSoftwareType();
                genericSoftwareXML = ( FuGECommonProtocolGenericSoftwareType ) generateRandomSoftwareXML(
                        genericSoftwareXML, protocolCollectionXML );
                JAXBElement<? extends FuGECommonProtocolGenericSoftwareType> element = ( new ObjectFactory() )
                        .createGenericSoftware( genericSoftwareXML );
                protocolCollectionXML.getSoftware().add( element );
            }
        }

        if ( protocolCollectionXML.getProtocol().isEmpty() ) {
            // protocol
            for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
                FuGECommonProtocolGenericProtocolType genericProtocolXML = new FuGECommonProtocolGenericProtocolType();
                genericProtocolXML = ( FuGECommonProtocolGenericProtocolType ) generateRandomProtocolXML(
                        genericProtocolXML, protocolCollectionXML );
                JAXBElement<? extends FuGECommonProtocolGenericProtocolType> element = ( new ObjectFactory() )
                        .createGenericProtocol( genericProtocolXML );
                protocolCollectionXML.getProtocol().add( element );
            }
        }

        if ( protocolCollectionXML.getProtocolApplication().isEmpty() ) {
            // protocol application
            for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
                JAXBElement<? extends FuGECommonProtocolGenericProtocolApplicationType> element = ( new ObjectFactory() )
                        .createGenericProtocolApplication(
                                ( FuGECommonProtocolGenericProtocolApplicationType ) generateRandomProtocolApplicationXML(
                                        i, protocolCollectionXML ) );
                protocolCollectionXML.getProtocolApplication().add( element );
            }
        }

        rootXML.setProtocolCollection( protocolCollectionXML );

    }

    /**
     * At this stage, rootXML may not have the new equipment and software - the protocol collection may be the only one to have it
     *
     * @param protocolXML           the JAXB2 object that is returned with attributes filled
     * @param protocolCollectionXML required to check values from
     * @return a random JAXB2 Protocol
     */
    private FuGECommonProtocolProtocolType generateRandomProtocolXML( FuGECommonProtocolProtocolType protocolXML,
                                                                      FuGECollectionProtocolCollectionType protocolCollectionXML
    ) {
        FuGECommonProtocolGenericProtocolType genericProtocolXML = ( FuGECommonProtocolGenericProtocolType ) protocolXML;

        // get protocol attributes
        genericProtocolXML = ( FuGECommonProtocolGenericProtocolType ) generateRandomIdentifiableXML( genericProtocolXML );

        genericProtocolXML = ( FuGECommonProtocolGenericProtocolType ) generateRandomParameterizableXML(
                genericProtocolXML );

        if ( rootXML.getOntologyCollection() != null ) {
            // input types
            for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
                FuGECommonProtocolProtocolType.InputTypes inputTypesXML = new FuGECommonProtocolProtocolType.InputTypes();
                inputTypesXML.setOntologyTermRef(
                        rootXML.getOntologyCollection().getOntologyTerm().get( i ).getValue().getIdentifier() );
                genericProtocolXML.getInputTypes().add( inputTypesXML );
            }

            // output types
            for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
                FuGECommonProtocolProtocolType.OutputTypes outputTypesXML = new FuGECommonProtocolProtocolType.OutputTypes();
                outputTypesXML.setOntologyTermRef(
                        rootXML.getOntologyCollection().getOntologyTerm().get( i ).getValue().getIdentifier() );
                genericProtocolXML.getOutputTypes().add( outputTypesXML );
            }
        }

        genericProtocolXML = generateRandomGenericProtocolXML( genericProtocolXML, protocolCollectionXML );

        return genericProtocolXML;

    }

    /**
     * @param genericProtocolXML    the JAXB2 object that is returned with attributes filled
     * @param protocolCollectionXML needed for the information contained within it to make the JAXB2 object correctly
     * @return a random JAXB2 GenericProtocol
     */
    private FuGECommonProtocolGenericProtocolType generateRandomGenericProtocolXML(
            FuGECommonProtocolGenericProtocolType genericProtocolXML,
            FuGECollectionProtocolCollectionType protocolCollectionXML ) {

        // can only have generic actions.
        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            genericProtocolXML.getAction().add(
                    new ObjectFactory().createGenericAction(
                            generateRandomActionXML(
                                    new FuGECommonProtocolGenericActionType(), i, protocolCollectionXML ) ) );
        }
        // can only have generic parameters
        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            genericProtocolXML.getGenericParameter().add(
                    ( FuGECommonProtocolGenericParameterType ) generateRandomParameterXML(
                            new FuGECommonProtocolGenericParameterType() ) );
        }

        if ( protocolCollectionXML != null ) {
            // protocol to equipment
            for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
                FuGECommonProtocolGenericProtocolType.Equipment equip = new FuGECommonProtocolGenericProtocolType.Equipment();
                equip.setGenericEquipmentRef(
                        protocolCollectionXML.getEquipment().get( i ).getValue().getIdentifier() );
                genericProtocolXML.getEquipment().add( equip );
            }
            // software
            for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
                FuGECommonProtocolGenericProtocolType.Software genSoftware = new FuGECommonProtocolGenericProtocolType.Software();
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
     * @return a random JAXB2 Action
     */
    private FuGECommonProtocolGenericActionType generateRandomActionXML( FuGECommonProtocolActionType actionXML,
                                                                         int ordinal,
                                                                         FuGECollectionProtocolCollectionType protocolCollectionXML ) {

        FuGECommonProtocolGenericActionType genericActionXML = ( FuGECommonProtocolGenericActionType ) actionXML;

        // get action attributes
        genericActionXML = ( FuGECommonProtocolGenericActionType ) generateRandomIdentifiableXML( genericActionXML );

        // action ordinal
        genericActionXML.setActionOrdinal( ordinal );

        // get generic action attributes
        genericActionXML = generateRandomGenericActionXML( genericActionXML, protocolCollectionXML );

        return genericActionXML;

    }

    /**
     * @param genericActionXML      the JAXB2 object that is returned with attributes filled
     * @param protocolCollectionXML needed for the information contained within it to make the JAXB2 object correctly
     * @return a random JAXB2 GenericAction
     */
    private FuGECommonProtocolGenericActionType generateRandomGenericActionXML( FuGECommonProtocolGenericActionType genericActionXML,
                                                                                FuGECollectionProtocolCollectionType protocolCollectionXML
    ) {
        // action term
        if ( rootXML.getOntologyCollection() != null ) {
            FuGECommonProtocolGenericActionType.ActionTerm aterm = new FuGECommonProtocolGenericActionType.ActionTerm();
            aterm.setOntologyTermRef(
                    rootXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
            genericActionXML.setActionTerm( aterm );
        }

        // action text
        genericActionXML.setActionText( String.valueOf( Math.random() ) );

        // protocol ref
        if ( protocolCollectionXML.getProtocol().size() > 0 ) {
            genericActionXML.setProtocolRef( protocolCollectionXML.getProtocol().get( 0 ).getValue().getIdentifier() );
        }

        // you can only have a GenericParameter here
        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            genericActionXML.getGenericParameter().add(
                    ( FuGECommonProtocolGenericParameterType ) generateRandomParameterXML(
                            new FuGECommonProtocolGenericParameterType() ) );
        }

        return genericActionXML;
    }

    /**
     * at this stage, rootXML may not have the new equipment and software - the protocol collection may be the only one to have it
     *
     * @param ordinal               the position in an exterior loop controlling how many elements are generated
     * @param protocolCollectionXML needed for the information contained within it to make the JAXB2 object correctly
     * @return a random JAXB2 ProtocolApplication
     */
    private FuGECommonProtocolProtocolApplicationType generateRandomProtocolApplicationXML( int ordinal,
                                                                                            FuGECollectionProtocolCollectionType protocolCollectionXML ) {
        FuGECommonProtocolGenericProtocolApplicationType genericProtocolApplicationXML = new FuGECommonProtocolGenericProtocolApplicationType();

        // get protocol application attributes
        genericProtocolApplicationXML = ( FuGECommonProtocolGenericProtocolApplicationType ) generateRandomIdentifiableXML(
                genericProtocolApplicationXML );
        genericProtocolApplicationXML = ( FuGECommonProtocolGenericProtocolApplicationType ) generateRandomParameterizableApplicationXML(
                genericProtocolApplicationXML, protocolCollectionXML );

        try {
            genericProtocolApplicationXML.setActivityDate( DatatypeFactory.newInstance().newXMLGregorianCalendar( new GregorianCalendar() ) );
        } catch ( DatatypeConfigurationException e ) {
            System.err.println(
                    "Error trying to set the Protocol Application element's date to the current time/date. Not setting." );
            e.printStackTrace();
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
            FuGECommonProtocolActionApplicationType actionApplicationXML = new FuGECommonProtocolActionApplicationType();
            actionApplicationXML = ( FuGECommonProtocolActionApplicationType ) generateRandomIdentifiableXML(
                    actionApplicationXML );

            FuGECommonProtocolGenericProtocolType gpType = ( FuGECommonProtocolGenericProtocolType ) protocolCollectionXML
                    .getProtocol().get( ordinal ).getValue();
            actionApplicationXML.setActionRef( gpType.getAction().get( ordinal ).getValue().getIdentifier() );
            FuGECommonDescriptionDescriptionType descXML = new FuGECommonDescriptionDescriptionType();
            descXML = ( FuGECommonDescriptionDescriptionType ) generateRandomDescribableXML( descXML );
            descXML.setText( String.valueOf( Math.random() ) );
            FuGECommonProtocolActionApplicationType.ActionDeviation deviation = new FuGECommonProtocolActionApplicationType.ActionDeviation();
            deviation.setDescription( descXML );

            actionApplicationXML.setActionDeviation( deviation );
            genericProtocolApplicationXML.getActionApplication().add( actionApplicationXML );

            if ( ordinal > 0 ) {
                actionApplicationXML.setProtocolApplicationRef(
                        protocolCollectionXML.getProtocolApplication().get( 0 ).getValue().getIdentifier() );
            }
        }

        FuGECommonProtocolProtocolApplicationType.ProtocolDeviation pdXML = new FuGECommonProtocolProtocolApplicationType.ProtocolDeviation();
        FuGECommonDescriptionDescriptionType descXML = new FuGECommonDescriptionDescriptionType();
        descXML = ( FuGECommonDescriptionDescriptionType ) generateRandomDescribableXML( descXML );
        descXML.setText( String.valueOf( Math.random() ) );
        pdXML.setDescription( descXML );
        genericProtocolApplicationXML.setProtocolDeviation( pdXML );

        genericProtocolApplicationXML = generateRandomGenericProtocolApplicationXML(
                genericProtocolApplicationXML, ordinal, protocolCollectionXML );
        return genericProtocolApplicationXML;

    }

    /**
     * @param genericProtocolApplicationXML the JAXB2 object that is returned with attributes filled
     * @param ordinal                       the position in an exterior loop controlling how many elements are generated
     * @param protocolCollectionXML         needed for the information contained within it to make the JAXB2 object correctly
     * @return a random JAXB2 GenericProtocolApplication
     */
    private FuGECommonProtocolGenericProtocolApplicationType generateRandomGenericProtocolApplicationXML(
            FuGECommonProtocolGenericProtocolApplicationType genericProtocolApplicationXML,
            int ordinal,
            FuGECollectionProtocolCollectionType protocolCollectionXML ) {

        if ( !protocolCollectionXML.getProtocol().isEmpty() ) {
            genericProtocolApplicationXML.setProtocolRef(
                    protocolCollectionXML.getProtocol().get( ordinal ).getValue().getIdentifier() );
        }

        int output = ordinal + 1;
        if ( ordinal == NUMBER_ELEMENTS - 1 )
            output = 0;

        if ( rootXML.getDataCollection() != null ) {
            // input data
            FuGECommonProtocolGenericProtocolApplicationType.InputData gidXML = new FuGECommonProtocolGenericProtocolApplicationType.InputData();
            gidXML.setDataRef( rootXML.getDataCollection().getData().get( ordinal ).getValue().getIdentifier() );
            genericProtocolApplicationXML.getInputData().add( gidXML );

            // output data
            FuGECommonProtocolGenericProtocolApplicationType.OutputData godXML = new FuGECommonProtocolGenericProtocolApplicationType.OutputData();
            godXML.setDataRef( rootXML.getDataCollection().getData().get( output ).getValue().getIdentifier() );
            genericProtocolApplicationXML.getOutputData().add( godXML );
        }

        if ( rootXML.getMaterialCollection() != null ) {
            // input complete material
            FuGECommonProtocolGenericProtocolApplicationType.InputCompleteMaterials gimXML = new FuGECommonProtocolGenericProtocolApplicationType.InputCompleteMaterials();
            gimXML.setMaterialRef(
                    rootXML.getMaterialCollection().getMaterial().get( ordinal ).getValue().getIdentifier() );
            genericProtocolApplicationXML.getInputCompleteMaterials().add( gimXML );

            // output material
            FuGECommonProtocolGenericProtocolApplicationType.OutputMaterials gomXML = new FuGECommonProtocolGenericProtocolApplicationType.OutputMaterials();
            gomXML.setMaterialRef(
                    rootXML.getMaterialCollection().getMaterial().get( ordinal ).getValue().getIdentifier() );
            genericProtocolApplicationXML.getOutputMaterials().add( gomXML );
        }

        return genericProtocolApplicationXML;

    }

    /**
     * @param parameterizableApplicationXML the JAXB2 object that is returned with attributes filled
     * @param protocolCollectionXML         needed for the information contained within it to make the JAXB2 object correctly
     * @return a random JAXB2 ParameterizableApplication
     */
    private FuGECommonProtocolParameterizableApplicationType generateRandomParameterizableApplicationXML(
            FuGECommonProtocolParameterizableApplicationType parameterizableApplicationXML,
            FuGECollectionProtocolCollectionType protocolCollectionXML ) {

        if ( !protocolCollectionXML.getEquipment().isEmpty() ) {
            FuGECommonProtocolGenericEquipmentType eqXML = ( FuGECommonProtocolGenericEquipmentType ) protocolCollectionXML
                    .getEquipment()
                    .get( 0 )
                    .getValue();
            for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
                FuGECommonProtocolParameterValueType pvalueXML = new FuGECommonProtocolParameterValueType();
                pvalueXML = ( FuGECommonProtocolParameterValueType ) generateRandomDescribableXML( pvalueXML );
                pvalueXML.setMeasurement( generateRandomMeasurementXML() );
                pvalueXML.setParameterRef( eqXML.getGenericParameter().get( 0 ).getIdentifier() );
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
     * @return a random JAXB2 ProtocolCollection
     */
    private FuGECollectionProtocolCollectionType generateRandomEquipmentXML(
            FuGECollectionProtocolCollectionType protocolCollectionXML ) {

        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            FuGECommonProtocolGenericEquipmentType genEquipmentXML = new FuGECommonProtocolGenericEquipmentType();
            // get equipment attributes
            genEquipmentXML = ( FuGECommonProtocolGenericEquipmentType ) generateRandomIdentifiableXML( genEquipmentXML );

            genEquipmentXML = ( FuGECommonProtocolGenericEquipmentType ) generateRandomParameterizableXML(
                    genEquipmentXML );

            if ( rootXML.getOntologyCollection() != null ) {
                FuGECommonProtocolEquipmentType.Make make = new FuGECommonProtocolEquipmentType.Make();
                make.setOntologyTermRef(
                        rootXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
                genEquipmentXML.setMake( make );

                FuGECommonProtocolEquipmentType.Model model = new FuGECommonProtocolEquipmentType.Model();
                model.setOntologyTermRef(
                        rootXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
                genEquipmentXML.setModel( model );
            }

            // software required for generic equipment attributes
            if ( protocolCollectionXML.getSoftware() == null ) {
                for ( int ii = 0; ii < NUMBER_ELEMENTS; ii++ ) {
                    FuGECommonProtocolGenericSoftwareType genericSoftwareXML = new FuGECommonProtocolGenericSoftwareType();
                    genericSoftwareXML = ( FuGECommonProtocolGenericSoftwareType ) generateRandomSoftwareXML(
                            genericSoftwareXML, protocolCollectionXML );
                    JAXBElement<? extends FuGECommonProtocolGenericSoftwareType> element = ( new ObjectFactory() ).createGenericSoftware(
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
                                .getValue()
                );
            } else {
                genEquipmentXML = generateRandomGenericEquipmentXML( genEquipmentXML, null );
            }

            JAXBElement<? extends FuGECommonProtocolGenericEquipmentType> element = ( new ObjectFactory() ).createGenericEquipment(
                    genEquipmentXML );
            protocolCollectionXML.getEquipment().add( element );
        }
        return protocolCollectionXML;

    }

    /**
     * @param genericEquipmentXML the JAXB2 object that is returned with attributes filled
     * @param partXML             the parts list for this equipment
     * @return a random JAXB2 GenericEquipment
     */
    private FuGECommonProtocolGenericEquipmentType generateRandomGenericEquipmentXML(
            FuGECommonProtocolGenericEquipmentType genericEquipmentXML,
            FuGECommonProtocolGenericEquipmentType partXML ) {

        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            FuGECommonProtocolGenericParameterType parameterXML = new FuGECommonProtocolGenericParameterType();
            genericEquipmentXML.getGenericParameter()
                    .add(
                            ( FuGECommonProtocolGenericParameterType ) generateRandomParameterXML(
                                    parameterXML ) );
        }

        if ( partXML != null ) {
            // parts list of one
            FuGECommonProtocolGenericEquipmentType.EquipmentParts parts = new FuGECommonProtocolGenericEquipmentType.EquipmentParts();
            parts.setGenericEquipmentRef( partXML.getIdentifier() );
            genericEquipmentXML.getEquipmentParts().add( parts );
        }

        if ( rootXML.getProtocolCollection() != null ) {
            for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
                FuGECommonProtocolGenericEquipmentType.Software softwareXML = new FuGECommonProtocolGenericEquipmentType.Software();
                softwareXML.setGenericSoftwareRef(
                        rootXML.getProtocolCollection().getSoftware().get( i ).getValue().getIdentifier() );
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
     * @return a random JAXB2 Software Object
     */
    private FuGECommonProtocolSoftwareType generateRandomSoftwareXML( FuGECommonProtocolSoftwareType softwareXML,
                                                                      FuGECollectionProtocolCollectionType protocolCollectionXML ) {
        // make software attributes

        if ( softwareXML instanceof FuGECommonProtocolGenericSoftwareType ) {
            FuGECommonProtocolGenericSoftwareType genericSoftwareXML = ( FuGECommonProtocolGenericSoftwareType ) softwareXML;
            genericSoftwareXML = ( FuGECommonProtocolGenericSoftwareType ) generateRandomIdentifiableXML(
                    genericSoftwareXML );
            genericSoftwareXML = ( FuGECommonProtocolGenericSoftwareType ) generateRandomParameterizableXML(
                    genericSoftwareXML );

            genericSoftwareXML.setVersion( String.valueOf( Math.random() ) );

            // get generic software attributes
            genericSoftwareXML = generateRandomGenericSoftwareXML( genericSoftwareXML, protocolCollectionXML );

            return genericSoftwareXML;
        }

        return softwareXML;

    }

    /**
     * @param genericSoftwareXML    the JAXB2 object that is returned with attributes filled
     * @param protocolCollectionXML needed for the information contained within it to make the JAXB2 object correctly
     * @return a random JAXB2 GenericSoftware
     */
    private FuGECommonProtocolGenericSoftwareType generateRandomGenericSoftwareXML(
            FuGECommonProtocolGenericSoftwareType genericSoftwareXML,
            FuGECollectionProtocolCollectionType protocolCollectionXML ) {

        // you can only have a GenericParameter here
        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            genericSoftwareXML.getGenericParameter().add(
                    ( FuGECommonProtocolGenericParameterType ) generateRandomParameterXML(
                            new FuGECommonProtocolGenericParameterType() ) );
        }

        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            FuGECommonProtocolGenericSoftwareType.Equipment parts = new FuGECommonProtocolGenericSoftwareType.Equipment();
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
     * @return a random JAXB2 Parameter
     */
    private FuGECommonProtocolParameterType generateRandomParameterXML( FuGECommonProtocolParameterType parameterXML ) {

        // get parameter attributes
        parameterXML = ( FuGECommonProtocolParameterType ) generateRandomIdentifiableXML( parameterXML );

        parameterXML.setIsInputParam( true );

        // make a measurement

        parameterXML.setMeasurement( generateRandomMeasurementXML() );

        // get generic parameter attributes
        parameterXML = generateRandomGenericParameterXML(
                ( FuGECommonProtocolGenericParameterType ) parameterXML );
        return parameterXML;

    }

    /**
     * @param genericParameterXML the JAXB2 object that is returned with attributes filled
     * @return a random JAXB2 GenericParameter
     */
    private FuGECommonProtocolGenericParameterType generateRandomGenericParameterXML(
            FuGECommonProtocolGenericParameterType genericParameterXML ) {

        if ( rootXML.getOntologyCollection() != null ) {
            FuGECommonProtocolGenericParameterType.ParameterType ptXML = new FuGECommonProtocolGenericParameterType.ParameterType();
            ptXML.setOntologyTermRef(
                    rootXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
            genericParameterXML.setParameterType( ptXML );
        }

        return genericParameterXML;
    }

    /**
     * todo deal with propertySets
     *
     * @return an AtomicValue Measurement wrapped in a JAXB2 element
     */
    private JAXBElement<? extends FuGECommonMeasurementAtomicValueType> generateRandomMeasurementXML() {

        // just choose one of the concrete versions of measurement
        FuGECommonMeasurementAtomicValueType measurementXML = new FuGECommonMeasurementAtomicValueType();

        // data type
        if ( rootXML.getOntologyCollection() != null ) {
            FuGECommonMeasurementMeasurementType.DataType dtXML = new FuGECommonMeasurementMeasurementType.DataType();
            dtXML.setOntologyTermRef(
                    rootXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
            measurementXML.setDataType( dtXML );
        }

        // unit
        if ( rootXML.getOntologyCollection() != null ) {
            FuGECommonMeasurementMeasurementType.Unit unitXML = new FuGECommonMeasurementMeasurementType.Unit();
            unitXML.setOntologyTermRef(
                    rootXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
            measurementXML.setUnit( unitXML );
        }

        // value
        measurementXML.setValue( "8" );

        ObjectFactory factory = new ObjectFactory();
        return factory.createAtomicValue( measurementXML );

    }

    /**
     * @param parameterizableXML the JAXB2 object that is returned with attributes filled
     * @return a random JAXB2 Parameterizable
     */
    private FuGECommonProtocolParameterizableType generateRandomParameterizableXML(
            FuGECommonProtocolParameterizableType parameterizableXML ) {

        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            parameterizableXML.getContactRole().add( generateRandomContactRoleXML() );
        }

        if ( rootXML.getOntologyCollection() != null ) {
            for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
                FuGECommonProtocolParameterizableType.Types parameterizableTypesXML = new FuGECommonProtocolParameterizableType.Types();
                parameterizableTypesXML.setOntologyTermRef(
                        rootXML.getOntologyCollection().getOntologyTerm().get( i ).getValue().getIdentifier() );
                parameterizableXML.getTypes().add( parameterizableTypesXML );
            }
        }
        return parameterizableXML;
    }

    /**
     * todo deal with InternalData
     * <p/>
     * Generates a random JAXB2 DataCollection element and adds it to the rootXML JAXB2 object
     */
    private void generateRandomDataCollectionXML() {

        // create the jaxb Data collection object
        FuGECollectionDataCollectionType datCollXML = new FuGECollectionDataCollectionType();

        // set describable information
        datCollXML = ( FuGECollectionDataCollectionType ) generateRandomDescribableXML( datCollXML );

        // set up the factory
        ObjectFactory factory = new ObjectFactory();

        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {

            // As RawData objects do not appear in the XML, there is no need to code it here.
            datCollXML.getData().add(
                    factory.createExternalData(
                            ( FuGEBioDataExternalDataType ) generateRandomDataXML(
                                    new FuGEBioDataExternalDataType() ) ) );
        }

        rootXML.setDataCollection( datCollXML );

    }

    /**
     * @param dataXML the JAXB2 object that is returned with attributes filled
     * @return a random JAXB2 Data object
     */
    private FuGEBioDataDataType generateRandomDataXML( FuGEBioDataDataType dataXML ) {

        FuGEBioDataExternalDataType externalDataXML = ( FuGEBioDataExternalDataType ) dataXML;

        // set the data attributes
        externalDataXML = ( FuGEBioDataExternalDataType ) generateRandomIdentifiableXML( externalDataXML );

        // set the externaldata attributes
        externalDataXML = generateRandomExternalDataXML( externalDataXML );

        return externalDataXML;

    }

    /**
     * Creates a random JAXB2 ExternalData object
     *
     * @param externalDataXML the JAXB2 object that is returned with attributes filled
     * @return a random JAXB2 ExternalData object
     */
    private FuGEBioDataExternalDataType generateRandomExternalDataXML( FuGEBioDataExternalDataType externalDataXML ) {

        // FileFormat
        if ( rootXML.getOntologyCollection() != null ) {
            FuGEBioDataExternalDataType.FileFormat fileformatXML = new FuGEBioDataExternalDataType.FileFormat();
            fileformatXML.setOntologyTermRef(
                    rootXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
            externalDataXML.setFileFormat( fileformatXML );
        }

        // Location
        externalDataXML.setLocation( String.valueOf( Math.random() ) );

        // external format documentation
        FuGEBioDataExternalDataType.ExternalFormatDocumentation efdXML = new FuGEBioDataExternalDataType.ExternalFormatDocumentation();
        FuGECommonDescriptionURIType uriXML = new FuGECommonDescriptionURIType();

        // set jaxb object
        uriXML = ( FuGECommonDescriptionURIType ) generateRandomDescribableXML( uriXML );
        uriXML.setLocation( "http://some.sortof.string/" + String.valueOf( Math.random() ) );

        // load jaxb object into describableXML
        efdXML.setURI( uriXML );
        externalDataXML.setExternalFormatDocumentation( efdXML );

        return externalDataXML;

    }

    /**
     * Generates a random JAXB2 MaterialCollection element and adds it to the rootXML JAXB2 object
     */
    private void generateRandomMaterialCollectionXML() {

        // create the jaxb material collection object
        FuGECollectionMaterialCollectionType matCollXML = new FuGECollectionMaterialCollectionType();

        // set describable information
        matCollXML = ( FuGECollectionMaterialCollectionType ) generateRandomDescribableXML( matCollXML );

        // set up the converter and the factory
        ObjectFactory factory = new ObjectFactory();

        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            // set jaxb object
            if ( i > 0 ) {
                matCollXML.getMaterial().add(
                        factory.createGenericMaterial(
                                ( FuGEBioMaterialGenericMaterialType ) generateRandomMaterialXML(
                                        ( FuGEBioMaterialGenericMaterialType ) matCollXML.getMaterial()
                                                .get( 0 )
                                                .getValue() ) ) );
            } else {
                matCollXML.getMaterial().add(
                        factory.createGenericMaterial(
                                ( FuGEBioMaterialGenericMaterialType ) generateRandomMaterialXML(
                                        null ) ) );
            }
        }

        rootXML.setMaterialCollection( matCollXML );

    }

    /**
     * Creates a random JAXB2 Material
     *
     * @param genXML the JAXB2 object that is returned with attributes filled
     * @return a random JAXB2 Material
     */
    private FuGEBioMaterialMaterialType generateRandomMaterialXML( FuGEBioMaterialGenericMaterialType genXML ) {
        // create fuge object
        FuGEBioMaterialGenericMaterialType genericMaterialXML = new FuGEBioMaterialGenericMaterialType();

        // set the material attributes
        genericMaterialXML = ( FuGEBioMaterialGenericMaterialType ) generateRandomSpecificXML(
                genericMaterialXML );

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
    private FuGEBioMaterialGenericMaterialType generateRandomGenericMaterialXML( FuGEBioMaterialGenericMaterialType genericMaterialXML,
                                                                                 FuGEBioMaterialGenericMaterialType componentXML ) {
        // Components. These elements are references to GenericMaterial. Only generate one reference.
        if ( componentXML != null ) {
            FuGEBioMaterialGenericMaterialType.Components componentsXML = new FuGEBioMaterialGenericMaterialType.Components();
            componentsXML.setGenericMaterialRef( componentXML.getIdentifier() );
            genericMaterialXML.getComponents().add( componentsXML );
        }
        return genericMaterialXML;
    }


    /**
     * This should be run at a time where the ontology collection and audit collection have already been run.
     *
     * @param materialXML the JAXB2 object that is returned with attributes filled
     * @return the passed Material JAXB2 object with the attributes filled that are specific to the abstract Material class
     */
    private FuGEBioMaterialMaterialType generateRandomSpecificXML( FuGEBioMaterialMaterialType materialXML ) {
        materialXML = ( FuGEBioMaterialMaterialType ) generateRandomIdentifiableXML( materialXML );

        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            materialXML.getContactRole().add( generateRandomContactRoleXML() );
        }

        if ( rootXML.getOntologyCollection() != null ) {
            FuGEBioMaterialMaterialType.MaterialType materialTypeXML = new FuGEBioMaterialMaterialType.MaterialType();
            materialTypeXML.setOntologyTermRef(
                    rootXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
            materialXML.setMaterialType( materialTypeXML );

            for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
                FuGEBioMaterialMaterialType.Characteristics characteristicXML = new FuGEBioMaterialMaterialType.Characteristics();
                characteristicXML.setOntologyTermRef(
                        rootXML.getOntologyCollection().getOntologyTerm().get( i ).getValue().getIdentifier() );
                materialXML.getCharacteristics().add( characteristicXML );
            }

            for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
                FuGEBioMaterialMaterialType.QualityControlStatistics qcsXML = new FuGEBioMaterialMaterialType.QualityControlStatistics();
                qcsXML.setOntologyTermRef(
                        rootXML.getOntologyCollection().getOntologyTerm().get( i ).getValue().getIdentifier() );
                materialXML.getQualityControlStatistics().add( qcsXML );
            }
        }
        return materialXML;
    }

    /**
     * @param identifiableXML the JAXB2 object that is returned with attributes filled
     * @return a randomly-filled Identifiable JAXB2 object
     */
    private FuGECommonIdentifiableType generateRandomIdentifiableXML( FuGECommonIdentifiableType identifiableXML ) {

        FuGEIdentifier identifierMaker = FuGEIdentifierFactory.createFuGEIdentifier( null, null );

        identifiableXML = ( FuGECommonIdentifiableType ) generateRandomDescribableXML( identifiableXML );
        identifiableXML.setIdentifier( identifierMaker.create( null ) );
        identifiableXML.setName( String.valueOf( Math.random() ) );

        // this ensures that if smaller objects (like DatabaseEntry) are being created, there is no unneccessary attempt
        //  to create sub-objects, and additionally there will be no infinite recursion
        if ( identifiableXML instanceof FuGECollectionFuGEType ) {
            FuGECollectionFuGEType fuGEType = ( FuGECollectionFuGEType ) identifiableXML;
            for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
                FuGECommonReferencesDatabaseReferenceType databaseReferenceXML = new FuGECommonReferencesDatabaseReferenceType();
                databaseReferenceXML = ( FuGECommonReferencesDatabaseReferenceType ) generateRandomDescribableXML(
                        databaseReferenceXML );
                databaseReferenceXML.setAccession( String.valueOf( Math.random() ) );
                databaseReferenceXML.setAccessionVersion( String.valueOf( Math.random() ) );

                // This is a reference to another object, so create that object before setting the reference
                if ( fuGEType.getReferenceableCollection() == null ) {
                    generateRandomReferenceableCollectionXML();
                }
                // get the first object and make it what is referred.
                databaseReferenceXML
                        .setDatabaseRef( fuGEType.getReferenceableCollection().getDatabase().get( i ).getIdentifier() );
                fuGEType.getDatabaseReference().add( databaseReferenceXML );
            }

            for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
                FuGECommonIdentifiableType.BibliographicReferences brRefXML = new FuGECommonIdentifiableType.BibliographicReferences();
                // This is a reference to another object, so create that object before setting the reference
                if ( fuGEType.getReferenceableCollection() == null ) {
                    generateRandomReferenceableCollectionXML();
                }
                // get the first object and make it what is referred.
                brRefXML.setBibliographicReferenceRef(
                        fuGEType.getReferenceableCollection().getBibliographicReference().get( i ).getIdentifier() );
                fuGEType.getBibliographicReferences().add( brRefXML );
            }
            return fuGEType;
        }
        return identifiableXML;

    }

    /**
     * Generates a random JAXB2 ReferenceableCollection element and adds it to the rootXML JAXB2 object
     */
    private void generateRandomReferenceableCollectionXML() {

        FuGECollectionReferenceableCollectionType refCollXML = new FuGECollectionReferenceableCollectionType();

        refCollXML = ( FuGECollectionReferenceableCollectionType ) generateRandomDescribableXML( refCollXML );

        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            FuGECommonReferencesBibliographicReferenceType bibRefXML = new FuGECommonReferencesBibliographicReferenceType();
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
                            10 ) ); // int will not be null, though it may be zero. Assume we won't print if it's zero.

            refCollXML.getBibliographicReference().add( bibRefXML );
        }

        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            FuGECommonReferencesDatabaseType dbXML = new FuGECommonReferencesDatabaseType();

            dbXML = ( FuGECommonReferencesDatabaseType ) generateRandomIdentifiableXML( dbXML );
            dbXML.setURI( String.valueOf( Math.random() ) );
            dbXML.setVersion( String.valueOf( Math.random() ) );
            for ( int ii = 0; ii < NUMBER_ELEMENTS; ii++ ) {
                if ( rootXML.getAuditCollection() == null ) {
                    generateRandomAuditCollectionXML();
                }
                if ( rootXML.getOntologyCollection() == null ) {
                    generateRandomOntologyCollectionXML();
                }
                dbXML.getContactRole().add( generateRandomContactRoleXML() );
            }
            refCollXML.getDatabase().add( dbXML );
        }
        rootXML.setReferenceableCollection( refCollXML );

    }

    /**
     * this method is a litte different from the other generateRandomXML, in that it needs to return
     * a contactRole type, so all creation of audit and ontology terms must have already happened outside
     * this method.
     *
     * @return a randomly-generated ContactRole JAXB2 object
     */
    private FuGECommonAuditContactRoleType generateRandomContactRoleXML() {

        FuGECommonAuditContactRoleType contactRoleXML = new FuGECommonAuditContactRoleType();
        contactRoleXML = ( FuGECommonAuditContactRoleType ) generateRandomDescribableXML( contactRoleXML );

        contactRoleXML.setContactRef( rootXML.getAuditCollection().getContact().get( 0 ).getValue().getIdentifier() );

        FuGECommonAuditContactRoleType.Role roleXML = new FuGECommonAuditContactRoleType.Role();
        roleXML.setOntologyTermRef(
                rootXML.getOntologyCollection().getOntologyTerm().get( 0 ).getValue().getIdentifier() );
        contactRoleXML.setRole( roleXML );

        return contactRoleXML;
    }

    /**
     * @param describableXML the JAXB2 object that is returned with attributes filled
     * @return a randomly-generated Describable JAXB2 object
     */
    private FuGECommonDescribableType generateRandomDescribableXML( FuGECommonDescribableType describableXML ) {

        // at the moment there is nothing outside the class check if-statement.

        // this ensures that if smaller objects (like DatabaseEntry) are being created, there is no unneccessary attempt
        //  to create sub-objects, and additionally there will be no infinite recursion

        // create jaxb object
        FuGECommonDescribableType.AuditTrail auditsXML = new FuGECommonDescribableType.AuditTrail();

        // It's only worth creating a single audit option (CREATION), as the method runs so quickly that the
        // timestamp is identical for all of them.
//        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
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
            System.err
                    .println( "Error trying to set the Audit element's date to the current time/date. Not setting." );
            e.printStackTrace();
        }

        // @todo options are hardcoded: is this really the only/best way?
        // Only create the first time.
        auditXML.setAction( "CREATION" );
        if ( describableXML instanceof FuGECollectionFuGEType ) {
            FuGECollectionFuGEType fuGEType = ( FuGECollectionFuGEType ) describableXML;
            if ( fuGEType.getAuditCollection() == null ) {
                generateRandomAuditCollectionXML();
            }
            auditXML.setContactRef(
                    fuGEType.getAuditCollection().getContact().get( 0 ).getValue().getIdentifier() );
            describableXML = fuGEType;
        }

        // add to collection
        auditsXML.getAudit().add( auditXML );
//        }

        // load jaxb object into fuGEType
        describableXML.setAuditTrail( auditsXML );

        // create fuge object for 0 or 1 descriptions (optional), which contain 1 to many Description elements.

        // create jaxb objects
        FuGECommonDescribableType.Descriptions descriptionsXML = new FuGECommonDescribableType.Descriptions();

        // set jaxb object
        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {

            // create singular jaxb object
            FuGECommonDescriptionDescriptionType descriptionXML = new FuGECommonDescriptionDescriptionType();

            // set jaxb object
            if ( describableXML instanceof FuGECollectionFuGEType )
                descriptionXML = ( FuGECommonDescriptionDescriptionType ) generateRandomDescribableXML( descriptionXML );
            descriptionXML.setText( String.valueOf( Math.random() ) );

            // add to collection of objects
            descriptionsXML.getDescription().add( descriptionXML );
        }
        // load jaxb object into fuGEType
        describableXML.setDescriptions( descriptionsXML );

        // create fuge object for any number of annotations (optional), which contains one required OntologyTerm_ref
        if ( describableXML instanceof FuGECollectionFuGEType ) {
            FuGECollectionFuGEType fuGEType = ( FuGECollectionFuGEType ) describableXML;
            for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
                FuGECommonDescribableType.Annotations annotationXML = new FuGECommonDescribableType.Annotations();
                if ( fuGEType.getOntologyCollection() == null ) {
                    generateRandomOntologyCollectionXML();
                }
                annotationXML.setOntologyTermRef(
                        fuGEType.getOntologyCollection().getOntologyTerm().get( i ).getValue().getIdentifier() );
                fuGEType.getAnnotations().add( annotationXML );
                describableXML = fuGEType;
            }
        }

        FuGECommonDescribableType.Uri uriElementXML = new FuGECommonDescribableType.Uri();
        FuGECommonDescriptionURIType uriXML = new FuGECommonDescriptionURIType();

        // set jaxb object
        if ( describableXML instanceof FuGECollectionFuGEType )
            uriXML = ( FuGECommonDescriptionURIType ) generateRandomDescribableXML( uriXML );
        uriXML.setLocation( "http://some.random.url/" + String.valueOf( Math.random() ) );

        // load jaxb object into fuGEType
        uriElementXML.setURI( uriXML );
        describableXML.setUri( uriElementXML );

        // create fuge object for 0 or 1 propertySets, which contain at least 1 NameValueType element
        // create jaxb objects
        FuGECommonDescribableType.PropertySets propertySetsXML = new FuGECommonDescribableType.PropertySets();

        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            // create singular jaxb object
            FuGECommonDescriptionNameValueTypeType nameValueTypeXML = new FuGECommonDescriptionNameValueTypeType();

            // set jaxb object
            if ( describableXML instanceof FuGECollectionFuGEType )
                nameValueTypeXML = ( FuGECommonDescriptionNameValueTypeType ) generateRandomDescribableXML(
                        nameValueTypeXML );
            nameValueTypeXML.setName( String.valueOf( Math.random() ) );
            nameValueTypeXML.setType( String.valueOf( Math.random() ) );
            nameValueTypeXML.setValue( String.valueOf( Math.random() ) );

            // load jaxb object into collection
            propertySetsXML.getNameValueType().add( nameValueTypeXML );
        }

        // load jaxb object into describable
        describableXML.setPropertySets( propertySetsXML );

        // load jaxb security object reference into fuGEType
        if ( describableXML instanceof FuGECollectionFuGEType ) {
            FuGECollectionFuGEType fuGEType = ( FuGECollectionFuGEType ) describableXML;
            if ( fuGEType.getAuditCollection() == null ) {
                generateRandomAuditCollectionXML();
            }
            fuGEType.setSecurityRef( fuGEType.getAuditCollection().getSecurity().get( 0 ).getIdentifier() );
            describableXML = fuGEType;
        }
        return describableXML;

    }

    /**
     * Generates a random JAXB2 AuditCollection element and adds it to the rootXML JAXB2 object
     */
    private void generateRandomAuditCollectionXML() {
        // create jaxb object
        FuGECollectionAuditCollectionType auditCollXML = new FuGECollectionAuditCollectionType();

        // set describable information
        auditCollXML = ( FuGECollectionAuditCollectionType ) generateRandomDescribableXML( auditCollXML );

        // Contacts
        String firstOrg = null;
        ObjectFactory factory = new ObjectFactory();
        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            // create jaxb object
            FuGECommonAuditOrganizationType organizationXML = new FuGECommonAuditOrganizationType();

            // set jaxb object
            organizationXML = ( FuGECommonAuditOrganizationType ) generateRandomContactXML( organizationXML );

            // set organization traits - only set a parent if i > 0.

            if ( i > 0 ) {
                FuGECommonAuditOrganizationType.Parent parentOrganizationXML = new FuGECommonAuditOrganizationType.Parent();
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
        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            FuGECommonAuditSecurityGroupType sgXML = new FuGECommonAuditSecurityGroupType();
            sgXML = ( FuGECommonAuditSecurityGroupType ) generateRandomIdentifiableXML( sgXML );

            for ( int ii = 0; ii < NUMBER_ELEMENTS; ii++ ) {
                FuGECommonAuditSecurityGroupType.Members memXML = new FuGECommonAuditSecurityGroupType.Members();
                memXML.setContactRef( auditCollXML.getContact().get( ii ).getValue().getIdentifier() );
                sgXML.getMembers().add( memXML );
            }
            auditCollXML.getSecurityGroup().add( sgXML );
        }

        // fixme should owner really be a collection??
        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            FuGECommonAuditSecurityType securityXML = new FuGECommonAuditSecurityType();
            securityXML = ( FuGECommonAuditSecurityType ) generateRandomIdentifiableXML( securityXML );

            for ( int ii = 0; ii < NUMBER_ELEMENTS; ii++ ) {
                FuGECommonAuditSecurityType.Owners ownersXml = new FuGECommonAuditSecurityType.Owners();
                ownersXml.setContactRef( auditCollXML.getContact().get( ii ).getValue().getIdentifier() );
                securityXML.getOwners().add( ownersXml );
            }

            for ( int ii = 0; ii < NUMBER_ELEMENTS; ii++ ) {
                FuGECommonAuditSecurityAccessType accessXML = new FuGECommonAuditSecurityAccessType();
                accessXML = ( FuGECommonAuditSecurityAccessType ) generateRandomDescribableXML( accessXML );

                FuGECommonAuditSecurityAccessType.AccessRight accessRightXML = new FuGECommonAuditSecurityAccessType.AccessRight();
                if ( rootXML.getOntologyCollection() == null ) {
                    generateRandomOntologyCollectionXML();
                }
                accessRightXML.setOntologyTermRef(
                        rootXML.getOntologyCollection().getOntologyTerm().get( ii ).getValue().getIdentifier() );
                accessXML.setAccessRight( accessRightXML );

                accessXML.setSecurityGroupRef( auditCollXML.getSecurityGroup().get( ii ).getIdentifier() );
                securityXML.getSecurityAccess().add( accessXML );
            }
            auditCollXML.getSecurity().add( securityXML );
        }

        rootXML.setAuditCollection( auditCollXML );

    }

    /**
     * Generates a random JAXB2 OntologyCollection element and adds it to the rootXML JAXB2 object
     */
    private void generateRandomOntologyCollectionXML() {
        FuGECollectionOntologyCollectionType ontoCollXML = new FuGECollectionOntologyCollectionType();

        ontoCollXML = ( FuGECollectionOntologyCollectionType ) generateRandomDescribableXML( ontoCollXML );

        // set ontology sources
        FuGECommonOntologyOntologySourceType ontoSourceXML = null;
        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            ontoSourceXML = new FuGECommonOntologyOntologySourceType();
            ontoSourceXML = ( FuGECommonOntologyOntologySourceType ) generateRandomIdentifiableXML( ontoSourceXML );
            ontoSourceXML.setOntologyURI( String.valueOf( Math.random() ) );
            ontoCollXML.getOntologySource().add( ontoSourceXML );
        }

        // set ontology terms
        ObjectFactory factory = new ObjectFactory();
        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            ontoCollXML.getOntologyTerm()
                    .add( factory.createOntologyIndividual( generateRandomOntologyIndividualXML( ontoSourceXML ) ) );
        }

        rootXML.setOntologyCollection( ontoCollXML );
    }

    /**
     * Calls the other generateRandomOntologyIndividualXML method with a default boolean value of false, to
     * say that it isn't being generated inside the for-loop that's counting the number of repeated XML elements to make
     *
     * @param ontoSourceXML the OntologySource JAXB2 object to associate with this OntologyIndividual
     * @return a randomly-generated OntologyIndividual JAXB2 object
     */
    private FuGECommonOntologyOntologyIndividualType generateRandomOntologyIndividualXML(
            FuGECommonOntologyOntologySourceType ontoSourceXML ) {
        return generateRandomOntologyIndividualXML( ontoSourceXML, false );
    }

    /**
     * @param ontoSourceXML the OntologySource JAXB2 object to associate with this OntologyIndividual
     * @param inner         whether or not this is being called from within this method
     * @return a randomly-generated OntologyIndividual JAXB2 object
     */
    private FuGECommonOntologyOntologyIndividualType generateRandomOntologyIndividualXML(
            FuGECommonOntologyOntologySourceType ontoSourceXML, boolean inner ) {

        FuGECommonOntologyOntologyIndividualType ontologyIndividualXML = new FuGECommonOntologyOntologyIndividualType();
        ontologyIndividualXML = ( FuGECommonOntologyOntologyIndividualType ) generateRandomOntologyTermXML(
                ontologyIndividualXML, ontoSourceXML );

        if ( !inner ) {
            ObjectFactory factory = new ObjectFactory();
            for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {

                FuGECommonOntologyObjectPropertyType objectPropertyXML = new FuGECommonOntologyObjectPropertyType();
                objectPropertyXML = ( FuGECommonOntologyObjectPropertyType ) generateRandomIdentifiableXML(
                        objectPropertyXML );
                objectPropertyXML = ( FuGECommonOntologyObjectPropertyType ) generateRandomOntologyTermXML(
                        objectPropertyXML, ontoSourceXML );
                for ( int ii = 0; ii < NUMBER_ELEMENTS; ii++ ) {
                    objectPropertyXML.getOntologyIndividual()
                            .add( generateRandomOntologyIndividualXML( ontoSourceXML, true ) );
                }
                ontologyIndividualXML.getOntologyProperty().add( factory.createObjectProperty( objectPropertyXML ) );

                FuGECommonOntologyDataPropertyType dataPropertyXML = new FuGECommonOntologyDataPropertyType();
                dataPropertyXML = ( FuGECommonOntologyDataPropertyType ) generateRandomIdentifiableXML( dataPropertyXML );
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
    private FuGECommonOntologyOntologyTermType generateRandomOntologyTermXML(
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
    private FuGECommonAuditContactType generateRandomContactXML( FuGECommonAuditContactType contactXML ) {
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
    private FuGECommonAuditPersonType generateRandomPersonXML(
            FuGECommonAuditPersonType personXML,
            FuGECommonAuditOrganizationType organizationXML ) {

        // set person traits
        personXML.setFirstName( String.valueOf( Math.random() ) );
        personXML.setLastName( String.valueOf( Math.random() ) );
        personXML.setMidInitials( String.valueOf( Math.random() ) );

        for ( int i = 0; i < NUMBER_ELEMENTS; i++ ) {
            FuGECommonAuditPersonType.Affiliations affiliationXML = new FuGECommonAuditPersonType.Affiliations();

            affiliationXML.setOrganizationRef( organizationXML.getIdentifier() );
            personXML.getAffiliations().add( affiliationXML );
        }
        return personXML;
    }

}
