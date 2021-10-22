package com.filmster.application.api;

import com.filmster.application.model.IMedia;
import com.google.gson.JsonObject;

/**
 * Interface for MediaFactory for creating media objects from json
 * @author Torbjörn
 */
public interface IMediaFactory {

    /**
     * Creates IMedia objects from JsonObjects
     * @param object - A JsonObject
     * @return - An IMedia Object
     */
    IMedia createMediaObjectFromJson(JsonObject object);
}
