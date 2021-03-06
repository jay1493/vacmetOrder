package com.imagesoftware.anubhav.vacmet.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.imagesoftware.anubhav.vacmet.database.converters.ArrayListConverter;
import com.imagesoftware.anubhav.vacmet.database.converters.ArrayListDoubleConverter;
import com.imagesoftware.anubhav.vacmet.database.daos.DatabaseRequestsDao;
import com.imagesoftware.anubhav.vacmet.database.entities.ItemEntity;
import com.imagesoftware.anubhav.vacmet.database.entities.OrderEntity;
import com.imagesoftware.anubhav.vacmet.model.ItemModel;
import com.imagesoftware.anubhav.vacmet.model.OrderModel;

/**
 * Created by Anubhav-Singh on 09-01-2018.
 */

@Database(entities = {OrderEntity.class, ItemEntity.class}, version = 1, exportSchema = false)
@TypeConverters({ArrayListConverter.class, ArrayListDoubleConverter.class})
public abstract class VacmetDatabase extends RoomDatabase {

    //Define abstract dao's

    public abstract DatabaseRequestsDao getDatabaseRequestDao();

}
