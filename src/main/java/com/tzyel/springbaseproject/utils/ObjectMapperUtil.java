package com.tzyel.springbaseproject.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class providing mapping functionalities using ModelMapper.
 * <p>
 * This class encapsulates the usage of ModelMapper to map objects of different types,
 * allowing convenient mapping between source and destination objects.
 */
public class ObjectMapperUtil {

    private static final ModelMapper modelMapper = new ModelMapper();

    /**
     * Static initialization block to configure the ModelMapper settings.
     * Default property matching strategy is set to STRICT.
     * Custom mappings can be added using ModelMapper's addMappings method.
     */
    static {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    /**
     * Private constructor to prevent direct instantiation.
     */
    private ObjectMapperUtil() {
    }

    /**
     * Maps a source object to a new object of the specified output class type.
     *
     * @param <D>      type of result object.
     * @param <T>      type of source object to map from.
     * @param source   source object that needs to be mapped.
     * @param outClass class of the result object to be created.
     * @return new object of type 'outClass'.
     */
    public static <D, T> D map(final T source, Class<D> outClass) {
        return modelMapper.map(source, outClass);
    }

    /**
     * Maps a collection of source objects to a list of objects of the specified output class type.
     *
     * @param sourceList collection of source objects to be mapped.
     * @param outClass   class of the result list elements.
     * @param <D>        type of objects in the result list.
     * @param <T>        type of objects in the 'sourceList'.
     * @return list of mapped objects of type 'D'.
     */
    public static <D, T> List<D> mapAll(final Collection<T> sourceList, Class<D> outClass) {
        return sourceList.stream()
                .map(source -> map(source, outClass))
                .collect(Collectors.toList());
    }

    /**
     * Maps a collection of source objects to a set of objects of the specified output class type.
     *
     * @param sourceList collection of source objects to be mapped.
     * @param outClass   class of the result set elements.
     * @param <D>        type of objects in the result set.
     * @param <T>        type of objects in the 'sourceList'.
     * @return set of mapped objects of type 'D'.
     */
    public static <D, T> Set<D> mapAllToSet(final Collection<T> sourceList, Class<D> outClass) {
        return sourceList.stream()
                .map(source -> map(source, outClass))
                .collect(Collectors.toSet());
    }

    /**
     * Maps a source object to an existing destination object.
     *
     * @param source      object to map from.
     * @param destination object to map to.
     * @param <S>         type of source object.
     * @param <D>         type of destination object.
     * @return the mapped destination object.
     */
    public static <S, D> D map(final S source, D destination) {
        modelMapper.map(source, destination);
        return destination;
    }
}
