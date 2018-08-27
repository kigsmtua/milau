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
package io.github.kigsmtua.milau.worker;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.kigsmtua.milau.Config;
import redis.clients.jedis.Jedis;

/**
 *
 * @author john.kiragu
 */
public class Worker implements ExecutorInterface {
    
    private static final Logger LOG = LoggerFactory.getLogger(Worker.class);
    
    protected final Config config;
    protected final Jedis jedis;
  
    /**
     * Instantiate a worker no?.
     * @param config
     *          the configuration instance
     * @param jedis
     *          the redis client instance
     */
    public Worker(Config config, Jedis jedis) {
        this.config = config;
        this.jedis = jedis;
    }
    
    /***
     * Implement the worker.
     */
    
    public void work(){
    }
}

