package com.example.football_field_management.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.football_field_management.model.Booking;
import com.example.football_field_management.model.Field;
import com.example.football_field_management.model.Service;
import com.example.football_field_management.model.ServiceUsage;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FootballFieldManager.db";
    private static final int DATABASE_VERSION = 1;

    // Tables
    private static final String TABLE_USERS = "users";
    private static final String TABLE_FIELDS = "fields";
    private static final String TABLE_SERVICES = "services";
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String TABLE_SERVICE_USAGE = "service_usage";

    // Common columns
    private static final String COLUMN_ID = "id";

    // Users table columns
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Fields table columns
    private static final String COLUMN_FIELD_NAME = "name";
    private static final String COLUMN_FIELD_PRICE = "price";

    // Services table columns
    private static final String COLUMN_SERVICE_NAME = "name";
    private static final String COLUMN_SERVICE_PRICE = "price";

    // Bookings table columns
    private static final String COLUMN_FIELD_ID = "field_id";
    private static final String COLUMN_CUSTOMER_NAME = "customer_name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_IS_PAID = "is_paid";

    // Service Usage table columns
    private static final String COLUMN_BOOKING_ID = "booking_id";
    private static final String COLUMN_SERVICE_ID = "service_id";
    private static final String COLUMN_QUANTITY = "quantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Create fields table
        String CREATE_FIELDS_TABLE = "CREATE TABLE " + TABLE_FIELDS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FIELD_NAME + " TEXT,"
                + COLUMN_FIELD_PRICE + " REAL)";
        db.execSQL(CREATE_FIELDS_TABLE);

        // Create services table
        String CREATE_SERVICES_TABLE = "CREATE TABLE " + TABLE_SERVICES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SERVICE_NAME + " TEXT,"
                + COLUMN_SERVICE_PRICE + " REAL)";
        db.execSQL(CREATE_SERVICES_TABLE);

        // Create bookings table
        String CREATE_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FIELD_ID + " INTEGER,"
                + COLUMN_CUSTOMER_NAME + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_IS_PAID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_FIELD_ID + ") REFERENCES " + TABLE_FIELDS + "(" + COLUMN_ID + "))";
        db.execSQL(CREATE_BOOKINGS_TABLE);

        // Create service_usage table
        String CREATE_SERVICE_USAGE_TABLE = "CREATE TABLE " + TABLE_SERVICE_USAGE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_BOOKING_ID + " INTEGER,"
                + COLUMN_SERVICE_ID + " INTEGER,"
                + COLUMN_QUANTITY + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_BOOKING_ID + ") REFERENCES " + TABLE_BOOKINGS + "(" + COLUMN_ID + "),"
                + "FOREIGN KEY(" + COLUMN_SERVICE_ID + ") REFERENCES " + TABLE_SERVICES + "(" + COLUMN_ID + "))";
        db.execSQL(CREATE_SERVICE_USAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE_USAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // User methods
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                COLUMN_USERNAME + "=?", new String[]{username},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        boolean valid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return valid;
    }

    // Field methods
    public boolean addField(Field field) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIELD_NAME, field.getName());
        values.put(COLUMN_FIELD_PRICE, field.getPrice());
        long result = db.insert(TABLE_FIELDS, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateField(Field field) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIELD_NAME, field.getName());
        values.put(COLUMN_FIELD_PRICE, field.getPrice());
        int result = db.update(TABLE_FIELDS, values, COLUMN_ID + "=?",
                new String[]{String.valueOf(field.getId())});
        db.close();
        return result > 0;
    }

    public boolean deleteField(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_FIELDS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public List<Field> getAllFields() {
        List<Field> fields = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FIELDS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Field field = new Field();
                field.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                field.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIELD_NAME)));
                field.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FIELD_PRICE)));
                fields.add(field);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return fields;
    }

    // Service methods
    public boolean addService(Service service) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SERVICE_NAME, service.getName());
        values.put(COLUMN_SERVICE_PRICE, service.getPrice());
        long result = db.insert(TABLE_SERVICES, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateService(Service service) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SERVICE_NAME, service.getName());
        values.put(COLUMN_SERVICE_PRICE, service.getPrice());
        int result = db.update(TABLE_SERVICES, values, COLUMN_ID + "=?",
                new String[]{String.valueOf(service.getId())});
        db.close();
        return result > 0;
    }

    public boolean deleteService(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_SERVICES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SERVICES, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Service service = new Service();
                service.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                service.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_NAME)));
                service.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_PRICE)));
                services.add(service);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return services;
    }

    // Booking methods
    public boolean addBooking(Booking booking, List<ServiceUsage> serviceUsages) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_FIELD_ID, booking.getFieldId());
            values.put(COLUMN_CUSTOMER_NAME, booking.getCustomerName());
            values.put(COLUMN_PHONE, booking.getPhone());
            values.put(COLUMN_DATE, booking.getDate());
            values.put(COLUMN_TIME, booking.getTime());
            values.put(COLUMN_IS_PAID, booking.isPaid() ? 1 : 0);
            long bookingId = db.insert(TABLE_BOOKINGS, null, values);

            for (ServiceUsage usage : serviceUsages) {
                ContentValues usageValues = new ContentValues();
                usageValues.put(COLUMN_BOOKING_ID, bookingId);
                usageValues.put(COLUMN_SERVICE_ID, usage.getServiceId());
                usageValues.put(COLUMN_QUANTITY, usage.getQuantity());
                db.insert(TABLE_SERVICE_USAGE, null, usageValues);
            }

            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean updateBooking(Booking booking, List<ServiceUsage> serviceUsages) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_FIELD_ID, booking.getFieldId());
            values.put(COLUMN_CUSTOMER_NAME, booking.getCustomerName());
            values.put(COLUMN_PHONE, booking.getPhone());
            values.put(COLUMN_DATE, booking.getDate());
            values.put(COLUMN_TIME, booking.getTime());
            values.put(COLUMN_IS_PAID, booking.isPaid() ? 1 : 0);
            db.update(TABLE_BOOKINGS, values, COLUMN_ID + "=?", new String[]{String.valueOf(booking.getId())});

            db.delete(TABLE_SERVICE_USAGE, COLUMN_BOOKING_ID + "=?", new String[]{String.valueOf(booking.getId())});

            for (ServiceUsage usage : serviceUsages) {
                ContentValues usageValues = new ContentValues();
                usageValues.put(COLUMN_BOOKING_ID, booking.getId());
                usageValues.put(COLUMN_SERVICE_ID, usage.getServiceId());
                usageValues.put(COLUMN_QUANTITY, usage.getQuantity());
                db.insert(TABLE_SERVICE_USAGE, null, usageValues);
            }

            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean deleteBooking(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_SERVICE_USAGE, COLUMN_BOOKING_ID + "=?", new String[]{String.valueOf(id)});
            int result = db.delete(TABLE_BOOKINGS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
            return result > 0;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKINGS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking();
                booking.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                booking.setFieldId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FIELD_ID)));
                booking.setCustomerName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_NAME)));
                booking.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
                booking.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                booking.setTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)));
                booking.setPaid(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_PAID)) == 1);
                bookings.add(booking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookings;
    }

    public List<ServiceUsage> getServiceUsagesForBooking(int bookingId) {
        List<ServiceUsage> usages = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SERVICE_USAGE, null, COLUMN_BOOKING_ID + "=?",
                new String[]{String.valueOf(bookingId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ServiceUsage usage = new ServiceUsage();
                usage.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                usage.setBookingId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_ID)));
                usage.setServiceId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_ID)));
                usage.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)));
                usages.add(usage);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return usages;
    }

    public String getFieldNameById(int fieldId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FIELDS, new String[]{COLUMN_FIELD_NAME},
                COLUMN_ID + "=?", new String[]{String.valueOf(fieldId)}, null, null, null);
        String name = "";
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIELD_NAME));
        }
        cursor.close();
        db.close();
        return name;
    }

    public double getFieldPriceById(int fieldId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FIELDS, new String[]{COLUMN_FIELD_PRICE},
                COLUMN_ID + "=?", new String[]{String.valueOf(fieldId)}, null, null, null);
        double price = 0;
        if (cursor.moveToFirst()) {
            price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FIELD_PRICE));
        }
        cursor.close();
        db.close();
        return price;
    }

    public String getServiceNameById(int serviceId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SERVICES, new String[]{COLUMN_SERVICE_NAME},
                COLUMN_ID + "=?", new String[]{String.valueOf(serviceId)}, null, null, null);
        String name = "";
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_NAME));
        }
        cursor.close();
        db.close();
        return name;
    }

    public double getServicePriceById(int serviceId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SERVICES, new String[]{COLUMN_SERVICE_PRICE},
                COLUMN_ID + "=?", new String[]{String.valueOf(serviceId)}, null, null, null);
        double price = 0;
        if (cursor.moveToFirst()) {
            price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_PRICE));
        }
        cursor.close();
        db.close();
        return price;
    }

    public boolean markBookingAsPaid(int bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_PAID, 1);
        int result = db.update(TABLE_BOOKINGS, values, COLUMN_ID + "=?", new String[]{String.valueOf(bookingId)});
        db.close();
        return result > 0;
    }

    public double getTotalPaidAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;
        List<Booking> bookings = getAllBookings();
        for (Booking booking : bookings) {
            if (booking.isPaid()) {
                total += getFieldPriceById(booking.getFieldId());
                List<ServiceUsage> usages = getServiceUsagesForBooking(booking.getId());
                for (ServiceUsage usage : usages) {
                    total += getServicePriceById(usage.getServiceId()) * usage.getQuantity();
                }
            }
        }
        db.close();
        return total;
    }
}