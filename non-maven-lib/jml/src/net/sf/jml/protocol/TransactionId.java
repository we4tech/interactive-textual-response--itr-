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
package net.sf.jml.protocol;

/**
 * Transaction Id, See
 * <a href="http://www.hypothetic.org/docs/msn/general/commands.php">http://www.hypothetic.org/docs/msn/general/commands.php</a>
 * "Transaction IDs" Section.
 * 
 * @author Roger Chen
 */
final class TransactionId {

    private int trId = 0;

    public synchronized int nextTransactionId() {
        trId++;
        if (trId < 0) //Bigger than Integer.MAX_VALUE
            trId = 1;
        return trId;
    }

    public String toString() {
        return String.valueOf(trId);
    }
}
