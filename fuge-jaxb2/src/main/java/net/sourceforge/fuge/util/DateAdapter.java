package net.sourceforge.fuge.util;

import javax.xml.bind.DatatypeConverter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
public class DateAdapter {
    public static Date parseDate( String s ) {
        return DatatypeConverter.parseDateTime( s ).getTime();
    }

    public static String printDate( Date dt ) {
        Calendar cal = new GregorianCalendar();
        cal.setTime( dt );
        return DatatypeConverter.printDateTime( cal );
    }
}
