/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 03.04.20, 19:48
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

package net.pretronic.libraries.message.bml.function.defaults.text;

import net.pretronic.libraries.message.bml.builder.BuildContext;
import net.pretronic.libraries.message.bml.function.ParametrizedFunction;
import net.pretronic.libraries.utility.GeneralUtil;
import net.pretronic.libraries.utility.StringUtil;

public class RandomTextFunction implements ParametrizedFunction {

    private static final int DEFAULT_SIZE = 5;

    @Override
    public Object execute(BuildContext context, Object[] parameters) {
        int size = DEFAULT_SIZE;
        if(parameters.length == 1){
            if(parameters[0] instanceof Integer){
                size = (int) parameters[0];
            }else if(GeneralUtil.isNaturalNumber((String) parameters[0])){
                size = Integer.parseInt((String) parameters[0]);
            }
        }else if(parameters.length > 1){
            throw new IllegalArgumentException("Invalid parameter length");
        }
        return StringUtil.getRandomString(size);
    }
}
