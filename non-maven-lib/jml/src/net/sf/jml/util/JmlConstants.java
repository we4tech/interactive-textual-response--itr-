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
package net.sf.jml.util;

/**
 * Some constants used in jml.
 * 
 * @author Roger Chen
 */
public final class JmlConstants {

    /**
     * Default encoding.
     */
    public static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Line separator.
     */
    public static final String LINE_SEPARATOR = "\r\n";

    /**
     * Fast ssl login. Use the default login url to login.
     */
    public static final boolean FAST_SSL_LOGIN = Integer.getInteger(
            "net.sf.jml.fastSslLogin", 0).intValue() == 1;

    /**
     * The MsnMessageChain length.
     */
    public static final int MESSAGE_CHAIN_LENGTH = Integer.getInteger(
            "net.sf.jml.messageChainLength", 20).intValue();

}