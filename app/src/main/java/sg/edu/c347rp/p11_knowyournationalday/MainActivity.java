package sg.edu.c347rp.p11_knowyournationalday;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvNdpList;
    ArrayList<String> alNdp = new ArrayList<String>();
    ArrayAdapter aa;
    SharedPreferences settings;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences("JSON", MODE_PRIVATE);
        if (!settings.getString("Access", "").equals("auth")) {
            login();
        }

        lvNdpList = (ListView) findViewById(R.id.lv);
        alNdp.add("Singapore National Day");
        alNdp.add("Singapore is 52 Years Old");
        alNdp.add("The theme is '#OneNationTogether'");

        aa = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, alNdp);

        lvNdpList.setAdapter(aa);
    }

    public void login() {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout login =
                (LinearLayout) inflater.inflate(R.layout.passphrase, null);
        final EditText etAccessCode = (EditText) login
                .findViewById(R.id.editTextPassPhrase);

        dialog = new AlertDialog.Builder(this)
                .setView(login)
                .setTitle("Please Enter")
                .setPositiveButton("Done", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        if (etAccessCode.getText().toString().equals("738964")) {
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("Access", "auth");
                            editor.commit();
                            dialog.dismiss();
                        } else {
                            etAccessCode.setHint("Your Access Code was incorrect, please try again ");
                        }
                    }
                });
            }

        });
        dialog.show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Tally against the respective action item clicked
        //  and implement the appropriate action
        if (item.getItemId() == R.id.send) {
        } else if (item.getItemId() == R.id.quit) {
        }
        if (item.getItemId() == R.id.quit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure?")
                    // Set text for the positive button and the corresponding
                    //  OnClickListener when it is clicked
                    .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    // Set text for the negative button and the corresponding
                    //  OnClickListener when it is clicked
                    .setNegativeButton("Not Really", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (item.getItemId() == R.id.send) {

            String[] list = new String[]{"Email", "SMS"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select the way to enrich your friend")
                    // Set the list of items easily by just supplying an
                    //  array of the items
                    .setItems(list, new DialogInterface.OnClickListener() {
                        // The parameter "which" is the item index
                        // clicked, starting from 0
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Intent email = new Intent(Intent.ACTION_SEND);
                                // Put essentials like email address, subject & body text
                                email.putExtra(Intent.EXTRA_EMAIL,
                                        new String[]{"jason_lim@rp.edu.sg"});
                                email.putExtra(Intent.EXTRA_SUBJECT,
                                        "Test Email from C347");
                                email.putExtra(Intent.EXTRA_TEXT, alNdp.get(0) + "\n" + alNdp.get(1) + "\n" + alNdp.get(2));
                                // This MIME type indicates email
                                email.setType("message/rfc822");
                                // createChooser shows user a list of app that can handle
                                // this MIME type, which is, email
                                startActivity(Intent.createChooser(email,
                                        "Choose an Email client :"));

                            } else {
                                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                sendIntent.putExtra(alNdp.get(0) + "\n" + alNdp.get(1) + "\n" + alNdp.get(2), "default content");
                                sendIntent.setType("vnd.android-dir/mms-sms");
                                startActivity(sendIntent);
                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (item.getItemId() == R.id.quiz) {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final LinearLayout quiz =
                    (LinearLayout) inflater.inflate(R.layout.quiz, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Test Yourself")
                    .setView(quiz)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            int score = 0;

                            final RadioGroup rd1 = (RadioGroup) quiz.findViewById(R.id.rg1);
                            final RadioGroup rd2 = (RadioGroup) quiz.findViewById(R.id.rg2);
                            final RadioGroup rd3 = (RadioGroup) quiz.findViewById(R.id.rg3);

                            int selectedId1 = rd1.getCheckedRadioButtonId();
                            int selectedId2 = rd2.getCheckedRadioButtonId();
                            int selectedId3 = rd3.getCheckedRadioButtonId();

                            RadioButton checkedRadio1 = (RadioButton) quiz.findViewById(selectedId1);
                            RadioButton checkedRadio2 = (RadioButton) quiz.findViewById(selectedId2);
                            RadioButton checkedRadio3 = (RadioButton) quiz.findViewById(selectedId3);

                            if (checkedRadio1.getText().toString().equals("No")){score ++;}
                            if (checkedRadio2.getText().toString().equals("Yes")){score ++;}
                            if (checkedRadio3.getText().toString().equals("Yes")){score ++;}

                            Toast.makeText(MainActivity.this, "Your score is " + score,
                                    Toast.LENGTH_LONG).show();

                            score = 0;
                            dialog.dismiss();
                        }
                    })

                    .setNegativeButton("DON'T KNOW LAH", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }
}
