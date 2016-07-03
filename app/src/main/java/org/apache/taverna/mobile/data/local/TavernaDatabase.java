package org.apache.taverna.mobile.data.local;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = TavernaDatabase.NAME,
        version = TavernaDatabase.VERSION,
        foreignKeysSupported = true)
public class TavernaDatabase {


    public static final String NAME = "Taverna";


    public static final int VERSION = 1;
}