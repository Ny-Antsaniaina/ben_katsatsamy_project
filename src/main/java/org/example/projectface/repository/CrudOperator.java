package org.example.projectface.repository;

import java.util.List;

public interface CrudOperator<T> {
    T saveAll(T params);
}
