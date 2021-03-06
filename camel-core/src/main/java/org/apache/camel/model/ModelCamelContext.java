/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.model;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;

/**
 * Model level interface for a camel context
 *
 */
public interface ModelCamelContext extends CamelContext {

    /**
     * Returns a list of the current route definitions
     *
     * @return list of the current route definitions
     */
    List<RouteDefinition> getRouteDefinitions();

    /**
     * Gets the route definition with the given id
     *
     * @param id id of the route
     * @return the route definition or <tt>null</tt> if not found
     */
    RouteDefinition getRouteDefinition(String id);

    /**
     * Loads a collection of route definitions from the given {@link java.io.InputStream}.
     *
     * @param is input stream with the route(s) definition to add
     * @throws Exception if the route definitions could not be loaded for whatever reason
     * @return the route definitions
     */
    RoutesDefinition loadRoutesDefinition(InputStream is) throws Exception;

    /**
     * Adds a collection of route definitions to the context
     *
     * @param routeDefinitions the route(s) definition to add
     * @throws Exception if the route definitions could not be created for whatever reason
     */
    void addRouteDefinitions(Collection<RouteDefinition> routeDefinitions) throws Exception;

    /**
     * Add a route definition to the context
     *
     * @param routeDefinition the route definition to add
     * @throws Exception if the route definition could not be created for whatever reason
     */
    void addRouteDefinition(RouteDefinition routeDefinition) throws Exception;

    /**
     * Removes a collection of route definitions from the context - stopping any previously running
     * routes if any of them are actively running
     *
     * @param routeDefinitions route(s) definitions to remove
     * @throws Exception if the route definitions could not be removed for whatever reason
     */
    void removeRouteDefinitions(Collection<RouteDefinition> routeDefinitions) throws Exception;

    /**
     * Removes a route definition from the context - stopping any previously running
     * routes if any of them are actively running
     *
     * @param routeDefinition route definition to remove
     * @throws Exception if the route definition could not be removed for whatever reason
     */
    void removeRouteDefinition(RouteDefinition routeDefinition) throws Exception;

    /**
     * Starts the given route if it has been previously stopped
     *
     * @param route the route to start
     * @throws Exception is thrown if the route could not be started for whatever reason
     */
    void startRoute(RouteDefinition route) throws Exception;
    
    /**
     * Stops the given route.
     *
     * @param route the route to stop
     * @throws Exception is thrown if the route could not be stopped for whatever reason
     */
    void stopRoute(RouteDefinition route) throws Exception;

    /**
     * Sets the data formats that can be referenced in the routes.
     *
     * @param dataFormats the data formats
     */
    void setDataFormats(Map<String, DataFormatDefinition> dataFormats);

    /**
     * Gets the data formats that can be referenced in the routes.
     *
     * @return the data formats available
     */
    Map<String, DataFormatDefinition> getDataFormats();

}
