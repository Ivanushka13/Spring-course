package ru.davydoff;

import org.springframework.stereotype.Component;
import ru.davydoff.operations.CommandOperationType;
import ru.davydoff.operations.OperationCommandProcessor;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class OperationsConsoleListener {

    private final Scanner scanner;
    private final Map<CommandOperationType, OperationCommandProcessor> processorMap;

    public OperationsConsoleListener(
            Scanner scanner,
            List<OperationCommandProcessor> processorList
    ) {
        this.processorMap = processorList.stream()
                .collect(
                        Collectors.toMap(
                                OperationCommandProcessor::getOperationType,
                                processor -> processor
                        )
                );
        this.scanner = scanner;
    }

    public void printAllAvailableOperations() {
        processorMap.keySet()
                .forEach(System.out::println);
    }

    public void startListen() {
        System.out.println("Console listener starting...");
    }

    public void endListen() {
        System.out.println("Console listener stopping...");
    }

    public void listenUpdates() {
        while (true) {
            var operation = listenNextOperation();
            processNextOperation(operation);
        }
    }

    public CommandOperationType listenNextOperation() {
        System.out.println("\nPlease type next operation: ");
        printAllAvailableOperations();
        System.out.println();
        while (true) {
            String operation = scanner.nextLine();
            try {
                return CommandOperationType.valueOf(operation);
            } catch (IllegalArgumentException e) {
                System.out.println("No such command found");
            }
        }
    }

    public void processNextOperation(CommandOperationType operation) {
        try {
            var processor = processorMap.get(operation);
            processor.processOperation();
        } catch (Exception e) {
            System.out.printf(
                    "Error executing command %s: error=%s%n",
                    operation, e.getMessage()
            );
        }
    }
}
