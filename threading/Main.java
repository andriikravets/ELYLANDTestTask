public class Main {
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 3)
            throw new IllegalArgumentException("Please provide valid parameters: fibonacci number, task quantity, threads quantity");
        try {

            Integer n = Integer.valueOf(args[0]);
            Integer numberOfTasks = Integer.valueOf(args[1]);
            Integer threadQuantity = Integer.valueOf(args[2]);

            if (n <= 0 || numberOfTasks <= 0 || threadQuantity <= 0)
                throw new IllegalArgumentException("All input parameters should be greater than 0");

            startCalculation(n, numberOfTasks, threadQuantity);

        } catch (NumberFormatException ex) {
            throw new IllegalAccessError("You have to provide integer numbers as a parameters");
        }
    }

    public static void startCalculation(int n, int numberOfTasks, int threadsQuantity) throws InterruptedException {
        PerformanceTester performanceTester = new PerformanceTesterImpl();
        PerformanceTestResult performanceTestResult = performanceTester.runPerformanceTest(createRunnableTask(n),
                                                                                            numberOfTasks,
                                                                                            threadsQuantity);
        System.out.println(String.format("Total execution time: %s", performanceTestResult.getTotalTime()));
        System.out.println(String.format("Minimum execution time: %s", performanceTestResult.getMinTime()));
        System.out.println(String.format("Maximum execution time: %s", performanceTestResult.getMaxTime()));
    }

    private static Runnable createRunnableTask(final int n) {
        return new Runnable() {
            @Override
            public void run() {
                FibCalc fibCalc = new FibCalcImpl();
                fibCalc.fib(n);
            }
        };
    }
}
