package com.sstc.shivam.petrolpumplocator.detailsScreen;

import android.app.Activity;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.sstc.shivam.petrolpumplocator.R;
import com.sstc.shivam.petrolpumplocator.petrolPumpDetails.PetrolPumpDetails;

/**
 * Created by shiva on 24-03-2017.
 */

class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

    Activity currentActivitiy;

   public MyGestureDetector(Activity a)
    {
        currentActivitiy=a;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        try {
            float slope = (e1.getY() - e2.getY()) / (e1.getX() - e2.getX());
            float angle = (float) Math.atan(slope);
            float angleInDegree = (float) Math.toDegrees(angle);
            // left to right
            if (e1.getX() - e2.getX() > 20 && Math.abs(velocityX) > 20) {
                if ((angleInDegree < 45 && angleInDegree > -45)) {
                    currentActivitiy.startActivity(new Intent(currentActivitiy, PetrolPumpDetailsActivity.class));

                    currentActivitiy.overridePendingTransition(
                            R.anim.slide_in_left, R.anim.slide_out_right);
                    currentActivitiy.finish();
                }
                // right to left fling
            } else if (e2.getX() - e1.getX() > 20
                    && Math.abs(velocityX) > 20) {
                if ((angleInDegree < 45 && angleInDegree > -45)) {
                    currentActivitiy.startActivity(new Intent(currentActivitiy, PetrolPumpDetailsActivity.class));
                    currentActivitiy.overridePendingTransition(
                            R.anim.slide_in_right, R.anim.slide_out_left);
                   currentActivitiy.finish();

                }
            }
            return true;
        } catch (Exception e) {
            // nothing
        }
        return false;
    }
}