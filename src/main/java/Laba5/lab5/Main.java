package Laba5.lab5;

import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String command = "";
            String path = args[0];
            CommandHandler commandHandler = new CommandHandler(path, reader);
            System.out.println("Введите команду");
            while (true) {
                commandHandler.startHandling();
            }
        } catch (NoSuchElementException e) {

        } catch (JsonSyntaxException e) {
            System.out.println("Ошибка парсинга файла");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Не введено имя файла");
        } catch (IOException e) {
            System.out.println("Нет доступа к файлу");
        }

    }
}
