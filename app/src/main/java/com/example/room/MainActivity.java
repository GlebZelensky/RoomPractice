package com.example.room;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    GameDB db;
    EditText name, price;
    Spinner category;
    ListView categories;
    GameDAO gameDAO;
    Spinner listCategory;

    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String category = listCategory.getSelectedItem().toString();
            new SelectFromDB().execute(category);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Список Игр
        categories = findViewById(R.id.list_w_category);

        // Добавление новой Игры
        name = findViewById(R.id.name_input);
        category = findViewById(R.id.category_input);
        price = findViewById(R.id.price_input);

        gameDAO = GameDB.create(this, false).gameDAO();

        listCategory = findViewById(R.id.category_list);
        listCategory.setOnItemSelectedListener(itemSelectedListener);
    }

    public void insertToDB(View view) {
        String name = this.name.getText().toString();
        String category = this.category.getSelectedItem().toString();
        String price = this.price.getText().toString();

        InsertToDB insertToDB = new InsertToDB();
        insertToDB.execute(name, category, price);
    }

    public void onClearClick(View v) {
        new deleteAllGamesAsyncTask(gameDAO).execute();
    }

    /* public void onOrderClick(View v) {
        gameDAO.orderedBy();
    } */


    class SelectFromDB extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... strings) {
            ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
            List<Game> games = new ArrayList<>();
            if (strings[0].equalsIgnoreCase("All")) {
                games = gameDAO.selectAll();
            }
            if (!strings[0].equalsIgnoreCase("All")) {
                games = gameDAO.findByVendor(strings[0]);
            }
            for (int i = 0; i < games.size(); i++) {
                HashMap<String, String> map = new HashMap();
                map.put("_id", String.valueOf(i+1)); //games.get(i)._id
                map.put("name", games.get(i).name);
                map.put("vendor", games.get(i).vendor);
                map.put("price", String.valueOf(games.get(i).price));
                arrayList.add(map);
            }
            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> products) {
            super.onPostExecute(products);
            SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(), products, R.layout.product_item, new String[]{"_id", "name", "vendor", "price"}, new int[]{R.id._id, R.id.name, R.id.vendor, R.id.price});
            categories.setAdapter(simpleAdapter);
        }


    }

    class InsertToDB extends AsyncTask<String, Void, Game> {

        @Override
        protected Game doInBackground(String... strings) {
            Game game = new Game(strings[0], strings[1], Float.parseFloat(strings[2]));
            gameDAO.insert(game);
            return game;
        }

        @Override
        protected void onPostExecute(Game game) {
            super.onPostExecute(game);
            new SelectFromDB().execute("All");
        }
    }

    class deleteAllGamesAsyncTask extends AsyncTask<Void, Void, Void>{
        private GameDAO mAsyncTaskDao;

        deleteAllGamesAsyncTask(GameDAO dao) {
            mAsyncTaskDao = dao;
        }

        void toUpdate(){
            new SelectFromDB().execute("All");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            toUpdate();
            return null;
        }

    }
}