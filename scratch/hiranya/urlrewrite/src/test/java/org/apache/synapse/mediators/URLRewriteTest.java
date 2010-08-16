/*
 *  Copyright (c) 2005-2008, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.apache.synapse.mediators;

import junit.framework.TestCase;
import org.apache.synapse.MessageContext;
import org.apache.synapse.util.xpath.SynapseXPath;
import org.apache.synapse.commons.evaluators.EqualEvaluator;
import org.apache.synapse.commons.evaluators.source.URLTextRetriever;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.core.axis2.Axis2SynapseEnvironment;
import org.apache.synapse.core.SynapseEnvironment;

import java.net.URI;
import java.net.URISyntaxException;

public class URLRewriteTest extends TestCase {

    public void testURIBuilder() {
        try {
            URI uri = new URI("http", null, "localhost", 8080, "/foo", "test=132&foo=bar", "id");
            System.out.println(uri);
        } catch (URISyntaxException e) {
            fail(e.getMessage());
        }
    }

    public void testURIParser() {
        String uriTxt = "";
        try {
            URI uri = new URI(uriTxt);
            printURLFragmetns(uri);
        } catch (URISyntaxException e) {
            fail(e.getMessage());
        }
    }

    public void testMediate() throws Exception {
        org.apache.axis2.context.MessageContext mc =
                new org.apache.axis2.context.MessageContext();
        SynapseConfiguration config = new SynapseConfiguration();
        SynapseEnvironment env = new Axis2SynapseEnvironment(config);
        MessageContext synMc = new Axis2MessageContext(mc, config, env);
        synMc.setProperty("prop1", "ref1");

        URLRewriteMediator mediator = new URLRewriteMediator();

        RewriteRule r1 = new RewriteRule();
        r1.setValue("http://localhost:8080/");
        mediator.addRule(r1);

        RewriteRule r2 = new RewriteRule();
        r2.setValue("/services/TestService");
        r2.setFragmentIndex(URLRewriteMediator.PATH);
        mediator.addRule(r2);

        EqualEvaluator eval = new EqualEvaluator();
        URLTextRetriever txtRtvr = new URLTextRetriever();
        txtRtvr.setFragment("port");
        eval.setTextRetriever(txtRtvr);
        eval.setValue("8080");
        RewriteRule r3 = new RewriteRule();
        r3.setCondition(eval);
        r3.setXpath(new SynapseXPath("get-property('prop1')"));
        r3.setFragmentIndex(URLRewriteMediator.REF);
        mediator.addRule(r3);

        mediator.mediate(synMc);
        System.out.println(synMc.getTo());
    }

    private void printURLFragmetns(URI uri) {
        System.out.println("Protocol: " + uri.getScheme());
        System.out.println("Host: " + uri.getHost());
        System.out.println("Port: " + uri.getPort());
        System.out.println("User: " + uri.getUserInfo());
        System.out.println("Path: " + uri.getPath());
        System.out.println("Query: " + uri.getQuery());
        System.out.println("Ref: " + uri.getFragment());
    }
}