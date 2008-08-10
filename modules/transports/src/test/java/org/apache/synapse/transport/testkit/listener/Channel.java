/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.synapse.transport.testkit.listener;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.TransportInDescription;
import org.apache.axis2.description.TransportOutDescription;

public interface Channel<T extends ListenerTestSetup> {
    T getSetup();
    
    void buildName(NameBuilder nameBuilder);
    
    TransportOutDescription createTransportOutDescription() throws Exception;
    
    /**
     * Create a TransportInDescription for the transport under test.
     * 
     * @return the transport description
     * @throws Exception
     */
    TransportInDescription createTransportInDescription() throws Exception;
    
    /**
     * Set up the service so that it can receive messages through the transport under test.
     * Implementations will typically call {@link AxisService#addParameter(Parameter)} to
     * setup the service parameters required by the transport.
     * The default implementation does nothing.
     * 
     * @param service
     * @throws Exception
     */
    void setupService(AxisService service) throws Exception;
    
    void setupRequestMessageContext(MessageContext msgContext);
    
    EndpointReference createEndpointReference(String address);
    
    void setUp() throws Exception;
    void tearDown() throws Exception;
}
