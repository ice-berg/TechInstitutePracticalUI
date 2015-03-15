package jp.techinstitute.ti_noda.paintapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CustomView extends View {
	/**
	 * コンストラクタ
	 * ※使用しないので、書かなくてもOK
	 * @param context
	 */
	public CustomView(Context context) {
		super(context);
	}

	/**
	 * スタイルを指定する場合に使用するコンストラクタ
	 * ※使用しないので、書かなくてもOK
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CustomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * XMLで定義する場合に使用するコンストラクタ
	 * @param context
	 * @param attrs
	 */
	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// フォーカスをCustomViewに当てる
		setFocusable(true);
		// Paint(描画する線の太さや色などの情報を持っている)の初期設定を行う
		initPaint();
	}

	// 描画データを保持するためのもの
	private Bitmap mBitmap;
	// mBitmapに描画データを書き込むためのもの
	private Canvas mCanvas;

	// タッチ開始からタッチ終了までに描いているデータを一時的に保持するためのもの(mBitmapには未書き込み分)
	private Path mPath;

	// 描画する線の太さや色を設定するもの
	private Paint mPaint;

	/**
	 * Paint(描画する線の太さや色などの情報を持っている)の初期設定を行う
	 */
	private void initPaint() {
		// Pathの初期化(「インスタンスを生成する」という)
		mPath = new Path();

		// Paintの初期化(「インスタンスを生成する」という)
		mPaint = new Paint();
		mPaint.setAntiAlias(true);	// アンチエイリアスを有効にする(色を滑らかに変化させる)
		mPaint.setDither(true);		// ディザを有効にする(少ない色で色調を表現できるようにする)
		mPaint.setColor(Color.RED);	// 線の色を赤にする
		mPaint.setStyle(Paint.Style.STROKE);	// 輪郭線を描く。塗りつぶす場合はPaint.Style.FILL
		mPaint.setStrokeJoin(Paint.Join.ROUND);	// つなぎ目を丸くする
		mPaint.setStrokeCap(Paint.Cap.ROUND);	// 端点を丸くする
		mPaint.setStrokeWidth(12);	// 線幅を12pixelにする
	}

	/**
	 * 画面サイズの変更時に呼ばれる
	 * @param w
	 * @param h
	 * @param oldw
	 * @param oldh
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		Log.d("CustomView", "onSizeChanged Width:" + w + ",Height:" + h);

		// 描画データを保持するBitmap(画面のサイズに合わせる必要があるので、ここで作成する)
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		// Bitmapに描画データを書き込むためのCanvasを初期化(「インスタンスを生成する」という)
		mCanvas = new Canvas(mBitmap);
	}

	/**
	 * 画面の再描画をする
	 * ※再描画なので、一から(真っ白な状態から)書き直しを行う
	 * @param canvas 画面に描画するためのもの
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// 今までの描画データ(mBitmap)を描画する
		canvas.drawBitmap(mBitmap, 0, 0, mPaint);
		// タッチ開始からタッチ終了までに描いているデータ(mBitmapには未保存分)を描画する
		canvas.drawPath(mPath, mPaint);
	}

	/**
	 * 画面をタッチ(CustomViewをタッチ)した時に呼ばれる
	 * @param event
	 * @return
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// タッチした位置
		float x = event.getX();
		float y = event.getY();

		// Actionはタッチしたのか、タッチ状態で動いたのか、タッチをやめたのか、という状態のこと
		switch (event.getAction()) {
			// タッチ開始
			case MotionEvent.ACTION_DOWN:
				touch_start(x, y);
				break;
			// 画面をタッチした状態で動かした
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				break;
			// タッチ終了
			case MotionEvent.ACTION_UP:
				touch_up();
				break;
		}

		// 再描画する(onDrawメソッドを直接呼ぶのではなく、invalidateメソッドで間接的にonDrawメソッドを呼び出して再描画する)
		invalidate();
		return true;
	}

	// 前回の位置として保持するためのもの
	private float mX, mY;
	// タッチした状態で移動した分が、短かったりすると、描画データとして保持する必要がないので、それを判断するための閾値
	private static final float TOUCH_TOLERANCE = 4;
	/**
	 * タッチ開始時の処理
	 * @param x
	 * @param y
	 */
	private void touch_start(float x, float y) {
		Log.d("CustomView", "touch_start");
		// 今からタッチ分の描画データをmPathに一時的に保持してもらうので、ここでリセットしておく
		mPath.reset();
		// どこから開始するか設定する
		mPath.moveTo(x, y);

		// 今の位置を覚えさせておく(次のタッチ移動した処理で、前回の位置として扱う)
		mX = x;
		mY = y;
	}

	/**
	 * タッチした状態で、指を動かした場合の処理
	 * @param x
	 * @param y
	 */
	private void touch_move(float x, float y) {
		Log.d("CustomView", "touch_move");
		// 今の位置と、前回の位置の差から、移動量を求める
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);

		// 閾値より移動量が多ければ線をつなぐ(全然移動していなければ、無駄なデータになるので、無視するため)
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			// 前回の位置(mX, mY)から今の位置(前回の位置と足して割ることで補正している)まで、線を描画する
			// ※2次ベジェ曲線という滑らかな線(曲線)になる
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			// 今の位置(次の時の前回位置)を保持する
			mX = x;
			mY = y;
		}
	}

	/**
	 * タッチ終了時の処理
	 */
	private void touch_up() {
		Log.d("CustomView", "touch_up");

		// 最後の位置を指定して線を描画する
		mPath.lineTo(mX, mY);
		// mCanvasを使って、mBitmapに描画データを書き込む
		mCanvas.drawPath(mPath, mPaint);

		// mPathはタッチ時の描画データを一時的に持っているだけなので、mBitmapに書き込み終わっているので、ここでリセットしておく
		mPath.reset();
	}

	/**
	 * 色を設定する
	 * @param color
	 */
	public void SetColor(int color){
		mPaint.setColor(color);
	}
}
