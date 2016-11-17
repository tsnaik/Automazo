package net.wecodelicious.automazo.util.dbutil;

/*add this to gradle
compile 'com.google.guava:guava:20.0'
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Created by Tanmay on 28-Aug-16.
 */
public class AutomazoDb extends SQLiteOpenHelper
{
    private  static  final String TAG = "DB";
    //database info
    private static final String DATABASE_NAME = "automazoDb";
    private static final int DATABASE_VERSION = 1;

    //tables
    private static final String TABLE_TRIGGERS = "triggers";
    private static final String TABLE_ACTIONS = "actions";
    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_VARIABLES = "variables";
    private static final String TABLE_CONSTRAINTS = "constraints";
    private static final String TABLE_ACTIONS_ARGUMENTS = "actions_arguments";
    private static final String TABLE_CONSTRAINTS_ARGUMENTS = "constraints_arguments";

    //columns of triggers table
    private static final String KEY_TRIGGER_ID = "id";
    private static final String KEY_TRIGGER_NAME = "name";
    private static final String KEY_TRIGGER_METHOD = "method";

    //columns of actions table
    private static final String KEY_ACTIONS_ID = "id";
    private static final String KEY_ACTIONS_NAME = "name";
    private static final String KEY_ACTIONS_METHOD = "method";
    private static final String KEY_ACTIONS_RECIPE_ID_FK = "recipeId";

    //columns of constraints table
    private static final String KEY_CONSTRAINTS_ID = "id";
    private static final String KEY_CONSTRAINTS_NAME = "name";
    private static final String KEY_CONSTRAINTS_METHOD = "method";
    private static final String KEY_CONSTRAINTS_RECIPE_ID_FK = "recipeId";
    //columns of recipes table
    private static final String KEY_RECIPES_ID = "id";
    private static final String KEY_RECIPES_NAME = "name";
    private static final String KEY_RECIPES_TRIGGERS_ID_FK = "triggerId";

    //columns of variables table
    private static final String KEY_VARIABLES_ID = "id";
    private static final String KEY_VARIABLES_NAME = "name";
    private static final String KEY_VARIABLES_VALUE = "value";

    //columns of actions_arguments table
    private static final String KEY_ACTIONS_ARGUMENTS_ID = "id";
    private static final String KEY_ACTIONS_ARGUMENTS_ACTION_ID_FK = "actionId";
    private static final String KEY_ACTIONS_ARGUMENTS_VALUE = "value";

    //columns of constraints_arguments table
    private static final String KEY_CONSTRAINTS_ARGUMENTS_ID = "id";
    private static final String KEY_CONSTRAINTS_ARGUMENTS_CONSTRAINT_ID_FK = "constraintId";
    private static final String KEY_CONSTRAINTS_ARGUMENTS_VALUE = "value";


    private AutomazoDb(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static AutomazoDb sInstance;


    public static synchronized AutomazoDb getInstance(Context context)
    {
        if (sInstance == null)
        {
            sInstance = new AutomazoDb(context.getApplicationContext());
        }
        return sInstance;
    }



    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_ACTIONS_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY NOT NULL,%s TEXT,%s TEXT,%s INTEGER REFERENCES %s(%s))", TABLE_ACTIONS, KEY_ACTIONS_ID, KEY_ACTIONS_NAME, KEY_ACTIONS_METHOD, KEY_ACTIONS_RECIPE_ID_FK, TABLE_RECIPES, KEY_RECIPES_ID);

        String CREATE_TRIGGERS_TABLE = "CREATE TABLE " + TABLE_TRIGGERS
                + "(" + KEY_TRIGGER_ID + "INTEGER PRIMARY KEY NOT NULL,"
                + KEY_TRIGGER_NAME + "TEXT,"
                + KEY_TRIGGER_METHOD + "TEXT"
                + ")";

        String CREATE_CONSTRAINTS_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY NOT NULL,%s TEXT,%s TEXT,%s INTEGER REFERENCES %s(%s))",
                TABLE_CONSTRAINTS,
                KEY_CONSTRAINTS_ID,
                KEY_CONSTRAINTS_NAME,
                KEY_CONSTRAINTS_METHOD,
                KEY_CONSTRAINTS_RECIPE_ID_FK,
                TABLE_RECIPES,
                KEY_RECIPES_ID);

        String CREATE_VARIABLES_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY NOT NULL,%s TEXT,%s TEXT)",
                TABLE_VARIABLES,
                KEY_VARIABLES_ID,
                KEY_VARIABLES_NAME,
                KEY_VARIABLES_VALUE);

        String CREATE_RECIPES_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY NOT NULL,%s TEXT,%s INTEGER REFERENCES %s(%s))",
                TABLE_RECIPES,
                KEY_RECIPES_ID,
                KEY_RECIPES_NAME,
                KEY_RECIPES_TRIGGERS_ID_FK,
                TABLE_TRIGGERS,
                KEY_TRIGGER_ID);

        String CREATE_ACTIONS_ARGUMENTS_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY NOT NULL,%s INTEGER REFERENCES %s(%s),%s TEXT,PRIMARY KEY (%s,%s))",
                TABLE_ACTIONS_ARGUMENTS,
                KEY_ACTIONS_ARGUMENTS_ID,
                KEY_ACTIONS_ARGUMENTS_ACTION_ID_FK,
                TABLE_ACTIONS,
                KEY_ACTIONS_ID,
                KEY_ACTIONS_ARGUMENTS_VALUE,
                KEY_ACTIONS_ARGUMENTS_ID,
                KEY_ACTIONS_ARGUMENTS_ACTION_ID_FK);

        String CREATE_CONSTRAINTS_ARGUMENTS_TABLE  = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY NOT NULL,%s INTEGER REFERENCES %s(%s),%s TEXT,PRIMARY KEY (%s,%s))",
                TABLE_CONSTRAINTS_ARGUMENTS,
                KEY_CONSTRAINTS_ARGUMENTS_ID,
                KEY_CONSTRAINTS_ARGUMENTS_CONSTRAINT_ID_FK,
                TABLE_CONSTRAINTS,
                KEY_CONSTRAINTS_ID,
                KEY_CONSTRAINTS_ARGUMENTS_VALUE,
                KEY_CONSTRAINTS_ARGUMENTS_ID,
                KEY_CONSTRAINTS_ARGUMENTS_CONSTRAINT_ID_FK);


        db.execSQL(CREATE_ACTIONS_TABLE);
        db.execSQL(CREATE_CONSTRAINTS_TABLE);
        db.execSQL(CREATE_RECIPES_TABLE);
        db.execSQL(CREATE_TRIGGERS_TABLE);
        db.execSQL(CREATE_VARIABLES_TABLE);
        db.execSQL(CREATE_ACTIONS_ARGUMENTS_TABLE);
        db.execSQL(CREATE_CONSTRAINTS_ARGUMENTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSTRAINTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIGGERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VARIABLES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSTRAINTS_ARGUMENTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIONS_ARGUMENTS);

            onCreate(db);
        }
    }

    private long addTrigger(Trigger trigger)
    {
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1; //-1 is error
        db.beginTransaction();
        try
        {
            ContentValues values = new ContentValues();
            values.put(KEY_TRIGGER_NAME,trigger.name);
            values.put(KEY_TRIGGER_METHOD,trigger.method);

            userId = db.insertOrThrow(TABLE_TRIGGERS,null,values);
            db.setTransactionSuccessful();

        }
        catch (Exception e)
        {
            Log.d(TAG,"Error in inserting in trigger" + e.getMessage());
        }
        finally
        {
            db.endTransaction();
        }
        return userId;
    }

    private long addVariable(Variable variable)
    {
        SQLiteDatabase db = getWritableDatabase();
        long id = -1; //-1 is error
        db.beginTransaction();
        try
        {
            ContentValues values = new ContentValues();
            values.put(KEY_VARIABLES_NAME,variable.name);
            values.put(KEY_VARIABLES_VALUE,variable.value);

            id = db.insertOrThrow(TABLE_VARIABLES,null,values);
            db.setTransactionSuccessful();

        }
        catch (Exception e)
        {
            Log.d(TAG,"Error in inserting in variables" + e.getMessage());
        }
        finally
        {
            db.endTransaction();
        }
        return id;
    }

    private long addRecipe(Recipe recipe, long triggerId)
    {
        SQLiteDatabase db = getWritableDatabase();
        long rId=-1;
        db.beginTransaction();
        try
        {
            long tId = triggerId;
            // = addTrigger(recipe.trigger);

            ContentValues values = new ContentValues();
            values.put(KEY_RECIPES_TRIGGERS_ID_FK,tId);
            values.put(KEY_RECIPES_NAME,recipe.name);

            rId = db.insertOrThrow(TABLE_RECIPES,null,values);
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            Log.d(TAG,"Error in inserting in recipes" + e.getMessage());

        }
        finally
        {
            db.endTransaction();
        }
        return rId;
    }

    private long addAction(Action action, long recipeId)
    {
        SQLiteDatabase db = getWritableDatabase();
        long aId=-1;
        db.beginTransaction();
        try
        {
            long rId = recipeId;
            // = addRecipe(action.recipe);

            ContentValues values = new ContentValues();
            values.put(KEY_ACTIONS_RECIPE_ID_FK,rId);
            values.put(KEY_ACTIONS_NAME,action.name);
            values.put(KEY_ACTIONS_METHOD,action.method);

            aId = db.insertOrThrow(TABLE_ACTIONS,null,values);
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            Log.d(TAG,"Error in inserting in actions" + e.getMessage());

        }
        finally
        {
            db.endTransaction();
        }
        return aId;
    }

    private long addConstraint(Constraint constraint, long recipeId)
    {
        SQLiteDatabase db = getWritableDatabase();
        long aId=-1;
        db.beginTransaction();
        try
        {
            long rId = recipeId;
                    //= addRecipe(constraint.recipe);

            ContentValues values = new ContentValues();
            values.put(KEY_CONSTRAINTS_RECIPE_ID_FK,rId);
            values.put(KEY_CONSTRAINTS_NAME,constraint.name);
            values.put(KEY_CONSTRAINTS_METHOD,constraint.method);

            aId = db.insertOrThrow(TABLE_CONSTRAINTS,null,values);
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            Log.d(TAG,"Error in inserting in constraints" + e.getMessage());

        }
        finally
        {
            db.endTransaction();
        }
        return aId;
    }

    private long addActionArgument(ActionArgument actionArgument, long actionId)
    {
        SQLiteDatabase db = getWritableDatabase();
        long aId=-1;
        db.beginTransaction();
        try
        {
            long tId = actionId;
                    //= addAction(actionArgument.action);

            ContentValues values = new ContentValues();
            values.put(KEY_ACTIONS_ARGUMENTS_ACTION_ID_FK,tId);
            values.put(KEY_ACTIONS_ARGUMENTS_VALUE,actionArgument.value);

            aId = db.insertOrThrow(TABLE_ACTIONS_ARGUMENTS,null,values);
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            Log.d(TAG,"Error in inserting in actionArguments" + e.getMessage());

        }
        finally
        {
            db.endTransaction();
        }
        return aId;
    }

    private long addConstraintArgument(ConstraintArgument constraintArgument, long constraintId)
    {
        SQLiteDatabase db = getWritableDatabase();
        long cId=-1;
        db.beginTransaction();
        try
        {
            long tId = constraintId;
                    //= addConstraint(constraintArgument.constraint);

            ContentValues values = new ContentValues();
            values.put(KEY_CONSTRAINTS_ARGUMENTS_CONSTRAINT_ID_FK,tId);
            values.put(KEY_CONSTRAINTS_ARGUMENTS_VALUE, constraintArgument.value);

            cId = db.insertOrThrow(TABLE_CONSTRAINTS_ARGUMENTS,null,values);
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            Log.d(TAG,"Error in inserting in constraintArguments" + e.getMessage());

        }
        finally
        {
            db.endTransaction();
        }
        return cId;
    }

    private long getRecipeIdByName(String recipeName)
    {
        SQLiteDatabase db = getReadableDatabase();
        long id = -1;//not found
        String SELECT_RECIPE_ID_QUERY = String.format("SELECT %s from %s where %s = %s"
                ,KEY_RECIPES_ID
                ,TABLE_RECIPES
                ,KEY_RECIPES_NAME
                ,recipeName);

        try(Cursor cursor = db.rawQuery(SELECT_RECIPE_ID_QUERY,null))
        {
            if(cursor.moveToFirst())
            {
                id = cursor.getLong(cursor.getColumnIndex(KEY_RECIPES_ID));
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error while trying to get from database " + e.getMessage());
        }
        return id;
    }

    public List<Long> getTriggersByRecipeId(long id)
    {
        List<Long> list = new ArrayList<Long>();

        SQLiteDatabase db = getReadableDatabase();

        String SELECT_TRIGGERS_QUERY = String.format("SELECT %s from %s where %s = %s",
                KEY_RECIPES_TRIGGERS_ID_FK,
                TABLE_RECIPES,
                KEY_RECIPES_ID,
                Long.toString(id));

        try(Cursor cursor = db.rawQuery(SELECT_TRIGGERS_QUERY,null))
        {
            if(cursor.moveToFirst())
            {
                do
                {
                    list.add(cursor.getLong(cursor.getColumnIndex(KEY_RECIPES_TRIGGERS_ID_FK)));
                }while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error while trying to get from database " + e.getMessage());
        }
        return list;
    }

    public List<Long> getActionsByRecipeId(long id)
    {
        List<Long> list = new ArrayList<Long>();

        SQLiteDatabase db = getReadableDatabase();

        String SELECT_ACTIONS_QUERY = String.format("SELECT %s from %s where %s = %s",
                KEY_ACTIONS_ID,
                TABLE_ACTIONS,
                KEY_ACTIONS_RECIPE_ID_FK,
                Long.toString(id));

        try(Cursor cursor = db.rawQuery(SELECT_ACTIONS_QUERY,null))
        {
            if(cursor.moveToFirst())
            {
                do
                {
                    list.add(cursor.getLong(cursor.getColumnIndex(KEY_ACTIONS_ID)));
                }while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error while trying to get from database " + e.getMessage());
        }
        return list;
    }

    private List<Long> getConstraintsByRecipeId(long id)
    {
        List<Long> list = new ArrayList<Long>();

        SQLiteDatabase db = getReadableDatabase();

        String SELECT_CONSTRAINTS_QUERY = String.format("SELECT %s from %s where %s = %s",
                KEY_CONSTRAINTS_ID,
                TABLE_CONSTRAINTS,
                KEY_CONSTRAINTS_RECIPE_ID_FK,
                Long.toString(id));

        try(Cursor cursor = db.rawQuery(SELECT_CONSTRAINTS_QUERY,null))
        {
            if(cursor.moveToFirst())
            {
                do
                {
                    list.add(cursor.getLong(cursor.getColumnIndex(KEY_CONSTRAINTS_ID)));
                }while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error while trying to get from database " + e.getMessage());
        }
        return list;
    }

    //new
    public LinkedHashMap<Integer,String> getActionsByTriggerName(String triggerName) //returns actions' "method"
    {
      //  List<String> list = new ArrayList<String>();
        LinkedHashMap<Integer,String> hash = new LinkedHashMap<>();
        SQLiteDatabase db = getReadableDatabase();
        String SUB_QUERY = String.format("SELECT %s from %s where %s=%s",KEY_TRIGGER_ID,TABLE_TRIGGERS,KEY_TRIGGER_NAME,triggerName);
        String JOIN_QUERY = String.format("SELECT %s,%s from %s INNER JOIN %s on %s=%s WHERE %s=%s",TABLE_RECIPES+"."+KEY_RECIPES_ID
                ,TABLE_ACTIONS + "." + KEY_ACTIONS_METHOD,
                TABLE_RECIPES,TABLE_ACTIONS,
                TABLE_RECIPES+"."+KEY_RECIPES_ID,
                TABLE_ACTIONS+"."+KEY_ACTIONS_RECIPE_ID_FK,
                TABLE_RECIPES+"."+KEY_RECIPES_TRIGGERS_ID_FK,
                SUB_QUERY);



        /**
         * SELECT actions.method
         * from recipes
         * INNER JOIN actions
         * on recipes.id=actions.recipe_id
         * where recipes.trigger_id=(select id from triggers where name=given)
         */

        try(Cursor cursor = db.rawQuery(JOIN_QUERY,null))
        {
            if(cursor.moveToFirst())
            {
                do
                {
                    hash.put(cursor.getInt(cursor.getColumnIndex(KEY_RECIPES_ID)),cursor.getString(cursor.getColumnIndex(KEY_ACTIONS_METHOD)));
                  //  list.add(cursor.getString(cursor.getColumnIndex(KEY_ACTIONS_METHOD)));
                }while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error while trying to get from database " + e.getMessage());
        }

        return hash;
    }

    //new
    public LinkedHashMap<Integer,String> getConstraintsByTriggerName(String triggerName) //returns constraints' "method"
    {
        //List<String> list = new ArrayList<String>();
        LinkedHashMap<Integer,String> hash = new LinkedHashMap<>();

        SQLiteDatabase db = getReadableDatabase();
        String SUB_QUERY = String.format("SELECT %s from %s where %s=%s",KEY_TRIGGER_ID,TABLE_TRIGGERS,KEY_TRIGGER_NAME,triggerName);
        String JOIN_QUERY = String.format("SELECT %s,%s from %s INNER JOIN %s on %s=%s WHERE %s=%s",TABLE_RECIPES+"."+KEY_RECIPES_ID,
                TABLE_CONSTRAINTS + "." + KEY_CONSTRAINTS_METHOD,
                TABLE_RECIPES,TABLE_CONSTRAINTS,
                TABLE_RECIPES+"."+KEY_RECIPES_ID,
                TABLE_CONSTRAINTS+"."+KEY_CONSTRAINTS_RECIPE_ID_FK,
                TABLE_RECIPES+"."+KEY_RECIPES_TRIGGERS_ID_FK,
                SUB_QUERY);

        /**
         * SELECT constraints.method
         * from recipes
         * INNER JOIN constraints
         * on recipes.id=constraints.recipe_id
         * where recipes.trigger_id=(select id from triggers where name=given)
         */

        try(Cursor cursor = db.rawQuery(JOIN_QUERY,null))
        {
            if(cursor.moveToFirst())
            {
                do
                {
                    hash.put(cursor.getInt(cursor.getColumnIndex(KEY_RECIPES_ID)),cursor.getString(cursor.getColumnIndex(KEY_CONSTRAINTS_METHOD)));
                    //list.add(cursor.getString(cursor.getColumnIndex(KEY_CONSTRAINTS_METHOD)));
                }while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error while trying to get from database " + e.getMessage());
        }

        return hash;

    }

    //new
    public Object[] getConstraintParameters(int recipeId,String constraintName) //returns value
    {
        List<Object> list = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String SELECT_QUERY = String.format("SELECT %s.%s from %s inner join %s on %s.%s = %s.%s where %s.%s = %s AND %s.%s = %s",
                TABLE_CONSTRAINTS,KEY_CONSTRAINTS_ARGUMENTS_VALUE,
                TABLE_CONSTRAINTS,TABLE_CONSTRAINTS_ARGUMENTS,
                TABLE_CONSTRAINTS,KEY_CONSTRAINTS_ID,
                TABLE_CONSTRAINTS_ARGUMENTS, KEY_CONSTRAINTS_ARGUMENTS_CONSTRAINT_ID_FK,
                TABLE_CONSTRAINTS,KEY_CONSTRAINTS_RECIPE_ID_FK, Integer.toString(recipeId),
                TABLE_CONSTRAINTS, KEY_CONSTRAINTS_NAME, constraintName);

        /*
        select constraint.name
        from constraint
        inner join constraintarguments
        on constraint.id = constraintarguments.constraintid
        where constraint.recipeid = given
         */

        try(Cursor cursor = db.rawQuery(SELECT_QUERY,null))
        {
            if(cursor.moveToFirst())
            {
                do
                {
                    list.add(cursor.getString(cursor.getColumnIndex(KEY_CONSTRAINTS_ARGUMENTS_VALUE)));
                }while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error while trying to get from database " + e.getMessage());
        }
        return (list.toArray(new Object[list.size()]));
    }

    //new
    public Object[] getActionParameters(int recipeId,String actionName) //returns value
    {
        List<Object> list = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String SELECT_QUERY = String.format("SELECT %s.%s from %s inner join %s on %s.%s = %s.%s where %s.%s = %s AND %s.%s = %s",
                TABLE_ACTIONS,KEY_ACTIONS_ARGUMENTS_VALUE,
                TABLE_ACTIONS,TABLE_ACTIONS_ARGUMENTS,
                TABLE_ACTIONS,KEY_ACTIONS_ID,
                TABLE_ACTIONS_ARGUMENTS, KEY_ACTIONS_ARGUMENTS_ACTION_ID_FK,
                TABLE_ACTIONS,KEY_ACTIONS_RECIPE_ID_FK, Integer.toString(recipeId),
                TABLE_ACTIONS, KEY_ACTIONS_NAME, actionName);

        /*
        select action.value
        from action
        inner join actionarguments
        on action.id = actionarguments.actionid
        where action.recipeid = given
         */
        try(Cursor cursor = db.rawQuery(SELECT_QUERY,null))
        {
            if(cursor.moveToFirst())
            {

                do
                {
                    list.add(cursor.getString(cursor.getColumnIndex(KEY_ACTIONS_ARGUMENTS_VALUE)));
                }while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error while trying to get from database " + e.getMessage());
        }
        return (list.toArray(new Object[list.size()]));
    }

    public void addRecipe(String recipeName, String triggerName, Multimap<String,String> actions, Multimap<String, String> constraints)
    {


        long tId,rId,aId, cId;



        tId = addTrigger(new Trigger(triggerName,"dummy"));
        rId = addRecipe(new Recipe(recipeName),tId);

        Set<String> t;
        t=actions.keySet();
        //String[] a = tmp.toArray(new String[tmp.size()]);

        for (String str:t)
        {
            //System.out.println(str);
            aId = addAction(new Action(str,"dummy"),rId);
           // addActionArgument(new ActionArgument("dummy"),aId);
            for (String b :actions.get(str))
            {
                addActionArgument(new ActionArgument(b),aId);
            }
        }

        t=constraints.keySet();
        //String[] a = tmp.toArray(new String[tmp.size()]);

        for (String str:t)
        {
            //System.out.println(str);
            cId = addConstraint(new Constraint(str,"dummy"),rId);
            for (String b :actions.get(str))
            {
                addConstraintArgument(new ConstraintArgument(b),cId);
            }
        }
        //return 0;
    }

    public Multimap<String, String[]> getAllRecipes()
    {
        /*
        recipeTitle, triggerName, CSVactions, CSVconstraints
         */
       // List<String> recipes = new ArrayList();
        List<String> tmp;
        Multimap<String, String[]> map =  ArrayListMultimap.create();
        SQLiteDatabase db = getReadableDatabase();

        StringBuilder action, constraint;
        String RECIPE_SELECT_QUERY = String.format("SELECT %s.%s, %s.%s, %s.%s FROM %s INNER JOIN %S ON %s.%s = %s.%s",
                TABLE_RECIPES,KEY_RECIPES_ID,
                TABLE_RECIPES,KEY_RECIPES_NAME,
                TABLE_TRIGGERS, KEY_TRIGGER_NAME,
                TABLE_RECIPES,TABLE_TRIGGERS,
                TABLE_TRIGGERS,KEY_TRIGGER_ID,
                TABLE_RECIPES,KEY_RECIPES_TRIGGERS_ID_FK); /*
                                                            SELECT recipe.id, recipe.name, trigger.name from
                                                            recipe inner join trigger
                                                            on trigger.id = recipe.triggerid
                                                             */

        String ACTION_QUERY, CONSTRAINT_QUERY;

        try(Cursor cursor = db.rawQuery(RECIPE_SELECT_QUERY,null))
        {
            if(cursor.moveToFirst())
            {

                do
                {
                    tmp = new ArrayList<>();
                    tmp.add(cursor.getString(cursor.getColumnIndex(KEY_TRIGGER_NAME)));

                    /* actions */
                    ACTION_QUERY = String.format("SELECT %s FROM %s where %s = %s",
                            KEY_ACTIONS_NAME,TABLE_ACTIONS,KEY_ACTIONS_RECIPE_ID_FK,cursor.getString(cursor.getColumnIndex(KEY_RECIPES_ID)));
                                /*
                                SELECT name from actions where recipeId = given
                                 */
                    action = new StringBuilder("");
                    try(Cursor aCursor = db.rawQuery(ACTION_QUERY,null))
                    {
                        if(aCursor.moveToFirst())
                        {
                            do
                            {
                                action.append(aCursor.getString(aCursor.getColumnIndex(KEY_ACTIONS_NAME)) + ", ");
                            }while (aCursor.moveToNext());
                        }
                        action.delete(action.length()-2,action.length()-1);
                        tmp.add(action.toString());
                    }
                    catch (Exception e)
                    {
                        Log.d(TAG, "Error while trying to get from database " + e.getMessage());
                    }
                    /* constraints */
                    CONSTRAINT_QUERY = String.format("SELECT %s FROM %s where %s = %s",
                            KEY_CONSTRAINTS_NAME,TABLE_CONSTRAINTS,KEY_CONSTRAINTS_RECIPE_ID_FK,cursor.getString(cursor.getColumnIndex(KEY_RECIPES_ID)));
                                /*
                                SELECT name from constraints where recipeId = given
                                 */
                    constraint = new StringBuilder("");
                    try(Cursor aCursor = db.rawQuery(CONSTRAINT_QUERY,null))
                    {
                        if(aCursor.moveToFirst())
                        {
                            do
                            {
                                constraint.append(aCursor.getString(aCursor.getColumnIndex(KEY_CONSTRAINTS_NAME)) + ", ");
                            }while (aCursor.moveToNext());
                        }
                        constraint.delete(constraint.length()-2,constraint.length()-1);
                        tmp.add(constraint.toString());
                    }
                    catch (Exception e)
                    {
                        Log.d(TAG, "Error while trying to get from database " + e.getMessage());
                    }
                    map.put(cursor.getString(cursor.getColumnIndex(KEY_RECIPES_NAME)),tmp.toArray(new String[tmp.size()]));

                   // list.add(cursor.getString(cursor.getColumnIndex(KEY_ACTIONS_ARGUMENTS_VALUE)));
                }while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error while trying to get from database " + e.getMessage());
        }

        return null;
    }

}
