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


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.kigsmtua.milau.Config;
import io.github.kigsmtua.milau.task.Task;
import java.io.IOException;
import redis.clients.jedis.Jedis;

/**
 *
 * @author john.kiragu
 */
public class Worker implements Runnable {
    
    private static final Logger LOG = LoggerFactory.getLogger(Worker.class);
    
    protected final Config config;
    
    protected final Jedis jedis;
    
    protected int concurrency;
  
    /**
     * Instantiate a worker no?.
     * @param config
     *          the configuration instance
     * @param concurrency
     *          the number of threads to actually run here
     */
    public Worker(Config config, int concurrency) {
        this.config = config;
        this.jedis = new Jedis(config.getHost(),
                               config.getPort(), config.getTimeout());
        this.concurrency = concurrency;
    }
    
    /**
     * Poll for tasks that are ready for execution.
     */
    public void pollForTasks() {
        while (true) {
            try {
               final String key = "currqueue";
               final String now = Long.toString(System.currentTimeMillis());
               final String payload = jedis.rpop(key);
               if (payload != null) {
                   ObjectMapper mapper = new ObjectMapper();
                   Task task = mapper.reader().readValue(payload);  
                   //There is no correctness to this 
                   //@TODO fix correctness
                   processTask(task, key);
               }
            } catch (IOException e) {
                ///What happens when 
            } finally {

            }
        }
    }
    /**
     * 
     * @param task
     *      The task to actually process
     * @param queue 
     *      The queue that is currently being executed.
     */
    public void processTask(final Task task, String queue) {
        try {
            
            task.perform();
            handleSuccessfulTask(task, queue);
        } catch (Exception ex) {
           //Log exception that happens here 
           handleFailedTask(task, queue);
         
        } finally {
           ///This should look like it actually computes
        }
    }
    
    public void handleSuccessfulTask(final Task task, String queue) {
        ///t
    }
    
    public void handleFailedTask(final Task task, String queue) {
    
    }

    @Override
    public void run() {
        pollForTasks();
    }
    
  }

