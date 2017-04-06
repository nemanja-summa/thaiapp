package io.summa.tutorial.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import io.summa.tutorial.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView mIvFacebook, mIvTwitter;
    Button mBtnSimulate, mBtnPractice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIvFacebook = (ImageView) findViewById(R.id.ivFacebook);
        mIvTwitter = (ImageView) findViewById(R.id.ivTwitter);

        mBtnSimulate = (Button) findViewById(R.id.btnSimulateExam);
        mBtnPractice = (Button) findViewById(R.id.btnPracticeExam);

        mIvFacebook.setOnClickListener(this);
        mIvTwitter.setOnClickListener(this);
        mBtnSimulate.setOnClickListener(this);
        mBtnPractice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivFacebook) {
            Toast.makeText(this, "Facebook link goes here", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.ivTwitter) {
            Toast.makeText(this, "Twitter link goes here", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.btnSimulateExam) {
            Intent i = new Intent(MainActivity.this, LicenceTestActivity.class);
            i.putExtra(LicenceTestActivity.EXTRA_CONTEXT, LicenceTestActivity.EXTRA_SIMULATION);
            startActivity(i);
        } else if (view.getId() == R.id.btnPracticeExam) {
            Intent i = new Intent(MainActivity.this, LicenceTestActivity.class);
            i.putExtra(LicenceTestActivity.EXTRA_CONTEXT, LicenceTestActivity.EXTRA_PRACTICE);
            startActivity(i);
        }
    }
}
