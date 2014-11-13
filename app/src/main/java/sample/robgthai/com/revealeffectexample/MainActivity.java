package sample.robgthai.com.revealeffectexample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final int REVEAL_DURATION = 300;
        private ImageView image;
        private TextView txtStatus;

        private View.OnClickListener revealClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int color;
                if(v.getId() == R.id.btnClick) {
                    color = Color.GRAY;
                }else if(v.getId() == R.id.txtHello) {
                    color = Color.GREEN;
                }else {
                    color = Color.BLACK;
                }

                revealV21(image, getOrigin(v), color);
            }
        };

        public PlaceholderFragment() {
        }

        private void setStatus(String text) {
            txtStatus.setText(text);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            txtStatus = (TextView) rootView.findViewById(R.id.txtStatus);
            TextView txtHello = (TextView) rootView.findViewById(R.id.txtHello);
            Button btnClick = (Button) rootView.findViewById(R.id.btnClick);

            image = (ImageView) rootView.findViewById(R.id.image);

            txtHello.setOnClickListener(revealClickListener);
            btnClick.setOnClickListener(revealClickListener);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(image.getVisibility() == View.VISIBLE) {
                        hideV21(image, getOrigin(v));
                    }
                }
            });

            return rootView;
        }

        private Pair<Integer, Integer> getOrigin(View view) {
            int cx = (view.getLeft() + view.getRight()) / 2;
            int cy = (view.getTop() + view.getBottom()) / 2;

            return Pair.create(cx, cy);
        }

        @SuppressLint("NewApi")
        private void revealV21(View myView, Pair<Integer, Integer> coord, int bgColor) {

            // get the final radius for the clipping circle
            int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

            setStatus(String.format("Reveal origin[%d, %d], to[%d]"
                    , coord.first.intValue(), coord.second.intValue(), finalRadius));

            myView.setBackgroundColor(bgColor);
            // create the animator for this view (the start radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, coord.first, coord.second, 0, finalRadius);

            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.setDuration(REVEAL_DURATION);
            anim.start();
        }

        @SuppressLint("NewApi")
        private void hideV21(final View myView, Pair<Integer, Integer> coord) {

            // get the initial radius for the clipping circle
            int initialRadius = myView.getWidth();

            setStatus(String.format("Collapse origin[%d, %d], from[%d]"
                    , coord.first.intValue(), coord.second.intValue(), initialRadius));

            // create the animation (the final radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, coord.first, coord.second, initialRadius, 0);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            });
            anim.setDuration(REVEAL_DURATION);
            anim.start();
        }
    }
}
