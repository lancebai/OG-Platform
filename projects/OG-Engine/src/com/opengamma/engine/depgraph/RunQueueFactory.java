/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.engine.depgraph;

/**
 * The source of run queues used by the graph building algorithm to track pending actions.
 */
public abstract class RunQueueFactory {

  /* package */RunQueueFactory() {
  }

  /* package */abstract RunQueue createRunQueue();

  /**
   * Creates FIFO queues based on linked lists. The list can perform well when a single thread is used for graph building. Multi-threaded graph building may perform better with
   * {@link #getConcurrentLinkedQueue}.
   * 
   * @return the factory instance
   */
  public static RunQueueFactory getFifoLinkedList() {
    return new RunQueueFactory() {
      @Override
      protected RunQueue createRunQueue() {
        return new LinkedListRunQueue.FIFO();
      }
    };
  }

  /**
   * Creates LIFO queues based on linked lists. The list can perform well when a single thread is used for graph building and LIFO ordering gives fewer misses on the computation target resolver cache
   * for large portfolios. Multi-threaded graph building may perform better with {@link #getConcurrentStack}.
   * 
   * @return the factory instance
   */
  public static RunQueueFactory getLifoLinkedList() {
    return new RunQueueFactory() {
      @Override
      protected RunQueue createRunQueue() {
        return new LinkedListRunQueue.LIFO();
      }
    };
  }

  /**
   * Creates FIFO queues based on a lock-free linked queue implementation.
   * 
   * @return the factory instance
   */
  public static RunQueueFactory getConcurrentLinkedQueue() {
    return new RunQueueFactory() {
      @Override
      protected RunQueue createRunQueue() {
        return new ConcurrentLinkedQueueRunQueue();
      }
    };
  }

  /**
   * Creates queues that order tasks internally to try and give a low memory footprint by prioritizing "depth first" graph building steps and grouping identical targets together to give fewer cache
   * misses on the computation target resolver for large portfolios. Depending on the nature of the functions and portfolio, the overhead of re-ordering the run-queue may make this less preferable to
   * {@link #getConcurrentStack}.
   * 
   * @return the factory instance
   */
  public static RunQueueFactory getOrdered() {
    return new RunQueueFactory() {
      @Override
      protected RunQueue createRunQueue() {
        return new OrderedRunQueue();
      }
    };
  }

  /**
   * Creates LIFO queues based on a lock-free stack implementation. This can perform well when LIFO ordering gives fewer misses on the computation target resolver cache for large portfolios.
   * 
   * @return the factory instance
   */
  public static RunQueueFactory getConcurrentStack() {
    return new RunQueueFactory() {
      @Override
      protected RunQueue createRunQueue() {
        return new StackRunQueue();
      }
    };
  }

}
