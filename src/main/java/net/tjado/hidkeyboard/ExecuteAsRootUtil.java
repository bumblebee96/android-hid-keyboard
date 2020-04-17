/**
 * Authorizer
 *
 *  Copyright 2016 by Tjado Mäcke <tjado@maecke.de>
 *  Licensed under GNU General Public License 3.0.
 *
 * @license GPL-3.0 <https://opensource.org/licenses/GPL-3.0>
 */

package net.tjado.hidkeyboard;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Class by muzikant <http://stackoverflow.com/users/624109/muzikant>
 *
 * Reference:
 * http://muzikant-android.blogspot.com/2011/02/how-to-get-root-access-and-execute.html
 * http://stackoverflow.com/a/7102780
 */

public class ExecuteAsRootUtil
{
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static boolean canRunRootCommands()
    {
        boolean retval = false;
        Process suProcess;

        try
        {
            suProcess = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            //DataInputStream osRes = new DataInputStream(suProcess.getInputStream());
            BufferedReader osRes
                    = new BufferedReader(new InputStreamReader(suProcess.getInputStream()));

            if (null != os && null != osRes)
            {
                // Getting the id of the current user to check if this is root
                os.writeBytes("id\n");
                os.flush();

                String currUid = osRes.readLine();
                boolean exitSu = false;
                if (null == currUid)
                {
                    retval = false;
                    exitSu = false;
                    LOGGER.log(Level.INFO, "ROOT : Can't get root access or denied by user");
                }
                else if (true == currUid.contains("uid=0"))
                {
                    retval = true;
                    exitSu = true;
                    LOGGER.log(Level.INFO, "ROOT : Root access granted");
                }
                else
                {
                    retval = false;
                    exitSu = true;
                    LOGGER.log(Level.INFO, "ROOT : Root access rejected: " + currUid);
                }

                if (exitSu)
                {
                    os.writeBytes("exit\n");
                    os.flush();
                }
            }
        }
        catch (Exception e)
        {
            // Can't get root !
            // Probably broken pipe exception on trying to write to output stream (os) after su failed, meaning that the device is not rooted

            retval = false;
            LOGGER.log(Level.INFO, "ROOT : Root access rejected [" + e.getClass().getName() + "] : " + e.getMessage());
        }

        return retval;
    }

    public static final boolean execute(String command)
    {
        boolean retval = false;

        try
        {
            if (command != null && command.length() > 0)
            {
                Process suProcess = Runtime.getRuntime().exec("su");

                DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());

                os.writeBytes(command);
                os.flush();

                os.writeBytes("exit\n");
                os.flush();

                try
                {
                    int suProcessRetval = suProcess.waitFor();
                    if (255 != suProcessRetval)
                    {
                        // Root access granted
                        retval = true;
                    }
                    else
                    {
                        // Root access denied
                        retval = false;
                    }
                }
                catch (Exception ex)
                {
                    LOGGER.log(Level.WARNING, "ROOT : Error executing root action", ex);
                }
            }
        }
        catch (IOException ex)
        {
            LOGGER.log(Level.WARNING, "ROOT : Can't get root access", ex);
        }
        catch (SecurityException ex)
        {
            LOGGER.log(Level.WARNING, "ROOT : Can't get root access", ex);
        }
        catch (Exception ex)
        {
            LOGGER.log(Level.WARNING, "ROOT : Error executing internal operation", ex);
        }

        return retval;
    }
}