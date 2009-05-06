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
/**
 * To change this template use File | Settings | File Templates.
 */
package org.apache.synapse.commons.util.datasource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.commons.util.SynapseUtilException;
import org.apache.synapse.commons.util.datasource.factory.DataSourceFactory;
import org.apache.synapse.commons.util.jmx.MBeanRepository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Keeps all DataSources in the memory
 */
public class InMemoryDataSourceRepository implements DataSourceRepository {

    private final static Log log = LogFactory.getLog(InMemoryDataSourceRepository.class);

    private final static Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
    private final static MBeanRepository REPOSITORY = DatasourceMBeanRepository.getInstance();

    public InMemoryDataSourceRepository() {
    }

    /**
     * Keep DataSource in the Local store.
     * 
     * @param dataSourceInformation the information describing a data source
     *
     * @see DataSourceRepository#register(DataSourceInformation)
     */
    public void register(DataSourceInformation dataSourceInformation) {

        if (dataSourceInformation == null) {
            handleException("DataSourceInformation cannot be found.");
        }

        DataSource dataSource = DataSourceFactory.createDataSource(dataSourceInformation);

        if (dataSource == null) {

            if (log.isDebugEnabled()) {
                log.debug("DataSource cannot be created or" +
                        " found for DataSource Information " + dataSourceInformation);
            }
            return;
        }

        String name = dataSourceInformation.getDatasourceName();

        if (log.isDebugEnabled()) {
            log.debug("Registering a DatSource with name : " + name + " in Local Pool");
        }

        REPOSITORY.addMBean(name, new DBPoolView(name));
        dataSources.put(name, dataSource);
    }

    public void unRegister(String name) {

        assertNull(name, "Name of the datasource to be removed is empty or null");
        dataSources.remove(name);
        REPOSITORY.removeMBean(name);
    }

    /**
     * Get a DataSource from Local store
     *
     * @see DataSourceRepository#lookUp(String)
     */
    public DataSource lookUp(String name) {

        assertNull(name, "DataSorce name cannot be found.");
        return dataSources.get(name);
    }

    public void init(Properties properties) {
        // nothing
    }

    public boolean isInitialized() {
        return true;
    }

    public void clear() {
        if (!dataSources.isEmpty()) {
            log.info("Clearing all in-memory datasources ");
            dataSources.clear();
        }
        REPOSITORY.clear();

    }

    private static void handleException(String msg) {
        log.error(msg);
        throw new SynapseUtilException(msg);
    }


    private void assertNull(String name, String msg) {
        if (name == null || "".equals(name)) {
            handleException(msg);
        }
    }
}
