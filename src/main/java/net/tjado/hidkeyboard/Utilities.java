/**
 * Authorizer
 *
 *  Copyright 2016 by Tjado Mäcke <tjado@maecke.de>
 *  Licensed under GNU General Public License 3.0.
 *
 * @license GPL-3.0 <https://opensource.org/licenses/GPL-3.0>
 */

package net.tjado.hidkeyboard;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Utilities
{
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static final boolean DEBUG = true;

    public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();

        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }

        return builder.toString();
    }


    /** Log a debug message at info level */
    public static void dbginfo(String tag, String msg)
    {
        if (DEBUG)
            LOGGER.log(Level.INFO, tag + " : " + msg);
    }

    /** Log a debug message and exception at info level */
    public static void dbginfo(String tag, Throwable t, String msg)
    {
        if (DEBUG)
            LOGGER.log(Level.INFO, tag + " : " + msg, t);
    }
}
