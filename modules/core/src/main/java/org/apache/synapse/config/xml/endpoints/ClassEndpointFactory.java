/*
*  Copyright (c) 2005-2011, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.apache.synapse.config.xml.endpoints;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.commons.util.PropertyHelper;
import org.apache.synapse.endpoints.Endpoint;

import javax.xml.namespace.QName;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Properties;

/**
 * This EndpointFactory will be used as an extension point for defining Endpoints externally.
 * <p/>
 * The endpoint configuration will look similar to the one shown below;
 * <p/>
 * <endpoint name="testEndpoint">
 * <class name="org.apache.synapse.ext.endpoints.TestEndpoint">
 * <parameter/>*
 * </class>
 * </endpoint>
 */
public class ClassEndpointFactory extends EndpointFactory {

    private static ClassEndpointFactory instance = new ClassEndpointFactory();
    public static final QName CLASS_QNAME = new QName(SynapseConstants.SYNAPSE_NAMESPACE,
                                                      "class");
    public static final QName NAME_QNAME = new QName("name");
    public static final QName PARAMETER_QNAME = new QName("parameter");

    private ClassEndpointFactory() {
    }

    public static ClassEndpointFactory getInstance() {
        return instance;
    }

    protected Endpoint createEndpoint(OMElement epConfig,
                                      boolean anonymousEndpoint,
                                      Properties properties) {
        OMElement classElement =
                epConfig.getFirstChildWithName(CLASS_QNAME);
        if (classElement == null) {
            return null;
        }

        String nameAttr = classElement.getAttributeValue(NAME_QNAME);
        if (nameAttr == null) {
            return null;
        }

        Endpoint endpoint = null;
        try {
            Class clazz = Class.forName(nameAttr);
            endpoint = (Endpoint) clazz.newInstance();
            for (Iterator iter = classElement.getChildrenWithName(PARAMETER_QNAME);
                 iter.hasNext(); ) {
                OMElement paramEle = (OMElement) iter.next();
                setParameter(endpoint, paramEle);
            }
        } catch (Exception e) {
            handleException("Cannot create class endpoint", e);
        }

        return endpoint;
    }

    private void setParameter(Endpoint endpoint, OMElement paramEle) throws IllegalAccessException,
                                                                            InvocationTargetException,
                                                                            NoSuchMethodException {
        String name = paramEle.getAttributeValue(new QName("name"));
        String value = paramEle.getText();
        PropertyHelper.setInstanceProperty(name, value, endpoint);
    }
}
