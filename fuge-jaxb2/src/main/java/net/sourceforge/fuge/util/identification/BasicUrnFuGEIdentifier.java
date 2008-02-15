package net.sourceforge.fuge.util.identification;

import java.util.UUID;

/**
 * A simple example FuGEIdentifier class that returns a random UUID prefixed with the classname and domain name of the
 * requested by the calling class.
 * <p/>
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