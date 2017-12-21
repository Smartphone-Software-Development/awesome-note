package com.bfd.note.network;

import com.bfd.note.util.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteSender {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    private String request(String url) throws IOException {

        RequestBody body = RequestBody.create(JSON, gson.toJson(map));
        Request request = new Request.Builder().url(url).post(body).build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    private Gson gson = new Gson();
    private Map map = new HashMap<String, Object>();

    // :2333/mine/uploadNote for upload
    //  request: (content, note_id, title, user_id, password)
    //  response: (ack)
    // if $note_id exists, this request should be ignored
    @SuppressWarnings("unchecked")
    public boolean uploadNote(String user_id, String pwd, Note note) {
        map.clear();
        map.put("user_id", user_id);
        map.put("password", pwd);
        map.put("note", note);

        try {
            request("http://localhost:2333/mine/uploadNote");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // :2333/mine/updateNote for update
    //  request: (content, note_id, title, user_id, password)
    //  response: (ack)
    // if $note_id not exists, this request should be ignored
    @SuppressWarnings("unchecked")
    public boolean updateNote(String user_id, String pwd, Note note) {
        map.clear();
        map.put("user_id", user_id);
        map.put("password", pwd);
        map.put("note", note);

        try {
            request("http://localhost:2333/mine/updateNote");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // :2333/mine/getAllNotes for the note list
    //  request: (user_id, password, limitation)
    //  response: the set of newest $limitation notes (note_id, title, content)
    // return: the list of notes
    @SuppressWarnings("unchecked")
    public List<Note> getAllNotes(String user_id, String pwd, Integer limitation)
            throws IOException {
        map.clear();
        map.put("user_id", user_id);
        map.put("password", pwd);
        map.put("limitation", limitation);
        String json = request("http://localhost:2333/mine/getAllNotes");
        Type type = new TypeToken<ArrayList<Note>>() {
        }.getType();
        return gson.fromJson(json, type);

    }

    // :2333/mine/getNote for a certain note
    //  request: (user_id, password, note_id)
    //  response: (content)
    // return the note or null if request fails
    @SuppressWarnings("unchecked")
    public Note getNote(String user_id, String pwd, String note_id)
            throws IOException {
        map.clear();
        map.put("user_id", user_id);
        map.put("password", pwd);
        map.put("note_id", note_id);
        String json = request("http://localhost:2333/mine/getNote");
        return gson.fromJson(json, Note.class);
    }

    // :2333/mine/deleteNote for deleting a note
    //  request: (user_id, password, note_id)
    //  response: (content)
    @SuppressWarnings("unchecked")
    public boolean deleteNote(String user_id, String pwd, Long note_id)
            throws IOException {
        map.clear();
        map.put("user_id", user_id);
        map.put("password", pwd);
        map.put("note_id", note_id);
        request("http://localhost:2333/mine/deleteNote");
        return true;
    }

    public static void main(String[] args) {
        NoteSender sender = new NoteSender();

        try {
            Note note = new Note();
            note.setContent("<html> this is a note \"《家》 \' </html>");
            note.setTitle("no title");
            note.setId(233L);
            boolean res = sender.uploadNote("123", "123", note);
            System.out.println(res);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}




