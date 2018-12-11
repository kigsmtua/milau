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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.kigsmtua.milau.Config;
import io.github.kigsmtua.milau.task.Task;
import redis.clients.jedis.Jedis;

/**
 * @author john.kiragu
 */
public class Worker implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(Worker.class);

    private final Config config;

    private final Jedis jedis;

    private final String queue;

    protected static final long EMPTY_QUEUE_SLEEP_TIME = 500;

    protected static final long RECONNECT_SLEEP_TIME = 5000;

    protected static final int RECONNECT_ATTEMPTS = 120;

    /**
     * Worker polls for tasks from queues.
     *
     * @param config the configuration instance
     * @param queue the queues this worker is listening or subscribed to
     */
    public Worker(Config config, String queue) {
        //@ TODO come up with implementation for multiple queue
        this.config = config;
        this.jedis = new Jedis(config.getHost(),
                config.getPort(), config.getTimeout());
        this.queue = queue;
    }

    /**
     * Starts a worker and registers the worker on the workers queue then begin
     * polling for tasks.
     */
    @Override
    public void run() {
        pollForTasks();
    }

    /**
     * Poll for tasks that are ready for execution.
     */
    private void pollForTasks() {
        while (true) {
            try {
                Set readyTasks = getReadyTasks();
                if (!readyTasks.isEmpty()) {
                    ///1. Get ready tasks 
                    ///2. Test the met
                 
                } else {
                    Thread.sleep(RECONNECT_SLEEP_TIME);
                }
            } catch (InterruptedException ex) {
                
            }
        }
    }
    
    /**
     * Get the messages that are ready to execute. Messages only leave the queue
     * once they are ready for execution so we check for the message between
     * zero and current time
     *
     * @return
     */
    protected Set getReadyTasks() {
        long currentTime = System.currentTimeMillis();
        return jedis.zrangeByScore(this.queue, 0, Double.valueOf(currentTime));
    }
    
    /**
     * Process the tasks that are ready for execution.
     * @param readyTasks 
     *        The tasks that are ready for execution as 
     * @return  A set of job payloads that need to be pr
     */
    protected Map getTaskPayloads(Set readyTasks) {
        Map<String, String> taskPayloads = new HashMap<>();
        //@TODO this queue generation name should be changed.
        String ackQueue = this.queue + "ack-queue";
        String jobQueue = this.queue + "job-queue";
        readyTasks.forEach((item) -> {
            long currentTime = System.currentTimeMillis();
            jedis.zadd(ackQueue, Double.valueOf(currentTime), (String) item);
            jedis.zrem(this.queue, (String) item);
            String taskPayload = jedis.hget(jobQueue, String.valueOf(item));
            taskPayloads.put(String.valueOf(item), taskPayload);
        });
        return taskPayloads;
    }

    /**
     *
     * @param task The task to actually process
     * @param queue The queue that is currently being executed.
     */
    private void processTasks(Map<String, String> tasks) {

        String ackQueue = this.queue + "ack-queue";
        String jobQueue = this.queue + "job-queue";
        
        ObjectMapper mapper = new ObjectMapper();

        tasks.entrySet().forEach((task) -> {
            String taskId = task.getKey();
            String taskPayload = task.getValue();
            try {
                Task s = mapper.reader().readValue(taskPayload);
                //task.perform();
            } catch (IOException ex) {
                //This task cannot be deserialized jist delete it
               jedis.zrem(this.queue, taskId);
               jedis.zrem(ackQueue, taskId);
               jedis.hdel(jobQueue, taskId);
            } catch (Exception ex) {
                LOG.error(ex.getMessage());
            } finally {
                ///We have a task that has finished execution.
                ackTask(taskId);
            }
        });        
    }
    /**
     * Acknowledge that the task has already completed execution.
     * @param taskId 
     */
    private void ackTask(String taskId) {
        String ackQueue = this.queue + "ack-queue";
        String jobQueue = this.queue + "job-queue";
        
        jedis.zrem(ackQueue, taskId);
        jedis.hdel(jobQueue, taskId);
    }
}
