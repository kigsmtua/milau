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
package io.github.kigsmtua.milau.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

/**
 * A collection of utilities for Redis connections.
 * 
 * @author John Kiragu
 */
public class RedisUtils {
    public static final String PONG = "PONG";
    private static final Logger LOG = LoggerFactory.getLogger(RedisUtils.class);
    private static final String LIST = "list";
    private static final String ZSET = "zset";
    private static final String HASH = "hash";
    private static final String NONE = "none";
    
     /**
     * Ensure that the given connection is established.
     * 
     * @param jedis
     *            a connection to Redis
     * @return true if the supplied connection was already connected
     */
    public static boolean ensureConnection(final Jedis jedis) {
        final boolean jedisOK = testConnection(jedis);
        if (!jedisOK) {
            try {
                jedis.quit();
            } catch (Exception e) {
            } // Ignore
            try {
                jedis.disconnect();
            } catch (Exception e) {
            } // Ignore
            jedis.connect();
        }
        return jedisOK;
    }
    
      /**
     * Test if a connection is valid.
     * 
     * @param jedis
     *            a connection to Redis
     * @return true if the supplied connection is connected
     */
    public static boolean testConnection(final Jedis jedis) {
        boolean jedisOK = false;
        try {
            jedisOK = (jedis.isConnected() && PONG.equals(jedis.ping()));
        } catch (Exception e) {
            jedisOK = false;
        }
        return jedisOK;
    }
    
    private RedisUtils() {
        // Utility class
    }
}
