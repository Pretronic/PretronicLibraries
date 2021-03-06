/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:45
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
import net.pretronic.libraries.logging.handler.LogHandler;

import java.util.concurrent.BlockingQueue;

/**
 * This object is a async que runner, it is made for printing log messages in an async thread.
 *
 * <p>This is a daemon thread with minimal priority.</p>
 */
public class AsyncQueuePrinter extends Thread {

    private final PretronicLogger logger;
    private final BlockingQueue<LogRecord> queue;

    public AsyncQueuePrinter(PretronicLogger logger, BlockingQueue<LogRecord> queue){
        super("Pretronic Queue Printer");
        this.logger = logger;
        this.queue = queue;
        setPriority(Thread.MIN_PRIORITY);
        setDaemon(true);
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            try {
                LogRecord record = queue.take();
                String result = logger.getFormatter().format(logger,record);
                for(LogHandler handler : logger.getHandlers()) handler.handleLog(record,result);
            }catch (Exception exception) {
                System.out.println("[Logger-Exception] An error in logging service occurred:");
                StringBuilder builder = new StringBuilder();
                FormatHelper.buildStackTrace(builder,Thread.currentThread(),exception,"[Logger-Exception]");
                System.out.println(builder.toString());
            }
        }
    }
}
