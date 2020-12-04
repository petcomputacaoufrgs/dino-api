package br.ufrgs.inf.pet.dinoapi.utils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListUtils {
    private ListUtils() {}
    /**
     * Used to create a "distinct" by object property with filter
     * Use: .filter(distinctByKey(Class::getProperty())
     * @param keyExtractor: function that get the property
     * @param <T>: object type
     * @return list without repeated values of selected property
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
