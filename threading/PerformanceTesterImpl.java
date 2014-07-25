import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

public class PerformanceTesterImpl implements PerformanceTester {

    @Override
    public PerformanceTestResult runPerformanceTest(Runnable task, int executionCount, int threadPoolSize) throws InterruptedException {
        return reduce(map(task, executionCount, threadPoolSize));
    }

    private Long[] map(final Runnable task, final int executionCount, int threadPoolSize) throws InterruptedException {
        final Map<Thread, Long[]> calculationResults = new HashMap<>();

        //Initialize working threads
        for (int i = 0; i < threadPoolSize; i++) {
            Thread thread = new Thread() {

                @Override
                public void run() {
                    Long[] calcs = calculationResults.get(this);
                    for (int j = 0; j < calcs.length; j++) {
                        long startTime = System.currentTimeMillis();
                        task.run();
                        calcs[j] = System.currentTimeMillis() - startTime;
                    }
                }
            };
            calculationResults.put(thread, new Long[executionCount]);
        }

        //Start jobs
        for (Thread thread : calculationResults.keySet()) {
            thread.start();
        }

        //Prepare results
        Long[] results = new Long[0];
        for (Map.Entry<Thread, Long[]> entry : calculationResults.entrySet()) {
            entry.getKey().join();
            results = ArrayUtils.addAll(results, entry.getValue());
        }
        return results;
    }

    private PerformanceTestResult reduce(Long[] values) {
        long totalTime = 0;
        long minTime = 0;
        long maxTime = 0;

        for (Long result : values) {
            totalTime += result;
            minTime = minTime==0 ? result : Math.min(result, minTime);
            maxTime = Math.max(result, maxTime);
        }

        return new PerformanceTestResult(totalTime, minTime, maxTime);
    }
}
