package com.onegravity.contactpicker.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.onegravity.contactpicker.core.ContactImpl;

import java.util.List;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM ContactImpl")
    List<ContactImpl> getAll();

    @Insert
    void insertAll(List<ContactImpl> contacts);

    @Delete
    void delete(ContactImpl contact);

    @Query("DELETE FROM ContactImpl")
    void deleteAll();


}
