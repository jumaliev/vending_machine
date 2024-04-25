import enums.ActionLetter;
import model.*;
import org.w3c.dom.ls.LSOutput;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Random;
import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final CoinAcceptor coinAcceptor;

    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        coinAcceptor = new CoinAcceptor(100);
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        print("В автомате доступны:");
        showProducts(products);
        choiseMethodPay();

        print("Монет на сумму: " + coinAcceptor.getAmount());

        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        chooseAction(allowProducts);

    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        showActions(products);
        print(" h - Выйти");
        String action = fromConsole().substring(0, 1);
        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    coinAcceptor.setAmount(coinAcceptor.getAmount() - products.get(i).getPrice());
                    print("Вы купили " + products.get(i).getName());
                    break;
                } else if ("h".equalsIgnoreCase(action)) {
                    isExit = true;
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            print("Недопустимая буква. Попрбуйте еще раз.");
            chooseAction(products);
        }


    }

    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }
    public void choiseMethodPay() {
        Random rnd = new Random();
        System.out.print("Выберите способ оплаты:\n'c' или 'C' карта\n'm' или 'M' монета\n");
        String userChoisePay = fromConsole();
        while (true) {
            if (userChoisePay.equalsIgnoreCase("c")) {
                System.out.print("Введите номер карты без пробелов: ");
                String userCard = fromConsole();
                if (userCard.length() != 16) {
                    print("Недопустимый номер карты, попробуйте еще раз");
                } else {
                    System.out.print("Введите пароль от карты: ");
                    String userCardPassword = fromConsole();
                    if (userCardPassword.length() < 8) {
                        print("Пароль от карты должен быть не менее 8 символов!");
                    } else {
                        coinAcceptor.setAmount(rnd.nextInt(2 + (8)) * 10);
                        break;
                    }
                }
            } else if (userChoisePay.equalsIgnoreCase("m")) {
                System.out.print("Сколько монет хотите закинуть?  ");
                while (true) {
                    try {
                        int userCoinInt = Integer.parseInt(fromConsole());
                        coinAcceptor.setAmount(userCoinInt);
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Неверное значение для монет, попробуйте еще раз!");
                    }
                }
                break;

            }
        }

    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
