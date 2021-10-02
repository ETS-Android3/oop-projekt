package com.example.testmaddafakka.api;

import com.example.testmaddafakka.model.Movie;

import java.util.List;

/**
 * Methods that can be made to get different results from the api
 */
public interface IAdapter {

    public void get250Movies();

    public List<Movie> getList(String listID);

}
