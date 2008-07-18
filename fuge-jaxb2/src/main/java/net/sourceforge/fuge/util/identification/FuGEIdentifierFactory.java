package net.sourceforge.fuge.util.identification;

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
public class FuGEIdentifierFactory {
    /**
     * Creates an instance of the appropriate subclass of FuGEIdentifier. The subclass is selected based on information
     * received from the identifierType.
     * <p/>
     * The LSID Assigner is currently not set up in this STK, as I will wait for general consensus on the list before
     * including in in this STK. However, since the LSID Assigner is needed for the hibernate STK, I am retaining the
     * "location" parameter so that people won't have to change their code if it comes into existence later on.
     * <p/>
     * Remember, just pass null as the argument to this method for either the domainName or the location, or both, depending
     * on the type of identifier you want.
     * <p/>
     * You can also create your own implementation of FuGEIdentifier. Create your class that extends FuGEIdentifier, then
     * update createFuGEIdentifier accordingly, keeping the parameters that go into it identical to their original state.
     *
     * @param domainName The domain name you want your identifier to include. If null, will create a BasicFuGEIdentifier
     * @param location   The location of the assigning service, if present. If null, will create a BasicUrnFuGEIdentifier.
     * @return the appropriate subclass of FuGEidentifier
     */
    public static FuGEIdentifier createFuGEIdentifier( String domainName, String location ) {
        if ( domainName == null || domainName.trim().length() == 0 ) {
            return new BasicFuGEIdentifier( null );
        }
//        if ( location != null && location.length() > 0 ) {
//            return new LSIDFuGEIdentifier( domainName, location );
//        }
        return new BasicUrnFuGEIdentifier( domainName );
    }

}
