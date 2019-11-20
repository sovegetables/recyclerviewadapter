package com.sovegetables.adapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by albert on 2018/2/15.
 */

class DeepCloneUtil {

    private DeepCloneUtil() {
        //no instance
    }

    @SuppressWarnings("unchecked")
    static  <T extends Serializable> T copy(T origin) {
        ByteArrayInputStream in = null;
        ByteArrayOutputStream pos = null;
        T copyList = null;
        try {
            pos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(pos);
            out.writeObject(origin);
            in = new ByteArrayInputStream(pos.toByteArray());
            ObjectInputStream oin = new ObjectInputStream(in);
            copyList = (T) oin.readObject();
        } catch (Exception ignored) {
        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
            if(pos != null){
                try {
                    pos.close();
                } catch (IOException ignored) {
                }
            }
        }
        return copyList;
    }
}
