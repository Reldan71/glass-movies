package com.stefansundin.glass.movies;

import java.io.File;

import android.widget.ListView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public class Touchpad implements
		GestureDetector.BaseListener,
		GestureDetector.FingerListener,
		GestureDetector.ScrollListener {

	final float THRESHOLD = 50;
	float lastDisplacement = 0;
	ListView mListView;
	MainActivity mActivity;

	public Touchpad(ListView pListView, MainActivity pActivity) {
		mListView = pListView;
		mActivity = pActivity;
	}

	public boolean onGesture(Gesture gesture) {
		String filename = mListView.getSelectedItem().toString();
		
		if (gesture == Gesture.TAP) {
			
			if(filename == "<Back to Root>"){
				mActivity.loadMovieDir(mActivity.mMovieDirectory);
				return true;
			}
						
			File checkFile = new File(mActivity.mCurrentDirectory + "/" +  filename);
			if(checkFile.isDirectory() == true) {
				mActivity.loadMovieDir(mActivity.mCurrentDirectory + "/" +  filename);
			}
			else {
				mActivity.launchVideo(mActivity.mCurrentDirectory + "/" + filename);
			}
			return true;
		}
		else if (gesture == Gesture.TWO_TAP) {
			mActivity.say(filename);
			return true;
		}
		else if (gesture == Gesture.SWIPE_DOWN){
			if(mActivity.mMovieDirectory.equalsIgnoreCase(mActivity.mCurrentDirectory) || mActivity.mCurrentDirectory.contains(mActivity.mMovieDirectory) == false){
				return false;
			}
			else{
				File backFile = new File(mActivity.mCurrentDirectory);
				mActivity.loadMovieDir(backFile.getParentFile().getPath());
				return true;
			}
		}
		/*else if (gesture == Gesture.SWIPE_RIGHT) {
			Log.d("stefan", "Gesture.SWIPE_RIGHT");
			return true;
		}
		else if (gesture == Gesture.SWIPE_LEFT) {
			Log.d("stefan", "Gesture.SWIPE_LEFT");
			return true;
		}*/
		
		return false;
	}

	public void onFingerCountChanged(int previousCount, int currentCount) {
		lastDisplacement = 0;
	}

	public boolean onScroll(float displacement, float delta, float velocity) {
		//Log.d("stefan", "onScroll("+displacement+", "+delta+", "+velocity+")");
		if (Math.abs(displacement-lastDisplacement) > THRESHOLD) {
			if (displacement > lastDisplacement) {
				mListView.setSelection(mListView.getSelectedItemPosition()+1);
			}
			else {
				mListView.setSelection(mListView.getSelectedItemPosition()-1);
			}
			lastDisplacement = displacement;
			return true;
		}
		return false;
	}

}
