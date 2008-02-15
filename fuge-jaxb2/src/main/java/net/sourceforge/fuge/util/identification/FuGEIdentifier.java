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
abstract public class FuGEIdentifier {
    private String domainName;

    /**
     * Constructor
     *
     * @param domainName The domain name to build into your identifier
     */
    public FuGEIdentifier( String domainName ) {
        this.domainName = domainName;
    }

    /**
     * This method returns a String that is a new FuGE identifier, according to the rules
     * of the concrete subclass used.
     *
     * @param className
     * @return a FuGE identifier suitable for filling in an Identifiable class' identifier attribute
     */
    abstract public String create( String className );

    public String getDomainName() {
        return domainName;
    }
}
