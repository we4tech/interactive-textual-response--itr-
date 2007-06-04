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
package net.sf.jml.impl;

import net.sf.jml.MsnMessageChain;
import net.sf.jml.MsnMessageIterator;
import net.sf.jml.protocol.MsnMessage;

/**
 * @author Roger Chen
 */
public class MsnMessageChainImpl implements MsnMessageChain {

    private static final int DEFAULT_FIXED_SIZE = 20;

    private final MsnMessage[] messages;
    private final int fixedSize;
    private int pos;

    public MsnMessageChainImpl() {
        this(DEFAULT_FIXED_SIZE);
    }

    public MsnMessageChainImpl(int fixedSize) {
        this.fixedSize = fixedSize;
        messages = new MsnMessage[fixedSize];
    }

    public void addMsnMessage(MsnMessage message) {
        if (message != null) {
            synchronized (messages) {
                messages[pos++] = message;
                if (pos >= fixedSize)
                    pos = 0;
            }
        }
    }

    public MsnMessageIterator iterator() {
        return new MsnMessageIteratorImpl();
    }

    private class MsnMessageIteratorImpl implements MsnMessageIterator {

        private MsnMessage[] msgs = new MsnMessage[fixedSize];
        private int position = fixedSize;

        public MsnMessageIteratorImpl() {
            synchronized (messages) {
                System.arraycopy(messages, pos, msgs, 0, fixedSize - pos);
                System.arraycopy(messages, 0, msgs, fixedSize - pos, pos);
            }
        }

        public boolean hasPrevious() {
            return position != 0 && msgs[position - 1] != null;
        }

        public MsnMessage previous() {
            return msgs[--position];
        }

    }
}