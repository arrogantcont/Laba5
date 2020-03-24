package Laba5.lab5;

import Laba5.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Date;

public class CommandHandler {
    private TicketOffice ticketOffice;
    private TicketType ticketType;
    private BufferedReader reader;
    private int nextTicketId = 1;
    private long nextEventId = 1;
    private ArrayDeque<String> history = new ArrayDeque<>();
    private boolean inputFromFile = false;
    private BufferedReader script;
    private Date dateOFCreation;
    private boolean updateNeeded;

    public CommandHandler(String path, BufferedReader reader) throws IOException {
        ticketOffice = new TicketOffice(path);
        this.reader = reader;

    }

    void handleCommand(String command) throws IOException {
        String[] commandParts = command.split(" ");
        addCommandToHistory(commandParts[0]);
        switch (commandParts[0]) {
            case "help":
                help();
                break;
            case "info":
                dateOFCreation = ticketOffice.findCreationDate();
                System.out.println("Тип коллекции: ArrayDequeue");
                System.out.println("Количество элементов коллекции: " + ticketOffice.getSize());
                if (dateOFCreation == null || ticketOffice.getSize() == 0) {
                    System.out.print("Коллекция ещё не создана");
                } else System.out.println("Дата создания колекции: " + dateOFCreation);

                break;
            case "exit":
                System.out.println("Выхожу из программы");
                System.exit(0);
                break;
            case "print_field_descending_refundable":
                ticketOffice.printAllDescendingRefundable();
                break;
            case "show":
                ticketOffice.printAllTickets();
                break;
            case "add":
                ticketOffice.addNewTicket(startAddingElement(nextTicketId));
                break;
            case "history":
                printHistory();
                break;
            case "filter_greater_than_price":
                printTicketsGreaterPrice(commandParts[1]);
                break;
            case "remove_by_id":
                remove_by_id(commandParts[1]);
                break;
            case "update":
                if ((commandParts.length == 2)) {updateId(commandParts[1]);}
                else System.out.println("Введите команду в формате: update *id билета, который хотите обновить*");
                break;
            case "clear":
                ticketOffice.clear();
                System.out.println("коллекция очищена");
                break;
            case "save":
                ticketOffice.save();
                System.out.println("Коллекця сохранена");
                break;
            case "add_if_max":
                addIfMax();
                break;
            case "add_if_min":
                addIfMin();
                break;
            case "print_field_ascending_price":
                ticketOffice.printAllAscendingPrice();
                break;
            case "execute_script":
                if (inputFromFile) {
                    System.out.println("Нельзя вызывать execute_script при выполнении другого скрипта");
                    break;
                }
                StringBuilder path = new StringBuilder();

                for (int i = 1; i < commandParts.length; i++)
                    path.append(commandParts[i]);
                try {
                    script = new BufferedReader(new FileReader(new File(path.toString())));
                    inputFromFile = true;
                } catch (Exception e) {
                    System.out.println("Доступа к файлу нет, либо вы не указали файл со скриптом"+"\n"+"Введите команду ещё раз");
                }
                break;
            default:
                System.out.println("Вы ввели неподдерживаюмаю команду");
        }

    }

    private void printTicketsGreaterPrice(String commandPart) {
        try {
            int i = Integer.parseInt(commandPart);
            ticketOffice.printAllTicketsGTPrice(i);
        } catch (NumberFormatException e) {
            System.out.println("Должно быть int");
        }


    }

    private void printHistory() {
        for (String s : history) {
            System.out.println(s);
        }
    }

    private void addCommandToHistory(String command) {
        history.addFirst(command);
        if (history.size() > 10) history.removeLast();
    }

    Ticket startAddingElement(int id) throws IOException {
        Pair<Integer, Long> maxs = ticketOffice.getMaxs();
        nextTicketId = maxs.getFirst() + 1;
        nextEventId = maxs.getSecond() + 1;
        System.out.println("Приступаем к заполнению данных билета"+ "\n" + "Вводите данные без пробелов в начале или на конце "+ "\n"  +"имя билета");
        String name = "";
        while (name.trim().equals("")) {
            System.out.println("имя билета не должно быть пустым");
            name = readLine();
        }
        System.out.println("координаты");
        System.out.println("x");
        int x = 0;
        boolean xUnsuccessful = true;
        while (xUnsuccessful) {
            try {
                x = Integer.parseInt(readLine());
                if (x<=851) xUnsuccessful = false;
                else System.out.println("Максимальное значение поля: 851");
            } catch (NumberFormatException e) {
                System.out.println("должно быть int");
            }
        }
        System.out.println("y");
        int y = 0;
        String userY;
        boolean yUnsuccessful = true;
        while (yUnsuccessful) {
            try {
                userY = readLine();
                if (userY.equals("")) {
                    y = 0;
                    System.out.println("y = 0");
                    yUnsuccessful = false;
                }
                else {
                    y = Integer.parseInt(userY);
                    if (y<= 621)  yUnsuccessful = false;
                    else System.out.println("Максимальное значение поля: 621");
                }
            } catch (NumberFormatException e) {
                System.out.println("должно быть int");
            }
        }
        System.out.println("price");
        int price = 0;
        boolean priceUnsuccessful = true;
        while (priceUnsuccessful) {
            try {
                price = Integer.parseInt(readLine());
                if (price > 0) priceUnsuccessful = false;
                if (price == 0) System.out.println("Бесплатный сыр только в мышеловке, цена должна быть больше 0");
                else System.out.println("Цена не может быть меньше 0");
            } catch (NumberFormatException e) {
                System.out.println("должно быть int");
            }
        }
        System.out.println("коммент");
        String comment = "";
        while (comment.trim().equals("")) {
            System.out.println("комментарий к билету не должен быть пустым");
            comment = readLine();
        }
        System.out.println("возврат");
        Boolean refoundable = Boolean.parseBoolean(readLine());
        System.out.println("тип билета");
        TicketType[] allTicketTypes = TicketType.values();
        for (TicketType ticketType : allTicketTypes) {
            System.out.println(ticketType);
        }
        String tickettype;
        boolean typeUnuccessful = true;
        while (typeUnuccessful) {
            try {
                tickettype = readLine();
                if (tickettype.equals("")) ticketType = null;
                else ticketType = TicketType.valueOf(tickettype);
                typeUnuccessful = false;
            } catch (IllegalArgumentException e) {
                System.out.println("Либо выберите предложенный тип, либо оставьте поле пустым");
            }
        }
        System.out.println("Хотите ли Вы добавить событие? " + "\n" + "Чтобы добавить событие, введите *да*" + "\n" + "Если не хотите добавлять событие, оставьте поле пустым или введите любой символ");
        String userEvent = readLine();
        Event event = null;

        if (userEvent.equalsIgnoreCase("Да")) {

            System.out.println("имя события");
            String eventName = "";
            while (eventName.trim().equals("")) {
                System.out.println("имя события не должно быть пустым");
                eventName = readLine();
            }
            System.out.println("число билетов");
            int ticketCount = 0;
            boolean countUnsuccessful = true;
            while (countUnsuccessful) {
                try {
                    ticketCount = Integer.parseInt(readLine());
                    if (ticketCount > 0) countUnsuccessful = false;
                    if (ticketCount == 0) System.out.println("Число билетов не может быть равно 0");
                    else System.out.println("Число билетов не может быть меньше 0");
                } catch (NumberFormatException e) {
                    System.out.println("должно быть int");
                }
            }
            System.out.println("выберите eventType");
            EventType[] allEventsTypes = EventType.values();
            for (EventType eventType : allEventsTypes) {
                System.out.println(eventType);
            }
            EventType eventType = null;
            boolean successful = false;
            while (!successful) {
                try {
                    String userEventType = readLine();
                    eventType = EventType.valueOf(userEventType);
                    successful = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Выберите один из преддложенных eventType");
                }

            }
            event = new Event(nextEventId, eventName, ticketCount, eventType);
        }

        Coordinates coordinates = new Coordinates(x, y);
        if (updateNeeded) {
            Ticket ticket = Ticket.builder()
                    .comment(comment)
                    .coordinates(coordinates)
                    .creationDate(new Date())
                    .event(event)
                    .ticketId(id)
                    .name(name)
                    .price(price)
                    .refundable(refoundable)
                    .type(ticketType)
                    .build();
            return ticket;
        } else {
            Ticket ticket = Ticket.builder()
                    .comment(comment)
                    .coordinates(coordinates)
                    .creationDate(new Date())
                    .event(event)
                    .ticketId(nextTicketId)
                    .name(name)
                    .price(price)
                    .refundable(refoundable)
                    .type(ticketType)
                    .build();
            return ticket;
        }
    }

    private void help() {
        System.out.println("Справка по командам");
        System.out.println("help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add {element} : добавить новый элемент в коллекцию\n" +
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_by_id id : удалить элемент из коллекции по его id\n" +
                "clear : очистить коллекцию\n" +
                "save : сохранить коллекцию в файл\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "exit : завершить программу (без сохранения в файл)\n" +
                "add_if_max {element} : добавить новый элемент в коллекцию, если значение ЕГО ЦЕНЫ превышает значение наибольшего элемента этой коллекции\n" +
                "add_if_min {element} : добавить новый элемент в коллекцию, если значение ЕГО ЦЕНЫ меньше, чем у наименьшего элемента этой коллекции\n" +
                "history : вывести последние 10 команд (без их аргументов)\n" +
                "filter_greater_than_price price : вывести элементы, значение поля price которых больше заданного\n" +
                "print_field_ascending_price : вывести значения поля price всех элементов в порядке возрастания\n" +
                "print_field_descending_refundable : вывести значения поля refundable всех элементов в порядке убывания");
    }

    private void remove_by_id(String commandPart) {
        try {
            int id = Integer.parseInt(commandPart);
            ticketOffice.remove_by_id(id);
        } catch (NumberFormatException e) {
            System.out.println("Должно быть Int");
        }
    }

    private void updateId(String commandPart) {
        try {
            int idUpdate = Integer.parseInt(commandPart);
            if (ticketOffice.checkId(idUpdate)) {
                System.out.println("Нашли такой билет");
                updateNeeded = true;
                ticketOffice.remove_by_id(idUpdate);
                ticketOffice.addNewTicket(startAddingElement(idUpdate));
            } else System.out.println("введите команду снова, такого id нет");
        } catch (NumberFormatException | IOException e) {
            System.out.println("Поле Id должно быть int и больше 0" + "\n" + "Введите команду в формате: update *id билета, который хотите обновить*");
        }
    }

    private void addIfMax() throws IOException {
        Ticket ticket = startAddingElement(nextTicketId);
        if (ticket.getPrice() > ticketOffice.getMaxPrice())
            ticketOffice.addNewTicket(ticket);
        else System.out.println("Билет не добавлен, условие не выполнено");
    }

    private void addIfMin() throws IOException {
        Ticket ticket = startAddingElement(nextTicketId);
        if (ticket.getPrice() < ticketOffice.getMinPrice())
            ticketOffice.addNewTicket(ticket);
        else System.out.println("Билет не добавлен, условие не выполнено");
    }

    private String readLine() throws IOException {
        if (inputFromFile) {
            String s = script.readLine();
            if (s == null) {
                inputFromFile = false;
                return reader.readLine();
            } else {
                System.out.println(s);
                return s;
            }
        } else return reader.readLine();
    }

    public void startHandling() throws IOException {
        String s = readLine();
        handleCommand(s);
    }
}
