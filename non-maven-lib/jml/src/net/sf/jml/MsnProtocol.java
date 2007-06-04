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
package net.sf.jml;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MSN Protocol, See
 * <a href="http://www.hypothetic.org/docs/msn/general/overview.php">http://www.hypothetic.org/docs/msn/general/overview.php</a>
 * "What is the MSN Messenger protocol" section.
 * 
 * @author Roger Chen
 */
public final class MsnProtocol {

    public static final MsnProtocol MSNP8 = new MsnProtocol("MSNP8", 8);
    public static final MsnProtocol MSNP9 = new MsnProtocol("MSNP9", 9);
    public static final MsnProtocol MSNP10 = new MsnProtocol("MSNP10", 10);
    public static final MsnProtocol MSNP11 = new MsnProtocol("MSNP11", 11);
    public static final MsnProtocol MSNP12 = new MsnProtocol("MSNP12", 12);

    private static Map protocolMap = new LinkedHashMap();

    static {
        protocolMap.put(MSNP12.toString().toUpperCase(), MSNP12);
        protocolMap.put(MSNP11.toString().toUpperCase(), MSNP11);
        protocolMap.put(MSNP10.toString().toUpperCase(), MSNP10);
        protocolMap.put(MSNP9.toString().toUpperCase(), MSNP9);
        protocolMap.put(MSNP8.toString().toUpperCase(), MSNP8);
    }

    public static MsnProtocol parseStr(String s) {
        return s == null ? null : (MsnProtocol) protocolMap
                .get(s.toUpperCase());
    }

    public static MsnProtocol[] getAllSupportedProtocol() {
        return (MsnProtocol[]) protocolMap.values().toArray(new MsnProtocol[0]);
    }

    private final String protocol;
    private final int version;

    private MsnProtocol(String protocol, int version) {
        this.protocol = protocol;
        this.version = version;
    }

    public boolean before(MsnProtocol protocol) {
        return version < protocol.version;
    }

    public boolean after(MsnProtocol protocol) {
        return version > protocol.version;
    }

    public String toString() {
        return protocol;
    }
}
