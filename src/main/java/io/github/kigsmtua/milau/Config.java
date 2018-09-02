/* MIT License

* Copyright (c) 2018 John Kiragu

* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:

* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*
*/

package io.github.kigsmtua.milau;

import java.util.Set;

/**
 * An immutable configuration bean for use with the rest of the project.
 * @author john.kiragu
 */
public final class Config {
    
    private final String host;
    private final int port;
    private final int timeout;
    private final String password;
    private final int database;
    private final Set<String> sentinels;
    private final String masterName;
    
    /**
     * Simple builder for the Config class.
     */
    public static class ConfigBuilder {


       //Required parameters
       private final String host; 
       private final int port;
       
       //Optional parameters
       private  int timeout = 5000;
       private  String password = null;
       private  int database = 0;
       private  Set<String> sentinels = null;
       private  String masterName = null;
       
        /**
         * created Config instance will have the given timeout value.
         * @param val the timeout value
         * @return this ConfigBuilder
         */
       public ConfigBuilder withTimeout(int val) {
           timeout = val;
           return this; 
       }
       /**
        * created Config instance will have the given password value.
        * @param val the password value
        * @return this ConfigBuilder
        */
       public ConfigBuilder withPassword(String val) {
           password = val;
           return this; 
       }
       /**
        * created Config instance will have the given database value.
        * @param val the database value
        * @return this ConfigBuilder
        */
       public ConfigBuilder withDatabase(int val) {
           database = val;
           return this; 
       }
       /**
        * created Config instance will have the given sentinels value.
        * @param val the sentinels value
        * @return this ConfigBuilder
        */
       public ConfigBuilder withSentinels(Set<String> val) {
           sentinels = val;
           return this; 
       }
       /**
        * created Config instance will have the given masterName value.
        * @param val the masterName value
        * @return this ConfigBuilder
        */
       public ConfigBuilder withMasterName(String val) {
           masterName = val;
           return this; 
       }
       /**
        * Create ConfigBuilder instance with required host and port.
        * @param host redis host value
        * @param port redis port value
        */
       public ConfigBuilder(String host, int port) {   
           this.host = host;
           this.port = port;
       }
       /**
        * create a new Config initialized with the current values.
        * @return new Config instance
        */
       public Config build() {
           return new Config(this);
       }
    }
    /**
     * create new Config instance based on passed builder instance.
     * @param builder 
     */
    private Config(ConfigBuilder builder) {
        host = builder.host;
        port = builder.port;
        timeout = builder.timeout;
        password = builder.password;
        database = builder.database;
        sentinels = builder.sentinels;
        masterName = builder.masterName;
    }  
    
    /**
     * @return the Redis hostname.
     */
    public String getHost() {
        return this.host;
    }

    /**
     * @return the Redis port number.
     */
    public int getPort() {
        return this.port;
    }

    /**
     * @return the Redis connection timeout.
     */
    public int getTimeout() {
        return this.timeout;
    }

    /**
     * @return the Redis password.
     */
    public String getPassword() {
        return this.password;
    }
    /**
     * @return the Redis database to use.
     */
    public int getDatabase() {
        return this.database;
    }

    /**
     * @return the Redis set of sentinels.
     */
    public Set<String> getSentinels() {
        return this.sentinels;
    }

    /**
     * @return the Redis master name.
     */
    public String getMasterName() {
        return this.masterName;
    }

}
