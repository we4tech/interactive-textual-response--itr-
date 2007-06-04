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
package net.sf.jml.event;

import net.sf.jml.MsnContact;
import net.sf.jml.MsnSwitchboard;

/**
 * Msn switchboard listener, used for chat. 
 * 
 * @author Roger Chen
 */
public interface MsnSwitchboardListener {

    /**
     * Switchboard started. Maybe start by user or join a
     * switchboard.
     * 
     * @param switchboard
     * 		MsnSwitchboard
     */
    public void switchboardStarted(MsnSwitchboard switchboard);

    /**
     * Switchboard closed.
     * 
     * @param switchboard
     *    	MsnSwitchboard
     */
    public void switchboardClosed(MsnSwitchboard switchboard);

    /**
     * A contact join switchboard.
     * 
     * @param switchboard
     * 		the MsnSwitchboard which contact joins.
     * @param contact
     *		the join contact
     */
    public void contactJoinSwitchboard(MsnSwitchboard switchboard,
            MsnContact contact);

    /**
     * A contact leave switchboard.
     * 
     * @param switchboard
     *   	the MsnSwitchboard which friend leave.
     * @param contact
     *      the leave contact
     */
    public void contactLeaveSwitchboard(MsnSwitchboard switchboard,
            MsnContact contact);

}