package com.bfd.note.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.bfd.note.store.Container;
import com.bfd.note.store.ContainerImpl;
import com.bfd.note.util.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;

public class MySynchronizer {
    private static final String TAG = "MySynchronizer";
    private final static String SP_FILE_NAME = "dirtyNoteRecord";
    private final static String SP_KEY_NAME_UPDATE = "dirty_notes";
    private final static String SP_KEY_NAME_DELETE = "deleted_notes";

    NoteSender sender = new NoteSender();
    Container container = ContainerImpl.getContainer();
    Context context;

    public MySynchronizer(Context context) {
        this.context = context;
    }

    public void syncDataToCloud(final String user_id, final String password, final Handler handler) {
        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "sync thread awake(upload)");
                SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);

                Gson gson = new Gson();
                Type type = new TypeToken<HashSet<Long>>() {}.getType();

                // whether the internet operations are success
                boolean success = true;

                // update/create
                String json = sp.getString(SP_KEY_NAME_UPDATE, "[]");
                Log.i(TAG, "sync... update/create json = " + json);
                HashSet<Long> idset = gson.fromJson(json, type);
                List<Note> notes = DataSupport.findAll(Note.class);
                for (Note note : notes) {
                    if (idset.contains(note.getId())) {
                        if (!sender.uploadNote(user_id, password, note)) {
                            success = false;
                        }
                        Log.i(TAG, "upload " + note.getId() + " success? " + success);
                    }
                }

                SharedPreferences.Editor editor = sp.edit();
                // if success, the dirty marks should be erased
                if (success) {
                    editor.putString(SP_KEY_NAME_UPDATE, "[]");
                    editor.apply();
                } else {
                    // -1 means failed
                    handler.sendEmptyMessage(-1);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                // delete
                json = sp.getString(SP_KEY_NAME_DELETE, "[]");
                Log.i(TAG, "sync... delete json = " + json);
                idset = gson.fromJson(json, type);
                success = true;
                for (Long id : idset) {
                    try {
                        sender.deleteNote(user_id, password, id);
                    } catch (IOException e) {
                        success = false;
                    }
                    Log.i(TAG, "delete " + id + " success? " + success);
                }

                if (success) {
                    editor.putString(SP_KEY_NAME_DELETE, "[]");
                    editor.apply();
                } else {
                    handler.sendEmptyMessage(-1);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                handler.sendEmptyMessage(100);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }.start();
    }

    public void syncDataToLocal(final String user_id, final String password, final Handler handler) {
        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "sync thread awake(download)");
                List<Note> cloudNotes;
                try {
                    cloudNotes = sender.getAllNotes(user_id, password, 999);
                } catch (IOException e) {   // error
                    handler.sendEmptyMessage(-1);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                List<Note> localNotes = DataSupport.findAll(Note.class);
                HashSet<Long> localIdSet = new HashSet<>();
                for (Note note : localNotes) {
                    localIdSet.add(note.getId());
                }

                for (Note note : cloudNotes) {
                    if (!localIdSet.contains(note.getId())) {
                        note.save();
                    }
                }

                // what = 100 means 100% complete
                handler.sendEmptyMessage(100);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }.start();
    }

    public void didDirtyNoteForId(Long note_id) {
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        String json = sp.getString(SP_KEY_NAME_UPDATE, "[]");

        Gson gson = new Gson();
        Type type = new TypeToken<HashSet<Long>>() {}.getType();
        HashSet<Long> idset = gson.fromJson(json, type);

        Log.i(TAG, "make dirty... old json = " + json);

        if (idset.contains(note_id)) return;
        idset.add(note_id);
        json = gson.toJson(idset);

        Log.i(TAG, "make dirty... new json = " + json);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SP_KEY_NAME_UPDATE, json);
        editor.apply();
    }

    public void didDeleteNoteForId(Long note_id) {
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        String json = sp.getString(SP_KEY_NAME_DELETE, "[]");

        Gson gson = new Gson();
        Type type = new TypeToken<HashSet<Long>>() {}.getType();
        HashSet<Long> idset = gson.fromJson(json, type);

        Log.i(TAG, "delete... old json = " + json);

        if (idset.contains(note_id)) return;
        idset.add(note_id);
        json = gson.toJson(idset);

        Log.i(TAG, "delete... new json = " + json);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SP_KEY_NAME_DELETE, json);
        editor.apply();
    }
}



