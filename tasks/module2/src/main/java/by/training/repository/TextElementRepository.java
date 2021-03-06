package by.training.repository;

import by.training.entity.BaseTextElement;

import java.util.List;
import java.util.Optional;

public interface TextElementRepository<T extends BaseTextElement> {
    long create(T item);

    Optional<T> get(long id);

    void update(T item);

    void delete(long id);

    List<T> find(TextElementSpecification<BaseTextElement> spec);
}
