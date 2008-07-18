package net.sourceforge.fuge.util;

import net.sourceforge.fuge.util.generatedJAXB2.FuGECollectionFuGEType;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
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
public class XmlRoundTrip {

    /**
     * XmlRoundTrip is an example main() that shows users how to load, validate, and output XML generated
     * via the AndroMDA build process.
     *
     * @param args provide 3 arguments in this order: schema-file input-xml-file output-xml-file
     * @throws Exception if the number of arguments isn't 3.
     */
    public static void main( String[] args ) throws Exception {
        if ( args.length != 3 )
            throw new java.lang.Exception(
                    "You must provide 3 arguments in this order: schema-file input-xml-file output-xml-file" );

        // create a JAXBContext capable of handling classes generated in the net.sourceforge.fuge.util.generatedJAXB2
        // package
        JAXBContext jc = JAXBContext.newInstance( "net.sourceforge.fuge.util.generatedJAXB2" );

        // create an Unmarshaller
        Unmarshaller u = jc.createUnmarshaller();

        // Sort out validation settings
        SchemaFactory sf = SchemaFactory.newInstance( W3C_XML_SCHEMA_NS_URI );
        Schema schema = sf.newSchema( new File( args[0] ) );

        // set a schema for validation. If you do not wish a validation step, you may comment-out this section.
        u.setSchema( schema );

        // unmarshal a net.sourceforge.fuge.util.generatedJAXB2 instance document into a tree of Java content
        // objects composed of classes from the net.sourceforge.fuge.util.generatedJAXB2 package.
        JAXBElement<?> genericTopLevelElement = ( JAXBElement<?> ) u.unmarshal( new FileInputStream( args[1] ) );

        // Get the jaxb root object. In the case of the default setup of the FuGE XSD STK, this is a
        // FuGECollectionFuGEType object. However, if you are developing a community extension, this may
        // be a different type of object. Please modify as appropriate.
        FuGECollectionFuGEType rootXML = ( FuGECollectionFuGEType ) genericTopLevelElement.getValue();

        //
        // Here is where you would perfom any checks or manipulation of the FuGE objects.
        //

        // As an example, print out the top-level identifier for the FuGE object. If you run the validation against
        // the schema, then there must be an identifier, as it is a mandatory attribute. However, this code will
        // check for the presence of the identifier, as if you don't run the validation step, you might have gotten
        // to this stage without an identifier.
        if ( rootXML.getIdentifier() != null && rootXML.getIdentifier().trim().length() > 0 ) {
            System.err.println( "Root Element Identifier is " + rootXML.getIdentifier() );
        }

        // Output the file
        // create a Marshaller
        Marshaller m = jc.createMarshaller();
        m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );

        // set the correct schema
        m.setSchema( schema );

        OutputStream os = new FileOutputStream( args[2] );
        @SuppressWarnings( "unchecked")
        JAXBElement element = new JAXBElement( new QName( "http://fuge.sourceforge.net/fuge/1.0", "FuGE" ),
                        FuGECollectionFuGEType.class,
                        rootXML );
        m.marshal( element, os );

    }
}
