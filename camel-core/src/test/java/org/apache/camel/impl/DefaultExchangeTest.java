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
package org.apache.camel.impl;

import java.io.IOException;
import java.net.ConnectException;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangeTestSupport;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.Message;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.util.ObjectHelper;

/**
 * @version 
 */
public class DefaultExchangeTest extends ExchangeTestSupport {

    public void testBody() throws Exception {
        assertNotNull(exchange.getIn().getBody());

        assertEquals("<hello id='m123'>world!</hello>", exchange.getIn().getBody());
        assertEquals("<hello id='m123'>world!</hello>", exchange.getIn().getBody(String.class));

        assertEquals("<hello id='m123'>world!</hello>", exchange.getIn().getMandatoryBody());
        assertEquals("<hello id='m123'>world!</hello>", exchange.getIn().getMandatoryBody(String.class));
    }

    public void testMandatoryBody() throws Exception {
        assertNotNull(exchange.getIn().getBody());

        assertEquals("<hello id='m123'>world!</hello>", exchange.getIn().getBody());
        assertEquals(null, exchange.getIn().getBody(Integer.class));

        assertEquals("<hello id='m123'>world!</hello>", exchange.getIn().getMandatoryBody());
        try {
            exchange.getIn().getMandatoryBody(Integer.class);
            fail("Should have thrown an InvalidPayloadException");
        } catch (InvalidPayloadException e) {
            // expected
        }
    }

    public void testExceptionAsType() throws Exception {
        exchange.setException(ObjectHelper.wrapRuntimeCamelException(new ConnectException("Cannot connect to remote server")));

        ConnectException ce = exchange.getException(ConnectException.class);
        assertNotNull(ce);
        assertEquals("Cannot connect to remote server", ce.getMessage());

        IOException ie = exchange.getException(IOException.class);
        assertNotNull(ie);
        assertEquals("Cannot connect to remote server", ie.getMessage());

        Exception e = exchange.getException(Exception.class);
        assertNotNull(e);
        assertEquals("Cannot connect to remote server", e.getMessage());

        RuntimeCamelException rce = exchange.getException(RuntimeCamelException.class);
        assertNotNull(rce);
        assertNotSame("Cannot connect to remote server", rce.getMessage());
        assertEquals("Cannot connect to remote server", rce.getCause().getMessage());
    }

    public void testHeader() throws Exception {
        assertNotNull(exchange.getIn().getHeaders());

        assertEquals(123, exchange.getIn().getHeader("bar"));
        assertEquals(new Integer(123), exchange.getIn().getHeader("bar", Integer.class));
        assertEquals("123", exchange.getIn().getHeader("bar", String.class));
        assertEquals(123, exchange.getIn().getHeader("bar", 234));

        assertEquals(123, exchange.getIn().getHeader("bar", 234));
        assertEquals(new Integer(123), exchange.getIn().getHeader("bar", 234, Integer.class));
        assertEquals("123", exchange.getIn().getHeader("bar", "234", String.class));

        assertEquals(234, exchange.getIn().getHeader("cheese", 234));
        assertEquals("234", exchange.getIn().getHeader("cheese", 234, String.class));
    }

    public void testProperty() throws Exception {
        exchange.removeProperty("foobar");
        assertFalse(exchange.hasProperties());

        exchange.setProperty("fruit", "apple");
        assertTrue(exchange.hasProperties());

        assertEquals("apple", exchange.getProperty("fruit"));
        assertEquals(null, exchange.getProperty("beer"));
        assertEquals(null, exchange.getProperty("beer", String.class));
        
        // Current TypeConverter support to turn the null value to false of boolean,
        // as assertEquals needs the Object as the parameter, we have to use Boolean.FALSE value in this case
        assertEquals(Boolean.FALSE, exchange.getProperty("beer", boolean.class));
        assertEquals(null, exchange.getProperty("beer", Boolean.class));

        assertEquals("apple", exchange.getProperty("fruit", String.class));
        assertEquals("apple", exchange.getProperty("fruit", "banana", String.class));
        assertEquals("banana", exchange.getProperty("beer", "banana"));
        assertEquals("banana", exchange.getProperty("beer", "banana", String.class));
    }

    public void testInType() throws Exception {
        exchange.setIn(new MyMessage());

        MyMessage my = exchange.getIn(MyMessage.class);
        assertNotNull(my);
    }

    public void testOutType() throws Exception {
        exchange.setOut(new MyMessage());

        MyMessage my = exchange.getOut(MyMessage.class);
        assertNotNull(my);
    }

    public void testCopy() {
        DefaultExchange sourceExchange = new DefaultExchange(context);
        MyMessage sourceIn = new MyMessage();
        sourceExchange.setIn(sourceIn);
        Exchange destExchange = sourceExchange.copy();
        Message destIn = destExchange.getIn();

        assertEquals("Dest message should be of the same type as source message",
                     sourceIn.getClass(), destIn.getClass());
    }

    public static class MyMessage extends DefaultMessage {
        @Override
        public MyMessage newInstance() {
            return new MyMessage();
        }
    }

}
