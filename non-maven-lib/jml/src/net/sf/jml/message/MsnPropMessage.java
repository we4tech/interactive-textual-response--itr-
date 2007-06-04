/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jml.message;

import java.nio.ByteBuffer;

import net.sf.jml.util.Charset;
import net.sf.jml.util.JmlConstants;
import net.sf.jml.util.StringHolder;

/**
 * The message's body contains key/value pairs. 
 * 
 * @author Roger Chen
 */
public abstract class MsnPropMessage extends MsnMimeMessage {

    protected final StringHolder properties = new StringHolder();

    public StringHolder getProperties() {
        return properties;
    }

    public String toString() {
        return super.toString() + properties.toString()
                + JmlConstants.LINE_SEPARATOR;
    }

    protected void parseBuffer(ByteBuffer buffer) {
        super.parseBuffer(buffer);
        properties.parseString(Charset.decode(buffer));
    }

}