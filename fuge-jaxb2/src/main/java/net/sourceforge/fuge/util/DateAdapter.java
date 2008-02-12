package net.sourceforge.fuge.util;

import javax.xml.bind.DatatypeConverter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/* SyMBA is covered under the GNU Lesser General Public License (LGPL).
 * Copyright (C) 2007 jointly held by Allyson Lister, Olly Shaw, and their employers.
 * To view the full licensing information for this software and ALL other files contained
 * in this distribution, please see LICENSE.txt
 *
 * $LastChangedDate: 2007-09-09 09:25:30 +0100 (Sun, 09 Sep 2007) $
 * $LastChangedRevision: 4 $
 * $Author: allysonlister $
 * $HeadURL: https://symba.svn.sourceforge.net/svnroot/symba/trunk/jaxb2/src/main/java/fugeOM/util/DateAdapter.java $
 *
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
