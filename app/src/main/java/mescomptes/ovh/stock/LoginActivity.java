package mescomptes.ovh.stock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import org.openerpclient.lib.Connection;
import org.openerpclient.lib.ConnectorType;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText email;
    private EditText password;
    private View mProgressView;
    private View mLoginFormView;
    private EditText host;
    private EditText port;
    private EditText database;
    private SharedPreferences pref;

    public static final String HOST_PREF = "HOSTNAME";
    public static final String PASSWORD_PREF = "PASSWORD";
    public static final String PREF_NAME = "STOCK_CONNECTION";
    public static final String LOGIN_PREF = "LOGIN";
    public static final String PORT_PREF = "PORT";
    public static final String DATABASE_REF = "DATABASE";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        this.host = (EditText) findViewById(R.id.host);
        this.port = (EditText) findViewById(R.id.port);
        this.database = (EditText) findViewById(R.id.database);
        initDefault();


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void initDefault() {
        this.pref = getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        email.setText(this.pref.getString(LOGIN_PREF, "admin"));
        port.setText(Integer.toString(this.pref.getInt(PORT_PREF, 8069)));
        host.setText(this.pref.getString(HOST_PREF, ""));
        password.setText(this.pref.getString(PASSWORD_PREF, ""));
        database.setText(this.pref.getString(DATABASE_REF, ""));

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        email.setError(null);
        password.setError(null);

        // Store values at the time of the login attempt.
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        String database = this.database.getText().toString();
        int port = Integer.parseInt(this.port.getText().toString());
        String hostname = this.host.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            this.email.setError(getString(R.string.error_field_required));
            focusView = this.email;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(hostname, port, database, email, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String email;
        private final String password;
        private final int port_number;
        private final String hostname;
        private final String database;
        private Connection con;

        UserLoginTask(String host, int port_number, String database, String email, String password) {
            this.email = email;
            this.password = password;
            this.port_number = port_number;
            this.hostname = host;
            this.database = database;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            this.con = new Connection(this.hostname, this.port_number, this.database, this.email, this.password, ConnectorType.JSONRPC);
            Boolean res = this.con.login();
            if (res) {
                System.out.println("Connection ok");
                return true;
            } else {
                System.out.println("Connection Failed");
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                //finish();
                setPref();
                String connection_ser = new Gson().toJson(this.con.getConnectionInfo());
                Intent nextStep = new Intent(LoginActivity.this, ProductListActivity.class);
                nextStep.putExtra("CONNECTION_INFO", connection_ser);
                nextStep.putExtra("LOGIN", this.email);
                startActivity(nextStep);
            } else {
                LoginActivity.this.password.setError(getString(R.string.error_incorrect_password));
                LoginActivity.this.password.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        private void setPref() {
            SharedPreferences.Editor editor = LoginActivity.this.pref.edit();
            editor.putString(HOST_PREF, this.hostname);
            editor.putInt(PORT_PREF, this.port_number);
            editor.putString(DATABASE_REF, this.database);
            editor.putString(LOGIN_PREF, this.email);
            editor.putString(PASSWORD_PREF, this.password);
            editor.apply();
        }
    }
}

