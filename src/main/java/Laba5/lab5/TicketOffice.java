package Laba5.lab5;

import Laba5.IO.TicketReader;
import Laba5.IO.TicketWriter;
import Laba5.model.Ticket;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Date;

import static java.util.Arrays.asList;
import static java.util.Arrays.sort;

public class TicketOffice {
    private ArrayDeque<Ticket> tickets = new ArrayDeque<>();
    private GsonBuilder builder = new GsonBuilder();
    private Gson gson = builder.create();
    private TicketWriter ticketWriter;
    private TicketReader ticketReader;
    private String path;

    @SneakyThrows
    public TicketOffice(String path) {
        this.path = path;
        Ticket[] tickets = readTicketsFromFile();
        if (tickets == null) this.tickets = new ArrayDeque<>();
        else this.tickets = new ArrayDeque<>(asList(tickets));

    }

    public Pair<Integer, Long> getMaxs() {
        int maxTicketId = 0;
        long maxEventId = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId() > maxTicketId)
                maxTicketId = ticket.getTicketId();
            if (ticket.getEvent() != null && ticket.getEvent().getId() > maxEventId)
                maxEventId = ticket.getEvent().getId();
        }

        return new Pair<>(maxTicketId, maxEventId);
    }

    @SneakyThrows
    public void save() {
        saveTicketsToFile(tickets);
    }

    public void saveTicketsToFile(ArrayDeque<Ticket> obj) throws IOException {
        ticketWriter = new TicketWriter(path);
        String s = gson.toJson(obj);
        ticketWriter.writeToFile(s);
    }

    public Ticket[] readTicketsFromFile() throws IOException {
        ticketReader = new TicketReader(path);
        String s = ticketReader.readFromFile();
        return gson.fromJson(s, Ticket[].class);//мы записываем билеты как элты массива, поэтому поэтому обращаемся к ним аналогично
    }


    public void printAllTickets() {
        for (Ticket ticket : tickets) {
            System.out.println(ticket);
        }
    }

    public void addNewTicket(Ticket ticket) {
        tickets.add(ticket);
        System.out.println("Билет добавлен");
    }

    public void printAllTicketsGTPrice(int i) {
        for (Ticket ticket : tickets) {
            if (ticket.getPrice() > i)
                System.out.println(ticket);
        }
    }

    public void remove_by_id(int id) {
        tickets.removeIf(ticket -> ticket.getTicketId() == id);
        System.out.println("удалили успешно");
    }

    public boolean checkId(int id) {
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId() == id) return true;
        }
        return false;
    }

    public void clear() {
        tickets.clear();
    }

    public Class<? extends ArrayDeque> info() {
        return tickets.getClass();
    }

    public int getSize() {
        return tickets.size();
    }

    public void printAllAscendingPrice() {
        int[] Prices = new int[tickets.size()];
        int counter = 0;
        for (Ticket ticket : tickets) {
            Prices[counter] = ticket.getPrice();
            counter++;
        }
        sort(Prices);
        for (int price : Prices) {
            System.out.println(price);
        }
    }

    public void printAllDescendingRefundable() {
        int trueCounter = 0;
        for (Ticket ticket : tickets) {
            if (ticket.isRefundable()) trueCounter++;
        }
        for (int i = 1; i <= trueCounter; i++) System.out.println("true");
        int falseCounter = tickets.size() - trueCounter;
        for (int i = 1; i <= falseCounter; i++) System.out.println("false");
    }

    public int getMaxPrice() {
        int maxPrice = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getPrice() > maxPrice) maxPrice = ticket.getPrice();
        }
        return maxPrice;
    }

    public int getMinPrice() {
        int minPrice = 2147483647;
        for (Ticket ticket : tickets) {
            if (ticket.getPrice() < minPrice) minPrice = ticket.getPrice();
        }
        return minPrice;
    }

    public Date findCreationDate() {
        Date trueDate = null;
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId() == 1) trueDate = ticket.getCreationDate();
        }
        return trueDate;
    }
}
