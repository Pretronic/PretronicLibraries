/*
 * (C) Copyright 2020 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 01.02.20, 18:13
 *
 * The PrematicLibraries Project is under the Apache License, version 2.0 (the "License");
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

package net.prematic.libraries.command;

import net.prematic.libraries.command.command.NotFoundHandler;
import net.prematic.libraries.command.sender.CommandSender;

public class MessageNotFoundHandler implements NotFoundHandler {

    private static MessageNotFoundHandler DEFAULT = new MessageNotFoundHandler("The command %command% was not found.");

    private final String message;

    public MessageNotFoundHandler(String message) {
        this.message = message;
    }

    @Override
    public void handle(CommandSender sender, String command, String[] args) {
        sender.sendMessage(message.replace("%command%",command));
    }

    public static MessageNotFoundHandler newDefaultHandler(){
        return DEFAULT;
    }
}