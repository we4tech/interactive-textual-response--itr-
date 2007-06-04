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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

/**
 * Hold Strings.
 * 
 * @author Roger Chen
 */
public final class StringHolder implements Cloneable {

    private final Map properties = new LinkedHashMap();

    public Map getProperties() {
        return properties;
    }

    public String getProperty(String key) {
        return (String) properties.get(key);
    }

    public int getIntProperty(String key) {
        return getIntProperty(key, 0);
    }

    public int getIntProperty(String key, int defaultValue) {
        String s = getProperty(key);
        if (s != null) {
            return NumberUtils.stringToInt(s, defaultValue);
        }
        return defaultValue;
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    public void setProperty(String key, int value) {
        setProperty(key, String.valueOf(value));
    }

    public void removeProperty(String key) {
        properties.remove(key);
    }

    public void clear() {
        properties.clear();
    }

    public void parseString(String s) {
        properties.clear();

        StringTokenizer st = new StringTokenizer(s, JmlConstants.LINE_SEPARATOR);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.trim().length() == 0)
                continue;
            int index = token.indexOf(':');
            if (index > 0) {
                String key = token.substring(0, index);
                String value = StringUtils.ltrim(token.substring(index + 1));
                setProperty(key, value);
            } else {
                setProperty(token, null);
            }
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (Iterator iter = properties.entrySet().iterator(); iter.hasNext();) {
            Entry item = (Entry) iter.next();
            buffer.append(item.getKey()).append(": ").append(item.getValue())
                    .append(JmlConstants.LINE_SEPARATOR);
        }
        return buffer.toString();
    }

    public Object clone() {
        StringHolder holder = new StringHolder();
        holder.properties.putAll(properties);
        return holder;
    }
}