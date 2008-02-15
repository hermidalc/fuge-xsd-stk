package net.sourceforge.fuge.util.identification;

import java.util.UUID;

/**
 * A very basic example FuGEIdentifier class that just returns a random UUID.
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
public class BasicFuGEIdentifier extends FuGEIdentifier {
    /**
     * Constructor
     *
     * @param domainName The domain name to build into your identifier. May be null, and will be ignored.
     */
    public BasicFuGEIdentifier( String domainName ) {
        super( domainName );
    }

    /**
     * Please note that this is such a basic identifier creation class that the className will be ignored.
     *
     * @param className currently ignored
     * @return the new identifier
     */
    public String create( String className ) {
        return UUID.randomUUID().toString();
    }

}
