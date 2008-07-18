package net.sourceforge.fuge.util;

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
public class GenerateRandomXml {

    /**
     * Read in user-provided arguments and produce 3 files of random XML based on the provided XSD. Please note that this
     * is only guaranteed to work with the FuGE V1 XSD. Any modifications or extensions to that XSD will require
     * changes in the RandomXmlGenerator class.
     * <p/>
     * the output xml file (second argument) is used to generate the output filenames. For example, "output.xml"
     * would produce output0.xml, output1.xml, and output2.xml
     *
     * @param args 2 arguments in this order: schema-file output-xml-file
     * @throws Exception if the number of arguments isn't 2
     */
    public static void main( String[] args ) throws Exception {

        if ( args.length != 2 )
            throw new java.lang.Exception( "You must provide 2 arguments in this order: schema-file output-xml-file" );

        String name = args[1].substring( 0, args[1].lastIndexOf( "." ) );
        String ext = args[1].substring( args[1].lastIndexOf( "." ) );

        // make 3 versions of the file
        for ( int i = 0; i < 3; i++ ) {
            RandomXmlGenerator.generate( args[0], name + String.valueOf( i ) + ext );
        }
    }

}
