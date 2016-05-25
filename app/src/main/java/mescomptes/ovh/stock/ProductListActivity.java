package mescomptes.ovh.stock;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import org.openerpclient.lib.Model;
import org.openerpclient.lib.Connection;
import org.openerpclient.lib.ConnectorType;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import java.util.HashMap;
import org.openerpclient.lib.Connection;
import org.openerpclient.lib.ConnectionInfo;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {
    private Connection connection;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ConnectionInfo info = new Gson().fromJson(getIntent().getStringExtra("CONNECTION_INFO"), ConnectionInfo.class);
        this.connection = new Connection(info);

        AsyncLogin mAuthTask = new AsyncLogin();
        mAuthTask.execute((Void) null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    private class AsyncLogin extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            //ProductListActivity.this.connection.login();
            Connection con = new Connection("192.168.0.16", 8069, "android", "admin", "admin", ConnectorType.JSONRPC);
            if (con.login()) {
                System.out.println("Connected");
            }
            else {
                System.out.println("Connection Failed");
            }
            Model partner = con.getModel("res.users");
            Object[] ids = partner.search("[]");
            String[] fields = {"id", "name", "email", "password", "login"};

            ArrayList datas = partner.read(ids, fields);
            for (int i=0; i <= datas.size()-1; i++){
                ((HashMap) datas.get(i)).get("name");
                System.out.println("------>"+((HashMap) datas.get(i)).get("name"));
            }
            return true;
        }
    }

}
