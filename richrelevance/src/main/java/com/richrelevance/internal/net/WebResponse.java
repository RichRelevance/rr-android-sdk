package com.richrelevance.internal.net;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for a web response, allowing access to the different returned values and fields.
 */
public interface WebResponse {
    /**
     * Returns true if the response contains a header with he given name.
     *
     * @param name The name of the header to look for.
     * @return True if the header exists
     */
    public boolean containsHeader(String name);

    /**
     * Gets the value for the header with the given name, or null if no
     * header exists with that name.
     *
     * @param name The name of the header to look up.
     * @return The value of the header or null if none exists.
     */
    public String getHeaderValue(String name);

    /**
     * @return The response code for this {@link WebResponse}
     */
    public int getResponseCode();

    /**
     * @return The response message for this {@link WebResponse}.
     */
    public String getResponseMessage();

    /**
     * Gets the content encoding for this {@link WebResponse}.
     *
     * @return The content encoding, or null if it wasn't defined.
     */
    public String getContentEncoding();

    /**
     * Gets the content length for this {@link WebResponse}.
     *
     * @return The content length, or -1 if it was not defined.
     */
    public long getContentLength();

    /**
     * Gets the content type for this {@link WebResponse}
     *
     * @return The content type, or null if it was not defined.
     */
    public String getContentType();

    /**
     * Gets the {@link InputStream} to the content of this
     * {@link WebResponse} or null if one does not exist.
     *
     * @return The {@link InputStream} to the content or null
     * if it does not exist.
     * @throws IOException If the stream couldn't be accessed.
     */
    public InputStream getContentStream() throws IOException;

    /**
     * Gets the content of this {@link WebResponse} by parsing it
     * into a string.
     *
     * @return The content, or null if there was none.
     */
    public String getContentAsString();

    /**
     * Gets the content of this {@link WebResponse} by parsing the
     * stream as an {@link JSONArray}.
     *
     * @return The {@link JSONArray} parsed from the stream, or null if it
     * couldn't be parsed.
     */
    public JSONArray getContentAsJSONArray();

    /**
     * Gets the content of this {@link WebResponse} by parsing the
     * stream as an {@link JSONObject}.
     *
     * @return The {@link JSONObject} parsed from the stream, or null if it
     * couldn't be parsed.
     */
    public JSONObject getContentAsJSON();
}
