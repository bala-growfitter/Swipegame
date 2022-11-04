package com.example.cycleswipe;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Context ctx;
    FrameLayout frame;
    TextView startLabel,scoreLabel;
    ImageView cycle,orange,berry,fries,sausage,burger,soundon,soundoff;
    int screenheight,screenwidth,cycleX,cycleY,movingX,frameheight,framewidth,cycleheight,cycleweight;
    Timer timer = new Timer();
    Handler handler = new Handler();

    //Position
     int manX,manY,burgerX,burgerY,friesX,friesY,sausageX,sausageY,orangeX,orangeY,berryX,berryY;
    private float deltax,deltay,distancex,distancey,movedx,movedy;

     //Speed
     int orangespeed,berryspeed,burgerspeed,friesspeed,sausagespeed;

    //Size
//     int boxSize1,frameWidth,frameHeight,screenWidth,screenHeight,boxSize2;

    //Score
     int score = 0;

     MediaPlayer hit,over;
    //Flags
    private boolean action_flag = false;
    private boolean start_flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frame=findViewById(R.id.frame);
        cycle=findViewById(R.id.cycle);
        orange=findViewById(R.id.orange);
        berry=findViewById(R.id.berry);
        fries=findViewById(R.id.fries);
        burger=findViewById(R.id.burger);
        sausage=findViewById(R.id.sausage);
        scoreLabel=findViewById(R.id.scoreLabel);
        soundon=findViewById(R.id.speakeron);
        soundoff=findViewById(R.id.speakeroff);
        getSupportActionBar().hide();

        hit=MediaPlayer.create(this,R.raw.hit);
        over=MediaPlayer.create(this,R.raw.over);
        //Get the screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenheight=size.y;
        screenwidth=size.x;
        movingX= (int) (screenwidth/3);
        cycleX= (int) cycle.getX();
        cycleY= (int) cycle.getY();
        Glide.with(this).load(R.drawable.walkingman).into(cycle);

        burgerspeed = Math.round(screenwidth/45F);
        friesspeed = Math.round(screenwidth/45F);
        sausagespeed = Math.round(screenwidth/45F);
        orangespeed = Math.round(screenwidth/65F);
        berryspeed = Math.round(screenwidth/65F);

        burger.setX(-80);
        burger.setY(-80);
        fries.setX(-80);
        fries.setY(-80);
        sausage.setX(-80);
        sausage.setY(-80);
        orange.setX(-80);
        orange.setY(-80);
        berry.setX(-80);
        berry.setY(-80);

//        cycle.setOnTouchListener(new OnSwipeTouchListener(ctx){
//            @Override
//            public void onSwipeRight() {
//                cycleX=cycleX+movingX;
//                cycle.setX(cycleX);
//            }
//            @Override
//            public void onSwipeLeft() {
//                cycleX=cycleX-movingX;
//                cycle.setX(cycleX);
//            }
//        });


        cycle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        deltax=motionEvent.getX();
                        deltay=motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        action_flag=true;
                        movedx=motionEvent.getX();
                        distancex=movedx-deltax;
                        cycle.setX(cycle.getX()+distancex);
                        scoreLabel.setText("Score : " + score);
                        cycleX= (int) cycle.getX();
                }
                return true;
            }
        });

        soundon.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           soundon.setVisibility(View.INVISIBLE);
                                           soundoff.setVisibility(View.VISIBLE);
                                           hit.setVolume(0,0);
                                           over.setVolume(0,0);
                                       }
                                   }
        );
        soundoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundoff.setVisibility(View.INVISIBLE);
                soundon.setVisibility(View.VISIBLE);
                hit.setVolume(1,1);
                over.setVolume(1,1);
            }
        });
    }
    public void changeposition()
    {
        hitCheck();
        //Orange

        orangeY += orangespeed;

        if (orangeY >= frameheight) {
            orangeY=-100;
//            orangeX=-100;
        }
        if(orangeY <= 17){
            //orangeX=500;
            orangeX = (int)(Math.random() * 3)* movingX + orange.getWidth();
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        //Berry

        berryY += berryspeed;

        if (berryY >= frameheight) {
            berryY=-100;
//            berryX=-100;
        }
        if(berryY <= 17){
            //orangeX=500;
            berryX =  (int)(Math.random() * 3)* movingX + berry.getWidth();
        }
        berry.setX(berryX);
        berry.setY(berryY);

        //Burger

        burgerY += burgerspeed;

        if (burgerY >= frameheight) {
            burgerY=-100;
//            burgerX=-100;
        }
        if(burgerY <= 24){
            //orangeX=500;
            burgerX =  (int) (Math.random() * 3)  * movingX + burger.getWidth();
        }
        burger.setX(burgerX);
        burger.setY(burgerY);

        //Fries

        friesY += friesspeed;

        if (friesY >= frameheight) {
            friesY=-100;
//            friesX=-100;
        }
        if(friesY <= 24){
            //orangeX=500;
            friesX =  (int) (Math.random() * 3)  * movingX +fries.getWidth();
        }
        fries.setX(friesX);
        fries.setY(friesY);

        //Sausage

       sausageY += sausagespeed;

        if (sausageY >= frameheight) {
            sausageY=-100;
//            sausageX=-100;
        }
        if(sausageY <= 24){
            //orangeX=500;
            sausageX =  (int) (Math.random() * 3)  * movingX +sausage.getWidth();
        }
        sausage.setX(sausageX);
        sausage.setY(sausageY);

        if (score<0)
        {
            score=0;
            scoreLabel.setText("Score : " + "0");
        }
    }

    public void hitCheck(){

        //If center of ball is in the box, then it counts as a hit

        //Orange
        int orangeCenterX = orangeX + orange.getWidth();
        int orangeCenterY = orangeY + orange.getHeight();

        //0<=orangeCenterX<=boxSize
        //boxY<=orangeCenterY<=boxY + boxSize
        if(cycleX <= orangeCenterX && orangeCenterX <= cycleX + cycleweight && cycleY <= orangeCenterY && orangeCenterY <= cycleY + cycleheight){
            score -= 10;
            orangeY = -10;
//            sound.playHitSound();
            hit.start();
        }


        //Pink
        int berrycenterX = berryX + berry.getWidth();
        int berrycenterY = berryY + berry.getHeight();

        if(cycleX <= berrycenterX && berrycenterX <= cycleX + cycleweight && cycleY <= berrycenterY && berrycenterY <= cycleY + cycleheight){
            score -= 10;
            berryY = -10;
//            sound.playHitSound();
            hit.start();
        }

        //Black
        int burgercenterX = burgerX + burger.getWidth();
        int burgercenterY = burgerY + burger.getHeight();

        if(cycleX <= burgercenterX && burgercenterX <= cycleX + cycleweight && cycleY <= burgercenterY && burgercenterY <= cycleY + cycleheight){
            //Stop Timer
//            timer.cancel();
//            timer = null;
//
//            sound.playOverSound();
            score += 20;
            burgerY = -10;
//            over.start();
//            sound.playHitSound();
        }

        //Fruit2
        int friescenterX = friesX + fries.getWidth();
        int friescenterY = friesY + fries.getHeight();

        if(cycleX <= friescenterX && friescenterX <= cycleX + cycleweight && cycleY <= friescenterY && friescenterY <= cycleY + cycleheight){
            score += 20;
            friesY = -10;
//            sound.playOverSound();
//            over.start();
        }

        //Jf3
        int sausagecenterX = sausageX + sausage.getWidth();
        int sausagecenterY = sausageY + sausage.getHeight();

        if(cycleX <= sausagecenterX && sausagecenterX <= cycleX + cycleweight && cycleY <= sausagecenterY && sausagecenterY <= cycleY + cycleheight){
            score -= 10;
            sausageY = -10;
//            over.start();

//            sound.playHitSound();
        }
        if (score<0)
        {
            score=0;
            scoreLabel.setText("Score : " + "0");
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!start_flag){
            start_flag = true;

//            startLabel.setVisibility(View.GONE);

            FrameLayout frame = findViewById(R.id.frame);
            frameheight = frame.getHeight();
            framewidth = frame.getWidth();
            cycleheight = cycle.getHeight();
            cycleweight = cycle.getWidth();

            cycleY = (int)cycle.getY();
            cycleX = (int)cycle.getX();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void run() {
                            changeposition();
                        }
                    });
                }
            },1000,20);
        }else{
            if(event.getAction()== MotionEvent.ACTION_DOWN){
                action_flag = true;
            }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                action_flag = false;
            }
        }
        return super.onTouchEvent(event);
    }

    //Disable Return Button
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}