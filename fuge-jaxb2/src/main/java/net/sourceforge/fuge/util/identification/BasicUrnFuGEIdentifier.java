package net.sourceforge.fuge.util.identification;

import java.util.UUID;

/**
 * A simple example FuGEIdentifier class that returns a random UUID prefixed with the classname and domain name of the
 * requested by the calling class.
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
public class BasicUrnFuGEIdentifier extends FuGEIdentifier {
    /**
     * Constructor
     *
     * @param domainName The domain name to build into your identifier
     */
    public BasicUrnFuGEIdentifier( String domainName ) {
        super( domainName );
    }

    /**
     * @param className the class name to append to the urn.
     * @return the new identifier
     */
    public String create( String className ) {
        String entityType = className.substring( className.lastIndexOf( "." ) + 1 );
        return "urn:" + getDomainName() + ":" + entityType + ":" + UUID.randomUUID().toString();
    }
}