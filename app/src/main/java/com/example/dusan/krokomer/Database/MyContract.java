package com.example.dusan.krokomer.Database;

public class MyContract {

    public static class Steps{
        public static final String TABLE_NAME = "steps";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_DATE = "date"; //Kazdych 30 minút nový záznam.
        public static final String COLUMN_STEPS = "steps"; //Počet krokov za posledných 30 minút.
    }

    public static class User{
        public static final String TABLE_NAME = "user"; //Iba jeden záznam.
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_BIRTH = "birth";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_SEX = "sex";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_GOAL = "goal";
        public static final String COLUMN_STEPS = "steps";
        public static final String COLUMN_TRACK= "track"; // 0 alebo 1
        public static final String COLUMN_ACHIEVEMENT = "achievement"; //Posledny datum, kedy bol dosiahnutý cieľový počet krokov
    }

}
