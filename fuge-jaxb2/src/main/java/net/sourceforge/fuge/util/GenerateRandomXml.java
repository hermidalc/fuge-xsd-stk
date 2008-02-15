package net.sourceforge.fuge.util;

import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

/**
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
 * This file is based on code originally part of SyMBA (http://symba.sourceforge.net).
 * SyMBA is covered under the GNU Lesser General Public License (LGPL).
 * Copyright (C) 2007 jointly held by Allyson Lister, Olly Shaw, and their employers.
 * <p/>
 * $LastChangedDate$
 * $LastChangedRevision$
 * $Author$
 * $HeadURL$
 */
public class GenerateRandomXml {

    /**
     * Read in user-provided arguments and produce 3 files of random XML based on the provided XSD. Please note that this
     * is only guaranteed to work with the FuGE V1 XSD. Any modifications or extensions to that XSD will require
     * changes in the RandomXmlGenerator class.
     *
     * @param args 2 arguments in this order: schema-file output-xml-file
     * @throws Exception if the number of arguments isn't 2
     */
    public static void main( String[] args ) throws Exception {

        try {
            if ( args.length != 2 )
                throw new java.lang.Exception( "You must provide 2 arguments in this order: schema-file output-xml-file" );

            RandomXmlGenerator xml = new RandomXmlGenerator( args[0], args[1] );
            xml.generate();

        } catch ( JAXBException je ) {
            System.err.println( "JAXB Exception:" );
//            try {
//                os.flush();
//                System.err.println( "Output buffer flushed." );
//            } catch ( IOException e ) {
//                System.err.println( "Internal IO Exception when flushing buffer" );
//                e.printStackTrace();
//            } catch ( NullPointerException e ) {
//                System.err.println( "Null Pointer Exception when flushing buffer" );
//                e.printStackTrace();
//            }
            je.printStackTrace();
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        } catch ( SAXException e ) {
            e.printStackTrace();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}
