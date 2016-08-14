package go.takethespread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TEST_Call_Exter_Exe {
    public static void main(String[] args) {
        try {
            Process process = Runtime.getRuntime().exec("D:\\xanothersoft\\NinjaTrader 7\\bin64\\NinjaTrader.exe");

            int i = 0;
            while (i < 20) {
                Thread.sleep(1000);
                i++;
                System.out.println(i + " sec left, status: " + process.isAlive());
                // активировать  ниндзю, потом хоткеями добавить стратегию и запустить (хз как), потом програмно проверять работоспособность и ожидать соединения.
            }

            process.destroy();

            System.out.println(process.isAlive());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        try {
//            String line;
//            Process p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
//            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            while ((line = input.readLine()) != null) {
//                System.out.println(line); //<-- Parse data here.
//            }
//            input.close();
//        } catch (Exception err) {
//            err.printStackTrace();
//        }
    }
}
