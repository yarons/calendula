/*
 *    Calendula - An assistant for personal medication management.
 *    Copyright (C) 2016 CITIUS - USC
 *
 *    Calendula is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.usc.citius.servando.calendula.database;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.misc.TransactionManager;

import java.util.concurrent.Callable;

import es.usc.citius.servando.calendula.drugdb.model.database.HomogeneousGroupDAO;
import es.usc.citius.servando.calendula.drugdb.model.database.PrescriptionDAO;


public class DB {


    public static final String TAG = DB.class.getSimpleName();

    // Database name
    public static String DB_NAME = "calendula.db";
    // initialized flag
    public static boolean initialized = false;

    // DatabaseManeger reference
    private static DatabaseManager<DatabaseHelper> manager;
    // SQLite DB Helper
    private static DatabaseHelper db;

    // Medicines DAO
    private static MedicineDao Medicines;
    // Routines DAO
    private static RoutineDao Routines;
    // Schedules DAO
    private static ScheduleDao Schedules;
    // ScheduleItems DAO
    private static ScheduleItemDao ScheduleItems;
    // DailyScheduleItem DAO
    private static DailyScheduleItemDao DailyScheduleItems;
    // Prescriptions DAO
    private static PrescriptionDAO Prescriptions;
    // HomogeneousGroups DAO
    private static HomogeneousGroupDAO Groups;
    // Pickups DAO
    private static PickupInfoDao Pickups;
    // Patients DAO
    private static PatientDao Patients;
    //Allergens DAO
    private static PatientAllergenDao PatientAllergens;

    /**
     * Initialize database and DAOs
     */
    public synchronized static void init(Context context) {

        if (!initialized) {
            initialized = true;
            manager = new DatabaseManager<>();
            db = manager.getHelper(context, DatabaseHelper.class);

            db.getReadableDatabase().enableWriteAheadLogging();

            Medicines = new MedicineDao(db);
            Routines = new RoutineDao(db);
            Schedules = new ScheduleDao(db);
            ScheduleItems = new ScheduleItemDao(db);
            DailyScheduleItems = new DailyScheduleItemDao(db);
            Prescriptions = new PrescriptionDAO(db);
            Groups = new HomogeneousGroupDAO(db);
            Pickups = new PickupInfoDao(db);
            Patients = new PatientDao(db);
            PatientAllergens = new PatientAllergenDao(db);
            Log.v(TAG, "DB initialized " + DB.DB_NAME);
        }

    }

    /**
     * Dispose DB and DAOs
     */
    public synchronized static void dispose() {
        initialized = false;
        db.close();
        manager.releaseHelper(db);
        Log.v(TAG, "DB disposed");
    }

    public static DatabaseHelper helper() {
        return db;
    }

    public static void transaction(Callable<?> callable) {
        try {
            TransactionManager.callInTransaction(db.getConnectionSource(), callable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static MedicineDao medicines() {
        return Medicines;
    }

    public static RoutineDao routines() {
        return Routines;
    }

    public static ScheduleDao schedules() {
        return Schedules;
    }

    public static ScheduleItemDao scheduleItems() {
        return ScheduleItems;
    }

    public static DailyScheduleItemDao dailyScheduleItems() {
        return DailyScheduleItems;
    }

    public static PrescriptionDAO prescriptions() {
        return Prescriptions;
    }

    public static HomogeneousGroupDAO groups() {
        return Groups;
    }

    public static PickupInfoDao pickups() {
        return Pickups;
    }

    public static PatientDao patients() {
        return Patients;
    }

    public static PatientAllergenDao allergens(){ return PatientAllergens; }

    public static void dropAndCreateDatabase() {
        db.dropAndCreateAllTables();
    }
}
