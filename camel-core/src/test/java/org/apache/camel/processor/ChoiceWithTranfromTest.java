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
package org.apache.camel.processor;

import org.apache.camel.Body;
import org.apache.camel.ContextTestSupport;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.ChoiceWhenBeanExpressionTest.Student;

public class ChoiceWithTranfromTest extends ContextTestSupport {
    
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:outerRoute").id("out")
                    .choice().when(header("test-header").isNotNull())
                        .to("direct:mainProcess")
                    .otherwise()
                        .to("log:badMessage").transform()
                        .method(new MyBean(), "processRejectedMessage")
                        .end();
                from("direct:mainProcess")
                    .bean(new MyBean(), "processMessage");
            }
        };
    }
    
    public static class MyBean {
        public String processRejectedMessage(@Body String message) {
            return "Rejecting " + message;            
        }
        public String processMessage(@Body String message) {
            return "Processing " + message;            
        }
    }
    
    public void testRoute() {
        String result = template.requestBodyAndHeader("direct:outerRoute", "body", "test-header", "headerValue", String.class);
        assertEquals("Processing body", result);
        
        result = template.requestBody("direct:outerRoute", "body", String.class);
        assertEquals("Rejecting body", result);
        
        //context.getRouteDefinition("out").toString();
    }
    

}
