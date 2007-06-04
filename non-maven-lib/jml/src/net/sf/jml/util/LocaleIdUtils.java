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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Get Locale Identifier from Locale, 
 * see <a href="http://krafft.com/scripts/deluxe-calendar/lcid_chart.htm">http://krafft.com/scripts/deluxe-calendar/lcid_chart.htm</a>.
 * 
 * @author Roger Chen
 */
public class LocaleIdUtils {

    private static final short DEFAULT_LOCALE_ID = 1033;

    private static Map localeIdMap = new HashMap();

    static {
        localeIdMap.put(Locale.ENGLISH, new Integer(1033));
        localeIdMap.put(Locale.CHINA, new Integer(2052));
        localeIdMap.put(Locale.JAPAN, new Integer(1041));
        localeIdMap.put(Locale.KOREA, new Integer(1042));
        localeIdMap.put(new Locale("pt","BR",""), new Integer(1046));
    }

    public static short getLocaleId() {
        return getLocaleId(Locale.getDefault());
    }

    public static short getLocaleId(Locale locale) {
        Integer localeId = (Integer) localeIdMap.get(locale);
        if (localeId == null)
            return DEFAULT_LOCALE_ID;
        return localeId.shortValue();
    }
}
