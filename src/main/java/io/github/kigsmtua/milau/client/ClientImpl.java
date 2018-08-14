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
package io.github.kigsmtua.milau.client;

import io.github.kigsmtua.milau.Config;
import io.github.kigsmtua.milau.utils.RedisUtils;
import redis.clients.jedis.Jedis;

/**
 *
 * @author john.kiragu
 */
public class ClientImpl extends AbstractClient {
    
    private final Config config;
    private final Jedis jedis;
    
    /**
     * Create a new ClientImpl, which creates it's own connection to Redis using
     * values from the config.
     *
     * @param config
     *            used to create a connection to Redis
     * @throws IllegalArgumentException
     *             if the config is null
     */
    public ClientImpl(final Config config){
        super(config);
        this.config = config;
        this.jedis = new Jedis(config.getHost(), config.getPort(), config.getTimeout());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void doEnqueue(String queue, String jobJson, long future) throws Exception {
        /// @TODO clean up this implementation 
        RedisUtils.ensureConnection(this.jedis);
        doEnqueue(this.jedis, queue, future, jobJson);
    }
    
}
