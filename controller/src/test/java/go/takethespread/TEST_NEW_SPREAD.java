package go.takethespread;

import go.takethespread.fsa.SpreadCalculator;

import java.util.concurrent.LinkedBlockingDeque;

public class TEST_NEW_SPREAD {
//    public static void main(String[] args) {
//        SpreadCalculator sc = new SpreadCalculator();
//        LinkedBlockingDeque<Money> data = new LinkedBlockingDeque<>();
//
//        printMessage("before data added");
//        System.out.println("cur: " + sc.getCurSpread().getAmount());
//        System.out.println("enter: " + sc.getEnteringSpread().getAmount());
//
//        for (int i = 0; i < 20; i++) {
//            data.add(Money.dollars(Math.random() * 100));
//            if (i == 0) {
//                System.out.println(data.getFirst().getAmount());
//            }
//        }
//
//        sc.setMarketData(data);
//        sc.testCalcs();
//
//        printMessage("startData: ");
//        for (Money m : sc.getTestData()) {
//            System.out.print(m.getAmount() + " | ");
//        }
//        System.out.println();
//        printMessage("");
//
//
//        printMessage("after data added");
//        System.out.println("cur: " + sc.getCurSpread().getAmount());
//        System.out.println("enter: " + sc.getEnteringSpread().getAmount());
//
//        printMessage("cycle");
//
//        int i = 0;
//        try {
//            while (true) {
//                Thread.sleep(500);
//                sc.addAndRemoveMD(Money.dollars(Math.random() * 100));
//                sc.testCalcs();
//                for (Money m : sc.getTestData()) {
//                    System.out.print(m.getAmount() + " | ");
//                }
//                System.out.println();
//                System.out.println("cur: " + sc.getCurSpread().getAmount());
//                System.out.println("enter: " + sc.getEnteringSpread().getAmount());
//                printMessage("");
//                if(i++ == 10){
//                    printMessage("PAUSE");
//                    printMessage("PAUSE");
//                    printMessage("PAUSE");
//                    printMessage("PAUSE");
//                    printMessage("PAUSE");
//                    sc.pause();
//                }
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    public static void printMessage(String msg) {
//        System.out.println("---------------- " + msg + " ----------------");
//    }
}
