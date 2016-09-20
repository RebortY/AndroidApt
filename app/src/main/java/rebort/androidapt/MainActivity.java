package rebort.androidapt;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.aptdc.AnDataCollect;
import com.aptdc.AptDC;
import com.aptdc.DCEventParam;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "MainActivity";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
            }
        });
    }

    @AnDataCollect(tagname = TAG ,action = "onCreateOptionsMenu")
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        AptDC.log(this,"onCreateOptionsMenu");

        return true;
    }

    @AnDataCollect(tagname = TAG ,action = "onOptionsItemSelected")
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        AptDC.log(this,"onOptionsItemSelected");
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
