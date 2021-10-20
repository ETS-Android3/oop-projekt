package com.filmster.test.api;

import com.filmster.test.model.IMedia;
import com.google.gson.JsonObject;

/**
 * Interface for MediaFactory for creating media objects from json
 * @author Torbjörn
 */
public interface IMediaFactory {

    public IMedia createMediaObjectFromJson(JsonObject object);
}
