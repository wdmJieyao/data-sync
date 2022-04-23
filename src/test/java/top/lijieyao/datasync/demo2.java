package top.lijieyao.datasync;

/**
 * @Description:
 * @Author: LiJieYao
 * @Date: 2022/4/22 19:20
 */
public class demo2 {

    public static void main(String[] args) throws Exception {

        //maxNumber为一个从传参获取的整型数，

        //要求实现startPrint方法，间隔1s，打印1到maxNumber，不能阻塞主线程

        Thread printThread = new Thread(() -> {
            startPrint(50);
            // count.countDown();
        }, "Thread-1");
        printThread.start();
        //stopTime为一个从传参获取的整型数,单位秒

        //要求实现stopPrint方法，一段时间后结束打印，不能阻塞主线程
        stopPrint(2);
        printThread.interrupt();
    }

    public static void startPrint(int maxNumber) {
        for (int i = 1; i <= maxNumber; i++) {
            System.out.println(i);

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void stopPrint(int stopTime) {
        try {
            Thread.sleep(stopTime * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
