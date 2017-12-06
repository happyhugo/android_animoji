//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ThreadPoolUtils {
    private ExecutorService exec;
    private ScheduledExecutorService scheduleExec;

    private ThreadPoolUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public ThreadPoolUtils(ThreadPoolUtils.Type type, int corePoolSize) {
        this.scheduleExec = Executors.newScheduledThreadPool(corePoolSize);
        switch(type) {
            case FixedThread:
                this.exec = Executors.newFixedThreadPool(corePoolSize);
                break;
            case SingleThread:
                this.exec = Executors.newSingleThreadExecutor();
                break;
            case CachedThread:
                this.exec = Executors.newCachedThreadPool();
                break;
            default:
                this.exec = this.scheduleExec;
        }

    }

    public void execute(Runnable command) {
        this.exec.execute(command);
    }

    public void execute(List<Runnable> commands) {
        Iterator var2 = commands.iterator();

        while(var2.hasNext()) {
            Runnable command = (Runnable)var2.next();
            this.exec.execute(command);
        }

    }

    public void shutDown() {
        this.exec.shutdown();
    }

    public List<Runnable> shutDownNow() {
        return this.exec.shutdownNow();
    }

    public boolean isShutDown() {
        return this.exec.isShutdown();
    }

    public boolean isTerminated() {
        return this.exec.isTerminated();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return this.exec.awaitTermination(timeout, unit);
    }

    public <T> Future<T> submit(Callable<T> task) {
        return this.exec.submit(task);
    }

    public <T> Future<T> submit(Runnable task, T result) {
        return this.exec.submit(task, result);
    }

    public Future<?> submit(Runnable task) {
        return this.exec.submit(task);
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return this.exec.invokeAll(tasks);
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return this.exec.invokeAll(tasks, timeout, unit);
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return this.exec.invokeAny(tasks);
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.exec.invokeAny(tasks, timeout, unit);
    }

    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return this.scheduleExec.schedule(command, delay, unit);
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return this.scheduleExec.schedule(callable, delay, unit);
    }

    public ScheduledFuture<?> scheduleWithFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return this.scheduleExec.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return this.scheduleExec.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    public static enum Type {
        FixedThread,
        CachedThread,
        SingleThread;

        private Type() {
        }
    }
}
