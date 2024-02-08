import Caches.*;
import Evictions.*;
import Interfaces.IEvictionStrategy;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        IEvictionStrategy<String> evictionStrategy = new LeastFrequentlyUsed<>();
        var cache = new MemoryCache<>(3, evictionStrategy);

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Enter command (put, get, stop):");
                String command = scanner.nextLine();

                if ("stop".equals(command)) {
                    break;
                } else if ("put".equals(command)) {
                    System.out.println("Enter key:");
                    String key = scanner.nextLine();
                    System.out.println("Enter value:");
                    String value = scanner.nextLine();

                    cache.putData(key, value);
                    System.out.println("Put data: " + key + ", " + value);
                } else if ("get".equals(command)) {
                    System.out.println("Enter key:");
                    String key = scanner.nextLine();

                    String value = (String) cache.getData(key);
                    System.out.println("Got data: " + value);
                }

                System.out.println("Current cache:");
                for (String key : cache.getKeys()) {
                    System.out.println(key + ": " + cache.getDataWithoutUpdate(key) + " " + cache.dataUsage.get(key).getUsageCount() + " " + cache.dataUsage.get(key).getLastUsed().getTime());
                }
            }
        }
    }
}