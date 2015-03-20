package jp.techinstitute.ti_noda.paintapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// CustomView
		final CustomView customView = (CustomView)findViewById(R.id.paintview);

		// 赤ボタン押下時
		findViewById(R.id.btn_red).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				customView.SetColor(Color.RED);
			}
		});

		// 緑ボタン押下時
		findViewById(R.id.btn_green).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				customView.SetColor(Color.GREEN);
			}
		});

		// 青ボタン押下時
		findViewById(R.id.btn_blue).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				customView.SetColor(Color.BLUE);
			}
		});

		// 消しゴムボタン押下時
		findViewById(R.id.btn_erase).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 背景色を白にしているので、白色で消す
				customView.SetColor(Color.WHITE);
			}
		});

		// 戻るボタン押下時
		findViewById(R.id.btn_undo).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 背景色を白にしているので、白色で消す
				customView.Undo(Color.WHITE);
			}
		});
	}



	/***********************************************************
	 * ここから下はメニュー項目関連(今回の講義では使用しない)
	 **********************************************************/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
