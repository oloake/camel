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
package org.apache.camel.util.jsse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.Security;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrustManagersParameters extends JsseParameters {

    private static final Logger LOG = LoggerFactory.getLogger(TrustManagersParameters.class);
    
    /**
     * The key store configuration used to create the {@link KeyStoreParameters} that the
     * {@link TrustManager}s produced by this object's configuration expose.
     */
    protected KeyStoreParameters keyStore;

    /**
     * The optional provider identifier for the {@link TrustManagerFactory} used to create
     * the {@link TrustManager}s represented by this object's configuration.
     */
    protected String provider;

    /**
     * The optional algorithm name for the {@link TrustManagerFactory} used to
     * create the {@link TrustManager}s represented by this object's
     * configuration. See the <a href=
     * "http://download.oracle.com/javase/6/docs/technotes/guides/security/jsse/JSSERefGuide.html"
     * >Java Secure Socket Extension Reference Guide</a> for information about
     * standard algorithm names.
     */
    protected String algorithm;
    
    /**
     * Creates {@link TrustManager}s based on this instance's configuration and the
     * {@code KeyStore} produced by the configuration returned from
     * {@link #getKeyStore()}. The {@code KeyManager}s are produced from a
     * factory created by using the provider and algorithm identifiers returned
     * by {@link #getProvider()} and {@link #getAlgorithm()}, respectively. If
     * either of these methods returns null, the default JSSE value is used
     * instead.
     * 
     * @return the initialized {@code TrustManager}s
     * @throws GeneralSecurityException if there is an error creating the
     *             {@code TrustManagers}s or in creating the {@code KeyStore}
     * @throws IOException if there is an error loading the {@code KeyStore}
     *
     * @see KeyStoreParameters#createKeyStore()
     */
    public TrustManager[] createTrustManagers() throws GeneralSecurityException, IOException {
        
        LOG.trace("Creating TrustManager[] from TrustManagersParameters: {}", this);

        TrustManager[] trustManagers = null;

        if (this.getKeyStore() != null) {
            String tmfAlgorithm = this.parsePropertyValue(this.getAlgorithm());
            if (tmfAlgorithm == null) {
                tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            }
            
            TrustManagerFactory tmf;
            if (this.getProvider() == null) {
                tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            } else {
                tmf = TrustManagerFactory.getInstance(tmfAlgorithm, this.parsePropertyValue(this.getProvider()));
            }
            
            KeyStore ks = this.getKeyStore() == null ? null : this.getKeyStore().createKeyStore();
            tmf.init(ks);
            trustManagers = tmf.getTrustManagers();
        }
        
        return trustManagers;
    }

    public KeyStoreParameters getKeyStore() {
        return keyStore;
    }

    /**
     * Sets the key store configuration used to create the {@link KeyStoreParameters} that the
     * {@link TrustManager}s produced by this object's configuration expose.
     * 
     * @param value the configuration to use
     */
    public void setKeyStore(KeyStoreParameters value) {
        this.keyStore = value;
    }

    public String getProvider() {
        return provider;
    }

    /**
     * Sets the optional provider identifier for the {@link TrustManagerFactory}
     * used to create the {@link TrustManager}s represented by this object's
     * configuration.
     * 
     * @param value the desired provider identifier or {@code null} to use the
     *            highest priority provider implementing the algorithm
     *            
     * @see Security#getProviders()
     */
    public void setProvider(String value) {
        this.provider = value;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Sets optional algorithm name for the {@link TrustManagerFactory} used to create
     * the {@link TrustManager}s represented by this object's configuration.  See the <a href=
     * "http://download.oracle.com/javase/6/docs/technotes/guides/security/jsse/JSSERefGuide.html"
     * >Java Secure Socket Extension Reference Guide</a> for information about
     * standard algorithm names.
     * 
     * @param value the desired algorithm or {@code null} to use default
     * 
     * @see TrustManagerFactory#getDefaultAlgorithm()
     */
    public void setAlgorithm(String value) {
        this.algorithm = value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TrustManagerType [keyStore=");
        builder.append(keyStore);
        builder.append(", provider=");
        builder.append(provider);
        builder.append(", algorithm=");
        builder.append(algorithm);
        builder.append(", getContext()=");
        builder.append(getCamelContext());
        builder.append("]");
        return builder.toString();
    }
}
