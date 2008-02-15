package net.sourceforge.fuge.util.identification;

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
