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

import junit.framework.TestSuite;

import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.synapse.transport.testkit.message.ByteArrayMessage;
import org.apache.synapse.transport.testkit.message.MessageData;
import org.apache.synapse.transport.testkit.message.RESTMessage;
import org.apache.synapse.transport.testkit.message.StringMessage;
import org.apache.synapse.transport.testkit.message.XMLMessage;
import org.apache.synapse.transport.testkit.message.XMLMessageType;
import org.apache.synapse.transport.testkit.server.AsyncEndpointFactory;
import org.apache.synapse.transport.testkit.server.EndpointFactory;
import org.apache.synapse.transport.testkit.tests.async.BinaryTestCase;
import org.apache.synapse.transport.testkit.tests.async.RESTTestCase;
import org.apache.synapse.transport.testkit.tests.async.SwATestCase;
import org.apache.synapse.transport.testkit.tests.async.TextPlainTestCase;

public class ListenerTestSuite extends TestSuite {
    public static final String testString = "\u00e0 peine arriv\u00e9s nous entr\u00e2mes dans sa chambre";
    
    public static final MessageTestData ASCII_TEST_DATA = new MessageTestData("ASCII", "test string", "us-ascii");
    public static final MessageTestData UTF8_TEST_DATA = new MessageTestData("UTF8", testString, "UTF-8");
    public static final MessageTestData LATIN1_TEST_DATA = new MessageTestData("Latin1", testString, "ISO-8859-1");
    
    private static final MessageTestData[] messageTestData = new MessageTestData[] {
        ASCII_TEST_DATA,
        UTF8_TEST_DATA,
        LATIN1_TEST_DATA,
    };
    
    private final boolean reuseServer;
    
    public ListenerTestSuite(boolean reuseServer) {
        this.reuseServer = reuseServer;
    }
    
    public ListenerTestSuite() {
        this(true);
    }

    public <C extends AsyncChannel<?>> void addSOAP11Test(C channel, AsyncMessageSender<? super C,XMLMessage> sender, AsyncEndpointFactory<? super C,MessageData> endpointFactory, ContentTypeMode contentTypeMode, MessageTestData data) {
        addTest(new XMLAsyncMessageTestCase<C>(channel, sender, endpointFactory, XMLMessageType.SOAP11, contentTypeMode, SOAP11Constants.SOAP_11_CONTENT_TYPE, data));
    }
    
    public <C extends AsyncChannel<?>> void addSOAP12Test(C channel, AsyncMessageSender<? super C,XMLMessage> sender, AsyncEndpointFactory<? super C,MessageData> endpointFactory, ContentTypeMode contentTypeMode, MessageTestData data) {
        addTest(new XMLAsyncMessageTestCase<C>(channel, sender, endpointFactory, XMLMessageType.SOAP12, contentTypeMode, SOAP12Constants.SOAP_12_CONTENT_TYPE, data));
    }
    
    public <C extends AsyncChannel<?>> void addSOAPTests(C channel, AsyncMessageSender<? super C,XMLMessage> sender, AsyncEndpointFactory<? super C,MessageData> endpointFactory, ContentTypeMode contentTypeMode) {
        for (MessageTestData data : messageTestData) {
            addSOAP11Test(channel, sender, endpointFactory, contentTypeMode, data);
            addSOAP12Test(channel, sender, endpointFactory, contentTypeMode, data);
        }
    }
    
    public <C extends AsyncChannel<?>> void addPOXTest(C channel, AsyncMessageSender<? super C,XMLMessage> sender, AsyncEndpointFactory<? super C,MessageData> endpointFactory, ContentTypeMode contentTypeMode, MessageTestData data) {
        addTest(new XMLAsyncMessageTestCase<C>(channel, sender, endpointFactory, XMLMessageType.POX, contentTypeMode, "application/xml", data));
    }
    
    public <C extends AsyncChannel<?>> void addPOXTests(C channel, AsyncMessageSender<? super C,XMLMessage> sender, AsyncEndpointFactory<? super C,MessageData> endpointFactory, ContentTypeMode contentTypeMode) {
        for (MessageTestData data : messageTestData) {
            addPOXTest(channel, sender, endpointFactory, contentTypeMode, data);
        }
    }
    
    public <C extends RequestResponseChannel<?>> void addPOXTest(C channel, XMLRequestResponseMessageSender<? super C> sender, EndpointFactory<? super C> endpointFactory, ContentTypeMode contentTypeMode, MessageTestData data) {
        addTest(new XMLRequestResponseMessageTestCase<C>(channel, sender, endpointFactory, contentTypeMode, "application/xml", XMLMessageType.POX, data));
    }
    
    public <C extends RequestResponseChannel<?>> void addPOXTests(C channel, XMLRequestResponseMessageSender<? super C> sender, EndpointFactory<? super C> endpointFactory, ContentTypeMode contentTypeMode) {
        for (MessageTestData data : messageTestData) {
            addPOXTest(channel, sender, endpointFactory, contentTypeMode, data);
        }
    }
    
    // TODO: this test actually only makes sense if the transport supports a Content-Type header
    public <C extends AsyncChannel<?>> void addSwATests(C channel, AsyncMessageSender<? super C,ByteArrayMessage> sender, AsyncEndpointFactory<? super C,MessageData> endpointFactory) {
        addTest(new SwATestCase<C>(channel, sender, endpointFactory));
    }
    
    public <C extends AsyncChannel<?>> void addTextPlainTest(C channel, AsyncMessageSender<? super C,StringMessage> sender, AsyncEndpointFactory<? super C,StringMessage> endpointFactory, ContentTypeMode contentTypeMode, MessageTestData data) {
        addTest(new TextPlainTestCase<C>(channel, sender, endpointFactory, contentTypeMode, data));
    }
    
    public <C extends AsyncChannel<?>> void addTextPlainTests(C channel, AsyncMessageSender<? super C,StringMessage> sender, AsyncEndpointFactory<? super C,StringMessage> endpointFactory, ContentTypeMode contentTypeMode) {
        for (MessageTestData data : messageTestData) {
            addTextPlainTest(channel, sender, endpointFactory, contentTypeMode, data);
        }
    }
    
    public <C extends AsyncChannel<?>> void addBinaryTest(C channel, AsyncMessageSender<? super C,ByteArrayMessage> sender, AsyncEndpointFactory<? super C,ByteArrayMessage> endpointFactory, ContentTypeMode contentTypeMode) {
        addTest(new BinaryTestCase<C>(channel, sender, endpointFactory, contentTypeMode));
    }

    public <C extends AsyncChannel<?>> void addRESTTests(C channel, AsyncMessageSender<? super C,RESTMessage> sender, AsyncEndpointFactory<? super C,MessageData> endpointFactory) {
        addTest(new RESTTestCase<C>(channel, sender, endpointFactory));
    }

/*
    @Override
    public void run(TestResult result) {
        if (!reuseServer) {
            super.run(result);
        } else {
            LinkedList<Test> tests = new LinkedList<Test>();
            for (Enumeration<?> e = tests(); e.hasMoreElements(); ) {
                tests.add((Test)e.nextElement());
            }
            while (!tests.isEmpty()) {
                if (result.shouldStop()) {
                    return;
                }
                Test test = tests.removeFirst();
                if (test instanceof AsyncMessageTestCase) {
                    AsyncMessageTestCase<?,?> listenerTest = (AsyncMessageTestCase<?,?>)test;
                    Channel<?> channel = listenerTest.getChannel();
                    ListenerTestServer server;
                    try {
                        server = new ListenerTestServer();
                        server.start(channel);
                    } catch (Throwable t) {
                        result.addError(this, t);
                        return;
                    }
                    listenerTest.setServer(server);
                    runTest(test, result);
                    for (Iterator<Test> it = tests.iterator(); it.hasNext(); ) {
                        if (result.shouldStop()) {
                            return;
                        }
                        test = it.next();
                        if (test instanceof AsyncMessageTestCase) {
                            listenerTest = (AsyncMessageTestCase<?,?>)test;
                            if (listenerTest.getChannel() == channel) {
                                it.remove();
                                listenerTest.setServer(server);
                                runTest(test, result);
                            }
                        }
                    }
                    try {
                        server.stop();
                    } catch (Throwable t) {
                        result.addError(this, t);
                        return;
                    }
                } else {
                    runTest(test, result);
                }
            }
        }
    }
*/
}
