/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 03.04.20, 18:21
 * @web %web%
 *
 * The PretronicLibraries Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package net.pretronic.libraries.logging;

import net.pretronic.libraries.logging.format.FormatHelper;
import net.pretronic.libraries.logging.level.DebugLevel;
import net.pretronic.libraries.logging.level.LogLevel;

/**
 * This class helps develops implementing debugs in there application. It simplifies the printing of debugs.
 */
public final class Debug {

    private static PretronicLogger LOGGER;
    private static LogLevel LOG_LEVEL = LogLevel.DEBUG;
    private static DebugLevel DEBUG_LEVEL = DebugLevel.NORMAL;

    /**
     * Set the debug output logger.
     * @param logger The logger
     */
    public static void setLogger(PretronicLogger logger){
        LOGGER = logger;
    }

    /**
     * Set the log output level (Default: DEBUG)
     *
     * @param lofLevel The log level
     */
    public static void setLogLevel(LogLevel lofLevel){
        LOG_LEVEL = lofLevel;
    }

    /**
     * Set the debug level (Default: NORMAL)
     *
     * @param lofLevel The debug level
     */
    public static void setDebugLevel(DebugLevel lofLevel){
        DEBUG_LEVEL = lofLevel;
    }

    /**
     * Print a simple debug message.
     * @param message The log message
     */
    public static void print(String message){
        if(LOGGER == null) throw new IllegalArgumentException("Debug logger is not set");
        if(LOG_LEVEL == LogLevel.DEBUG){
            LOGGER.debug(DEBUG_LEVEL,message);
        }else{
            LOGGER.log(LOG_LEVEL,message);
        }
    }

    /**
     * Print an object
     *
     * @param object The object
     */
    public static void print(Object object){
        print(object == null ? "null" : object.toString());
    }

    /**
     * Print a message and objects for the formatter.
     *
     * @param message The log message
     * @param objects The objects for the formatter
     */
    public static void print(String message, Object... objects){
        print(FormatHelper.format(message,objects));
    }

}
