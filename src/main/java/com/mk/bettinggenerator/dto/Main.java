package com.mk.bettinggenerator.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        List<String> originalList = new ArrayList<>();
        originalList.add("apple");
        originalList.add("banana");
        originalList.add("cherry");

        Random random = new Random();
        int size = originalList.size();

        for (int i = 0; i < size; i++) {
            List<String> listToBeModified = new ArrayList<>(originalList);
            int indexToBeRemoved = random.nextInt(listToBeModified.size());
            String removedElement = listToBeModified.remove(indexToBeRemoved);
            System.out.println("Removed element: " + removedElement);
            System.out.println("Original list after removal: " + originalList);
            System.out.println("Modified list after removal: " + listToBeModified);
        }
    }
}