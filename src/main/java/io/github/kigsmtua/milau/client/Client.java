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
import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.kigsmtua.milau.Config;
import io.github.kigsmtua.milau.task.Task;
import redis.clients.jedis.Jedis;

/**
 *
 * @author john.kiragu
 */
public class Client {


    private final Config config;
    private final Jedis jedis;
    private static final Logger LOGGER =
            LoggerFactory.getLogger(Client.class);
    /**
     * Create a new Client, which creates it's own connection to Redis using
     * values from the Config.
     *
     * @param config
     *            used to create a connection to Redis
     * @throws IllegalArgumentException
     *             if the Config is null
     */
    public Client(final Config config) {
        this.config = config;
        this.jedis = new Jedis(this.config.getHost(),
                               this.config.getPort(), this.config.getTimeout());
    }
    
     /**
     *
     * Queues a job in a given queue to be run.
     *
     * @param queue
     *            the queue name
     * @param future
     *            time in milliseconds from now to execute the job
     * @param task
     *            the task to be queued
     *
     * the operations required to execute a job should be atomic
     * to avoid a job that has been scheduled and there is no half baked jobs
     */
    public void enqueue(String queue, Task task, long future) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            doEnqueue(queue, mapper.writeValueAsString(task), future);
        } catch (JsonProcessingException ex) {
            LOGGER.error("Error occured while serializing message {}:::",
                    ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error("Execption occured while enqueueing messsage {}:::",
                    ex.getMessage());
        }
    }


    /**
     * Helper method that encapsulates the minimum logic for adding a job to a
     * queue.
     *
     * @param jedis
     *            the connection to Redis
     * @param queue
     *            the queue name
     * @param future
     *            time in milliseconds from now to execute the job
     * @param jobJson
     *            serialized class to be picked from the queue
     *
     * the operations required to execute a job should be atomic
     * to avoid a job that has been scheduled and there is no half baked jobs
     */
    private void doEnqueue(final String queue,
             final String jobJson, final long future) {
        long timeToExecuteJob = System.currentTimeMillis() + future;
        UUID uuid = UUID.randomUUID();
        String taskId = uuid.toString();
        HashMap<String, Double> scores = new HashMap<>();
        scores.put(taskId, Double.valueOf(timeToExecuteJob));
        this.jedis.zadd(queue, scores);
        //@TODO this needs to be cleaned up so as to work with all queues
        String jobQueue = queue + "job-queue";
        jedis.hset(jobQueue, taskId, jobJson);
    }

}
