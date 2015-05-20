package to.done.com.drawerntoolbar;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Avinazz on 21/05/15.
 */
public class ManualActionBarDrawerToggle extends ActionBarDrawerToggle {
    private static final float MENU_POSITION = 0f;
    private static final float ARROW_POSITION = 1.0f;

    private final int animationLength;
    private final DrawerLayout drawerLayout;
    private final Activity activity;
    private State currentState;

    private enum State { UP_ARROW, MENU }

    public ManualActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, int openDrawerContentDescriptionResource, int closeDrawerContentDescriptionResource) {
        super(activity, drawerLayout, openDrawerContentDescriptionResource, closeDrawerContentDescriptionResource);
        animationLength = activity.getResources().getInteger(android.R.integer.config_shortAnimTime);
        this.drawerLayout = drawerLayout;
        this.activity = activity;
        currentState = State.MENU;
    }

    public void animateToBackArrow() {
        ValueAnimator anim = ValueAnimator.ofFloat(MENU_POSITION, ARROW_POSITION);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                onDrawerSlide(drawerLayout, slideOffset);
            }
        });

        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(animationLength);
        anim.start();

        currentState = State.UP_ARROW;
    }

    public void animateToMenu() {
        ValueAnimator anim = ValueAnimator.ofFloat(ARROW_POSITION, MENU_POSITION);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                onDrawerSlide(drawerLayout, slideOffset);
            }
        });

        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(animationLength);
        anim.start();

        currentState = State.MENU;
    }

    public boolean isUpArrow(){
        if (currentState==State.MENU){
            return false;
        }

        return true;
    }


}
