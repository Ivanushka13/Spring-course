package ru.davydoff.operations;

public interface OperationCommandProcessor {
    void processOperation();
    CommandOperationType getOperationType();
}
