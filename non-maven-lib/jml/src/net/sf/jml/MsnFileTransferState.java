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

/**
 * Msn file transfer state.
 * 
 * @author Roger Chen
 */
public class MsnFileTransferState {

    public static final MsnFileTransferState INIT = new MsnFileTransferState(
            "Init");

    public static final MsnFileTransferState ACCEPTED = new MsnFileTransferState(
            "Accepted");

    public static final MsnFileTransferState COMPLETED = new MsnFileTransferState(
            "Completed");

    public static final MsnFileTransferState CANCELED = new MsnFileTransferState(
            "Canceled");

    public static final MsnFileTransferState FAILED = new MsnFileTransferState(
            "Failed");

    private String s;

    private MsnFileTransferState(String s) {
        this.s = s;
    }

    public String toString() {
        return s;
    }
}
