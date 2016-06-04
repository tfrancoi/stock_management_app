package mescomptes.ovh.stock;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.openerpclient.lib.Connection;
import org.openerpclient.lib.ConnectionInfo;
import org.openerpclient.lib.Model;

import java.util.ArrayList;
import java.util.HashMap;

import mescomptes.ovh.stock.data.Product;

public class ProductListActivity extends AppCompatActivity {
    private Connection connection;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private ArrayList<Product> productList;
    private ArrayAdapter<Product> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ConnectionInfo info = new Gson().fromJson(getIntent().getStringExtra("CONNECTION_INFO"), ConnectionInfo.class);
        this.connection = new Connection(info);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(getListAdapter());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                arrayAdapter.notifyDataSetChanged();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private ListAdapter getListAdapter() {
        if (this.productList == null) {
            this.productList = getProductList();
        }
        if (this.arrayAdapter == null) {
            this.arrayAdapter = new ArrayAdapter<Product>(this, android.R.layout.simple_list_item_1, this.productList);
        }
        return arrayAdapter;
    }

    private ArrayList<Product> getProductList() {
        Model product = this.connection.getModel("perso.product");
        int[] ids = product.search("[]");
        ArrayList<HashMap> results = product.read(ids, "['name', 'quantity']");
        return Product.parseProductData(results);
    }

    private class AsyncLogin extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            //ProductListActivity.this.connection.login();
            return true;
        }
    }

}
